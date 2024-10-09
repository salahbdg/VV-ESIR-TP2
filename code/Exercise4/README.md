# Code of your exercise

Put here all the code created for this exercise
```Java
package fr.istic.vv;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PrivateElementChecker extends VoidVisitorAdapter<Void> {
    //store the details of the private fields without public getters
    private final List<String> report = new ArrayList<>();


    @Override
    public void visit(CompilationUnit unit, Void arg) {
// extracting the package name if it does exist
        String nP = unit.getPackageDeclaration().map(pd -> pd.getName().toString()).orElse("None");
        for (TypeDeclaration<?> tp : unit.getTypes()) {
            if (tp instanceof ClassOrInterfaceDeclaration) {
// call the corresponding method depending on the type of declaration
                visitorClassOrInterface((ClassOrInterfaceDeclaration) tp,namePackage);
            }
        }
        super.visit(unit, arg);
    }
// method to search for all the private fields and the public getters
    public void visitorClassOrInterface(ClassOrInterfaceDeclaration cd, String namePackage) {
        if (!cd.isPublic()) {return;}
        String name = cd.getName().toString();
        Map<String, FieldDeclaration> prvF = new HashMap<>();
        Set<String> pubF = new HashSet<>();//storing getters

        //Collection of all private fields
        for (FieldDecleration fd : cd.getFields()) {
            if (fd.isPrivate()) { //just the fiels that are private
                for (VariableDeclarator vd : fd.getVariables()) {
                    prvF.put(vd.getName().toString(), fd);
                }
            }
        }

        //collection of all public getters
        for (MethodDeclaration mtd : cd.getMethods()) {
            if (mtd.isPublic()) {
                String mtdName = mtd.getName().toString();
                //check if it's a getter
                if (mtdName.startsWith("get") && mtd.getParameters().isEmpty()) {
                    String fname = methodName.substring(3,4).toLowerCase() + methodName.substring(4);
                    pubF.add(fname);
                }
            }
        }

        //checking if there's no public getters for the private fields
        for (String fname : privateFields.keySet()) {
            if (!pubF.contains(fname)){
                report.add(String.format("Fields : %s, Class: %s", fname, name));
            }
        }
    }

//generating the report with all the field and their corresponding class
    public void generateReport(String fpath) {
        try(FileWriter wr = new FileWriter(fpath)){
            wr.write("Private fields without getters: \n");
            for (String ent: report){
                wr.write(ent + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
