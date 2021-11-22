package exceptions;

public class InvalidTypeException extends CompileException {
    // Invalid Integer Expression: [expression]
    public InvalidTypeException(String s) { super("Right-side does not match any valid type: " + s); }
    
}
