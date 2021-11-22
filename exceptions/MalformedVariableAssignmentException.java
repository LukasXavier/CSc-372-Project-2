package exceptions;

public class MalformedVariableAssignmentException extends CompileException{
    public MalformedVariableAssignmentException(String s) {
        super("Variable Assignment " + s + " Is not valid");
    }
}
