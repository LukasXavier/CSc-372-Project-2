/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: InvalidTypeException.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This should be thrown when a variable or primitive does not match the regex for a valid type
 */

 package exceptions;

public class InvalidTypeException extends CompileException {
    public InvalidTypeException(String s) { super("InvalidTypeException: Right-side does not match any valid type: " + s); }
    
}
