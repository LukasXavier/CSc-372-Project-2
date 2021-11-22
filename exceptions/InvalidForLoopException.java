package exceptions;

public class InvalidForLoopException extends CompileException {
    public InvalidForLoopException(String s) {
        super("InvalidForLoopException: Variable " + s + " has already been initialized");
    }
}
