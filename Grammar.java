import java.util.HashMap;
import java.util.regex.Matcher;
import exceptions.InvalidForLoopException;
import exceptions.CompileException;
import exceptions.InvalidBooleanExpressionException;
import exceptions.InvalidIntegerExpressionException;
import exceptions.ArgumentsNotFoundException;
import exceptions.InvalidTypeException;
import exceptions.InvalidTypeExpressionException;
import exceptions.MalformedVariableAssignmentException;
import exceptions.MissingCurlyBraceException;
import exceptions.TypeMismatchException;

public class Grammar {
    private String[] args;
    private int argc;
    private HashMap<String, String> variables;
    private Patterns p;

    public Grammar(String[] args) {
        this.args = args;
        argc = 0;
        variables = new HashMap<String, String>();
        p = new Patterns();
    }

    public String forILoop(String line) throws CompileException {
        Matcher m = p.forI.matcher(line);
        m.find();
        if (variables.get(m.group(1)) != null) {
            throw new InvalidForLoopException(m.group(1)); 
        }
        return "for (" + variableType(m.group(4)) + " " + m.group(1) + " = " + primitive(m.group(4)) + "; " +
                m.group(1) + " < " + primitive(m.group(5)) + "; " + 
                variableAssignment(m.group(1) + m.group(2) + "=" + m.group(3)).replace(";", "") +
                ") {" + expression(m.group(6)) + "}";
    }

    private String variableType(String line) throws InvalidTypeException {
        if (p.sLit.matcher(line).find()) { return "String "; }
        else if (p.chars.matcher(line).find()) { return "char "; }
        else if (p.bool.matcher(line).find()) { return "boolean "; }
        else if (p.bExpr.matcher(line).find()) { return "boolean "; }
        else if (p.ints.matcher(line).find()) { return "int "; }
        else if (p.iExpr.matcher(line).find()) {return "int "; }
        throw new InvalidTypeException(line);
    }

    public String variableAssignment(String line) throws CompileException {
        Matcher m = p.args.matcher(line);
        if (m.find()) {
            argc++;
            if (args.length <= argc) {
                throw new ArgumentsNotFoundException();
            }
            variables.put(m.group(1), variableType(args[argc]));
            return variableType(args[argc]) + m.group(1) + " = " + args[argc] + ";";
        } m = p.vAssn.matcher(line);
        if (m.find()) {
            String varName = m.group(1);
            String varType = variableType(m.group(2));
            if (variables.get(varName) == null) {
                if (variables.get(m.group(2)) != null) {
                    variables.put(varName, variables.get(m.group(2)));
                    return variables.get(m.group(2)) + varName + " = " + primitive(m.group(2)) + ";";
                }
                variables.put(varName, varType);
                return varType + varName + " = " + primitive(m.group(2)) + ";";
            }
            if (variables.get(varName).equals(varType)) {
                return varName + " = " + typeExpression(m.group(2), varType) + ";";
            }
            throw new TypeMismatchException(varName, varType);
        } m = p.iIncrementor.matcher(line);
        if (m.find()) {
            String varName = (m.group(1) == null) ? m.group(3) : m.group(1);
            if (m.group(2) == null) {
                return varName + " " + m.group(4) + " " + m.group(5) + ";";
            }
            return varName + " " + m.group(2) + ";";
        }
        throw new MalformedVariableAssignmentException(line);
    }

    private String typeExpression(String line, String varType) throws CompileException {
        if (varType == "boolean ") { return booleanExpression(line); }
        if (varType == "int ") { return integerExpression(line); }
        if (varType == "String ") { return line; }
        if (varType == "char ") { return line; }
        throw new InvalidTypeExpressionException(line);
    }

    private String integerExpression(String line) throws CompileException {
        Matcher m = p.iExpr.matcher(line);
        if (m.find()) {
            if (m.group(2).equals("")) {
                return primitive(m.group(1));
            }
            return primitive(m.group(1)) + " " + m.group(2) + " " + integerExpression(m.group(3));
        }
        throw new InvalidIntegerExpressionException(line);
    }
    
    private String booleanExpression(String line) throws CompileException {
        Matcher m = p.bExpr.matcher(line);
        if (m.find()) {
            if (m.group(2) == null) { return m.group(1); }
            return primitive(m.group(1)) + " " + m.group(2) + " " + primitive(m.group(3));
        } m = p.bool.matcher(line);
        if (m.find()) { return line; }
        if (line.charAt(0) == '!') { return "!" + primitive(line.replace("!", "")); }
        if (variables.get(line).equals("boolean ")) { return line; }
        throw new InvalidBooleanExpressionException(line);
    }

    private String primitive(String line) throws InvalidTypeException {
        if (p.ints.matcher(line).find()) { return line; }
        if (p.chars.matcher(line).find()) { return line; }
        if (p.bool.matcher(line).find()) { return line; }
        if (p.sLit.matcher(line).find()) { return line; }
        if (p.iExpr.matcher(line).find()) { return line; }
        if (p.bExpr.matcher(line).find()) { return line; }
        if (variables.get(line) != null) { return line; }
        if (line.strip().replaceAll("\0", "").replaceAll("}", "").equals("")) {return "}";}
        throw new InvalidTypeException(line);
    }

    private int charCount(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    private String expression(String line) throws CompileException {
        if (line.equals("")) { return "";}
        if (p.forI.matcher(line).find()) { return forILoop(line); }
        if (p.conditional.matcher(line).find()) { return conditional(line); }
        if (p.conElse.matcher(line).find()) { return conditional(line); }
        if (line.contains("\0")) {
            String[] exprs = line.split("\0");
            String cur = "";
            String tail = "";
            int bracketCounter = 0;
            int i = 0;
            while(i < exprs.length) {
                if (exprs[i].contains("{")) {bracketCounter += charCount(exprs[i], '{');} 
                if (exprs[i].contains("}")) {bracketCounter -= charCount(exprs[i], '}');} 
                if (bracketCounter > 0) {cur += exprs[i] + '\0';} 
                else {cur += exprs[i] + '\0';break;}
                i++;
            }
            if (bracketCounter != 0) {
                throw new MissingCurlyBraceException(line);
            }
            for (int j = i+1; j < exprs.length; j++) {
                tail += exprs[j] + '\0';
            }
            if (tail.strip().replaceAll("\0", "").equals("")) {
                if (cur.contains("\0")) {
                    return expression(cur.substring(0, cur.lastIndexOf('\0')));
                }
                else {
                    return expression(cur);
                }
            }
            else {
                return expression(cur) + expression(tail);
            }
        }
        if (p.vAssn.matcher(line).find()) { return variableAssignment(line); }
        if (p.iIncrementor.matcher(line).find()) { return variableAssignment(line); }
        if (p.print.matcher(line).find()) { return print(line); }
        return primitive(line);
    }

    public String conditional(String line) throws CompileException {
        String res = "";
        Matcher m = p.conditional.matcher(line);
        Matcher n = p.conElse.matcher(line);
        if (m.find()) {
            if (m.group(1).equals("if")) {
                res += "if (" + booleanExpression(m.group(2)) + ") {";
            }
            else if (m.group(1).equals("elif")) {
                res += "else if (" + booleanExpression(m.group(2))  + ") {";
            }
            return res + expression(m.group(3)) + "}";
        }
        n.find();
        return res + "else {" + expression(n.group(1)) + "}";
    }

    public String print(String line) throws CompileException {
        Matcher m = p.print.matcher(line);
        m.find();
        if (m.group(1).equals(">>")) {
            return "System.out.println(" + primitive(m.group(2)) + ");"; 
        }
        return "System.out.print(" + primitive(m.group(2)) + ");";
    }
}