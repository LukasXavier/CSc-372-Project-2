/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: TypeMismatchException.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This should be thrown when a variable is being reassigned to a different type
 */

 package exceptions;

public class TypeMismatchException extends CompileException {
    // [varName] cannot be converted to type [Type]
    public TypeMismatchException(String varName, String type) { super("TypeMismatchException: " + varName + " cannot be converted to type " + type); }
}
