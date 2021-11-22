package exceptions;

public class IntExprException extends CompileException {
    // Invalid Integer Expression: [expression]
    public IntExprException(String s) {super("IntExprException: Invalid Integer Expression: " + s);}
}