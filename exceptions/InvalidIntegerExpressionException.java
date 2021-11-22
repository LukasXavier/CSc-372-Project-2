package exceptions;

public class InvalidIntegerExpressionException extends CompileException {

    public InvalidIntegerExpressionException(String s) {
        super("InvalidIntegerExpressionException: Unable to parse line: " + s + " into an integer expression");
    }
}
