/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: ArgumentsNotFoundException.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This should be thrown when the user tries to call 'getArgs()' more than the passed in arguments
 */

package exceptions;

public class ArgumentsNotFoundException extends CompileException {
    public ArgumentsNotFoundException() { super("ArgumentsNotFoundException: No more arguments were found"); }
}
