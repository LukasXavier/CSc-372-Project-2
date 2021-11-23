/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: InvalidTypeExpressionException.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This should be thrown when TypeExpression() cannot resolve the type of the right side
 */

 package exceptions;

public class InvalidTypeExpressionException extends CompileException {

    public InvalidTypeExpressionException(String s) {
        super("InvalidTypeExpressionException: Unable to infer type from: " + s);
    }
    
}
