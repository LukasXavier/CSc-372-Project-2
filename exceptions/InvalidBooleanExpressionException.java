/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: InvalidBooleanExpressionException.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This should be thrown when a string falls into BooleanExpression() but can't be properly parsed
 */

package exceptions;

public class InvalidBooleanExpressionException extends CompileException {
    public InvalidBooleanExpressionException(String s) {
        super("InvalidBooleanExpressionException: Unable to parse line: " + s + " into a boolean expression");
    }
}
