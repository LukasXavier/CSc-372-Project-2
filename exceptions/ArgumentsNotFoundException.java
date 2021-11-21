package exceptions;

public class ArgumentsNotFoundException extends CompileException {
    public ArgumentsNotFoundException() { super("No more arguments were found"); }
}
