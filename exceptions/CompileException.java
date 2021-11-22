package exceptions;

public class CompileException extends Exception {

    public CompileException(String s) {
        super("Compiler Error: " + s);
    }
}
