/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: InvalidForLoopException.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This should be thrown in a variable is reused in the forI loop
 */

 package exceptions;

public class InvalidForLoopException extends CompileException {
    public InvalidForLoopException(String s) {
        super("InvalidForLoopException: Variable " + s + " has already been initialized");
    }
}
