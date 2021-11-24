/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: MalformedVariableAssignmentException.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This should be thrown when a string falls into variableAssignment but not be assigned
 */

 package exceptions;

public class MalformedVariableAssignmentException extends CompileException{
    public MalformedVariableAssignmentException(String s) {
        super("MalformedVariableAssignmentException: Variable Assignment " + s + " Is not valid");
    }
}
