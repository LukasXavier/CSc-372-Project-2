package exceptions;

public class InvalidTypeExpressionException extends CompileException {

    public InvalidTypeExpressionException(String s) {
        super("InvalidTypeExpressionException: Unable to infer type from: " + s);
    }
    
}
