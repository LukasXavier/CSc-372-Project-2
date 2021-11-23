/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: InvalidIntegerExpressionException.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This should be thrown when a string falls into IntegerExpression() but can't be properly parsed
 */

 package exceptions;

public class InvalidIntegerExpressionException extends CompileException {

    public InvalidIntegerExpressionException(String s) {
        super("InvalidIntegerExpressionException: Unable to parse line: " + s + " into an integer expression");
    }
}
