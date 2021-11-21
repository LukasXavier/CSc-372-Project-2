package exceptions;

public class InvalidTypeException extends CompileException {
    // Invalid Integer Expression: [expression]
    public InvalidTypeException(String s) {super("Invalid type: " + s);}
    
}
