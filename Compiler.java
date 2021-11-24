/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: FileParser.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This is the main file
 * 
 * <p><b>You should probably run <code>javac *.java</code> to make sure everything is compiled
 * continuing.</b>
 * 
 * <p>This program requires at least a command line argument passed in like 
 * <code>java Compiler [file]</code> where the file can either a .txt or .lemr
 */

import java.util.ArrayList;

import exceptions.CompileException;

public class Compiler {

    public static void main(String[] args) {
        // Grammar contains all the grammar parsing and FileParser returns all the lines in the passed in file
        Grammar g = new Grammar(args);
        ArrayList<String> lines = new FileParser().getLines(args);
        String res = "public class " + args[0].replace(".txt", "").replace(".lemr", "") +  "{public static void main(String[] args) {";
        // all public methods of Grammar throw either CompilerException or a subclass Exception 
        try {
            String expressionType;
            // for each expression in line check if it matches the anything based on precedence
            for (String s : lines) {
                expressionType = s.split(" ")[0];
                if (expressionType.equals("fori")) { res += g.forILoop(s);}
                else if (expressionType.equals("if")) { res += g.conditional(s);}
                else if (expressionType.equals("elif")) { res += g.conditional(s);}
                else if (expressionType.equals("else")) { res += g.conditional(s);}
                else if (expressionType.contains(">{")) { res += g.print(s);}
                // variableAssignment is the catch all, if it is not valid, this should throw an error
                else { res += g.variableAssignment(s); }
            }
            // adds the closing braces for a java file
            System.out.println(res + "}}");
        } catch (CompileException e) {
            // prints out an error message about what caused a compile Error
            System.err.println(e.getMessage());
        }
    }
}
