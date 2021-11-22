package exceptions;

public class ArgumentsNotFoundException extends CompileException {
    public ArgumentsNotFoundException() { super("ArgumentsNotFoundException: No more arguments were found"); }
}
