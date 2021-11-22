import java.util.ArrayList;

import exceptions.CompileException;

public class Compiler {

    public static void main(String[] args) {
        Grammar g = new Grammar(args);
        ArrayList<String> lines = new FileParser().getLines(args);
        String res = "public class " + args[0].replace(".txt", "") +  "{public static void main(String[] args) {";
        try {
            String expressionType;
            for (String s : lines) {
                expressionType = s.split(" ")[0];
                if (expressionType.equals("fori")) { res += g.forILoop(s);}
                else if (expressionType.equals("if")) { res += g.conditional(s);}
                else if (expressionType.equals("elif")) { res += g.conditional(s);}
                else if (expressionType.equals("else")) { res += g.conditional(s);}
                else if (expressionType.contains(">{")) { res += g.print(s);}
                else { res += g.variableAssignment(s); }
            }
            System.out.println(res + "}}");
        } catch (CompileException e) {
            System.out.println(e.getMessage());
        }
    }
}