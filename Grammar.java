import java.util.HashMap;

import exceptions.*;

public class Grammar {
    private int argc;
    private HashMap<String, String> variables;
    private Patterns p;

    public Grammar() {
        argc = 0;
        variables = new HashMap<String, String>();
        p = new Patterns();
    }

    private String varType(String s) throws InvalidTypeException {
        if (p.sLit.matcher(s).find()) { return "String "; }
        else if (p.chars.matcher(s).find()) { return "char "; }
        else if (p.bool.matcher(s).find()) { return "boolean "; }
        else if (p.ints.matcher(s).find()) { return "int "; }
        throw new InvalidTypeException(s);
    }
    
    public String getArgs(String[] args, String var) throws CompileException {
        argc++;
        if (args.length <= argc) {
            throw new ArgumentsNotFoundException();
        }
        variables.put(var, varType(args[argc]));
        return varType(args[argc]) + var + " = " + args[argc] + ";";
    }
}
