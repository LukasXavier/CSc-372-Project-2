package exceptions;

public class TypeMismatchException extends CompileException {
    // [varName] cannot be converted to type [Type]
    public TypeMismatchException(String varName, String type) { super("TypeMismatchException: " + varName + " cannot be converted to type " + type); }
}
