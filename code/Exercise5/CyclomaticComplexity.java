package fr.istic.vv;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class CyclomaticComplexity extends VoidVisitorAdapter<Void> {
    private final List<String> report = new ArrayList<>();
    private final List<Integer> compVal= new ArrayList<>();

    @Override
    public void visit(CompilationUnit cu, Void arg) {
        String pN = cu.getPackageDeclaration().map(pd -> pd.getName().toString()).orElse("None");
        for (ClassOrInterfaceDeclaration classOrInterface : cu.findAll(ClassOrInterfaceDeclaration.class)) {
            classOrInterface.getMethods().forEach(method -> visitMethod(method, pN, classOrInterface.getName().toString()));
        }
        super.visit(cu, arg);
    }

    //method to visit the nodes
    private void visitMethod(MethodDeclaration mtd, String pN, String className) {
        // Start Cyclomatic Complexity at 1 (by definition)
        int c = 1;

        // Count control structures
        c += mtd.findAll(IfStmt.class).size();
        c += mtd.findAll(ForStmt.class).size();
        c += mtd.findAll(WhileStmt.class).size();
        c += mtd.findAll(DoStmt.class).size();
        c += mtd.findAll(SwitchEntry.class).size();
        c += mtd.findAll(CatchClause.class).size();

        // Collect the details of the methods and their complexity
        String mtdName = mtd.getName().toString();
        String param = mtd.getParameters().toString();

        //debugg just to verify if it works 
        System.out.println(String.format("Method: %s in Class: %s with CC: %d",mtd,className,c));



        report.add(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%d</td></tr>",
                pN, className, mtdName, param, c));


        // Add to complexity values list for histogram
        compVal.add(c);
    }


public void generateReport(String filePath) {
    try (FileWriter writer = new FileWriter(filePath)) {
        //start of the HTML document and the table headers
        writer.write("<html><body><table border='1'>\n");
        writer.write("<tr><th>Package</th><th>Class</th><th>Method</th><th>Parameters</th><th>Cyclomatic Complexity</th></tr>\n");

        // Write each entry as an HTML table row
        for (String entry : report) {
            writer.write(entry + "\n");
        }

        // Close the HTML document
        writer.write("</table></body></html>\n");

        System.out.println("HTML report generated successfully.");
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    public List<Integer> getComplexityValues() {
        return compVal;
    }
}
