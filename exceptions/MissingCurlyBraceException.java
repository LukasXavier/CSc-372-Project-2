/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: MissingCurlyBraceException.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This should be thrown when expression() can't find a closing brace
 */

package exceptions;

public class MissingCurlyBraceException extends CompileException {
    public MissingCurlyBraceException(String s) {
        super("MissingCurlyBraceException: " + s + " is missing a closing or opening brace");
    }
}
