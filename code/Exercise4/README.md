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
    //store the details of the fields without getters
    private final List<String> report = new ArrayList<>();


    @Override
    public void visit(CompilationUnit unit, Void arg) {
        String nP = unit.getPackageDeclaration().map(pd -> pd.getName().toString()).orElse("None");
        for (TypeDeclaration<?> tp : unit.getTypes()) {
            if (tp instanceof ClassOrInterfaceDeclaration) {
                visitorClassOrInterface((ClassOrInterfaceDeclaration) tp,namePackage);
            }
        }
        super.visit(unit, arg);
    }

    public void visitorClassOrInterface(ClassOrInterfaceDeclaration cd, String namePackage) {
        if (!cd.isPublic()) {return;}
        String name = cd.getName().toString();
        Map<String, FieldDeclaration> privateFields = new HashMap<>();
        Set<String> publicFields = new HashSet<>();//getters

        //Collection of all private fields
        for (FieldDeclaration fd : cd.getFields()) {
            if (fd.isPrivate()) {
                for (VariableDeclarator vd : fd.getVariables()) {
                    privateFields.put(vd.getName().toString(), fd);
                }
            }
        }

        //collection of all public getters
        for (MethodDeclaration mtd : cd.getMethods()) {
            if (mtd.isPublic()) {
                String methodName = mtd.getName().toString();
                //check if it's a getter
                if (methodName.startsWith("get") && mtd.getParameters().isEmpty()) {
                    String fname = methodName.substring(3,4).toLowerCase() + methodName.substring(4);
                    publicFields.add(fname);
                }
            }
        }

        //checking if there's no public getters
        for (String fname : privateFields.keySet()) {
            if (!publicFields.contains(fname)){
                report.add(String.format("Fields : %s, Package: %s, Class: %s", fname, namePackage, name));
            }
        }
    }

    public void generateReport(String fpath) {
        try(FileWriter writer = new FileWriter(fpath)){
            writer.write("Private fields without getters: \n");
            for (String ent: report){
                writer.write(ent + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
