# Code of your exercise

Put here all the code created for this exercise
```Java
package fr.istic.vv;

import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        
        SourceRoot root = new SourceRoot(Paths.get(file.getAbsolutePath()));
       
        CyclomaticComplexity cp = new CyclomaticComplexity();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(cp, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
        //output

        cp.generateReport("CyclomaticComplexity.html");
        // Get the list of Cyclomatic Complexity values
        List<Integer> compVal = cp.getComplexityValues();

        // Generate the histogram in CSV format
        CyclomaticComplexityHistogram histGen = new CyclomaticComplexityHistogram(compVal);
        histogramGenerator.generateHistogramCSV("cc_histo.csv");

    }





}

```
# Example of the report generated   

![image](https://github.com/user-attachments/assets/a21ebe20-b96c-4b1b-9fcc-4654c6e20ddf)

