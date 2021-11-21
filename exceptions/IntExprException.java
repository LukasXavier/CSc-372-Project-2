package exceptions;

public class IntExprException extends CompileException {
    // Invalid Integer Expression: [expression]
    public IntExprException(String s) {super("Invalid Integer Expression: " + s);}
}