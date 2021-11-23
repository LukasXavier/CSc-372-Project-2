/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: CompileException.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This is the parent class of all Exceptions thrown in this project
 */

package exceptions;

public class CompileException extends Exception {

    public CompileException(String s) {
        super("CompilerException:\n"+s);
    }
}
