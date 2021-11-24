/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: Grammar.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This class handles all the grammar parsing
 */

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

    /**
     * Sets up private fields
     * @param args
     */
    public Grammar(String[] args) {
        this.args = args;
        argc = 0;
        variables = new HashMap<String, String>();
        p = new Patterns();
    }

    /**
     * Converts a LEMR for loop expression into java
     * @param line the line to be converted
     * @return the java equivalent for loop
     * @throws CompileException
     */
    public String forILoop(String line) throws CompileException {
        // uses the matcher for the groups
        Matcher m = p.forI.matcher(line);
        m.find();
        // you are not allowed to re-use a variable in a for loop
        if (variables.get(m.group(1)) != null) {
            throw new InvalidForLoopException(m.group(1)); 
        }
        // constructs the java for loop and does compile checking
        return "for (" + variableType(m.group(4)) + " " + m.group(1) + " = " + primitive(m.group(4)) + "; " +
                m.group(1) + " < " + primitive(m.group(5)) + "; " + 
                variableAssignment(m.group(1) + m.group(2) + "=" + m.group(3)).replace(";", "") +
                ") {" + expression(m.group(6)) + "}";
    }

    /**
     * determines the type of the passed in line
     * @param line the line to check it's type
     * @return return the type that line is as a String
     * @throws InvalidTypeException
     */
    private String variableType(String line) throws InvalidTypeException {
        if (p.sLit.matcher(line).find()) { return "String "; }
        if (p.chars.matcher(line).find()) { return "char "; }
        if (p.bool.matcher(line).find()) { return "boolean "; }
        if (p.bExpr.matcher(line).find()) { return "boolean "; }
        if (p.ints.matcher(line).find()) { return "int "; }
        if (p.iExpr.matcher(line).find()) {return "int "; }
        throw new InvalidTypeException(line);
    }

    /**
     * handles normal variable assignment, re-assignment, incrementing, and
     * getting command line arguments
     * @param line line to be parsed
     * @return the java equivalent for variable assignment
     * @throws CompileException
     */
    public String variableAssignment(String line) throws CompileException {
        // handles getting command line arguments
        Matcher m = p.args.matcher(line);
        if (m.find()) {
            argc++;
            // throw error if the user asks for more arguments than are passed in
            if (args.length <= argc) {
                throw new ArgumentsNotFoundException();
            }
            // puts the new variable into our type hashmap
            variables.put(m.group(1), variableType(args[argc]));
            // returns [varType] [varName] = [value];
            return variableType(args[argc]) + m.group(1) + " = " + args[argc] + ";";
        // handles variable assignment
        } m = p.vAssn.matcher(line);
        if (m.find()) {
            String varName = m.group(1);
            String varType = variableType(m.group(2));
            // checks if the variable is in our type hashmap
            if (variables.get(varName) == null) {
                // checks if the right side is in the hashmap
                if (variables.get(m.group(2)) != null) {
                    // puts the new variable in our hashmap as the type of the right
                    variables.put(varName, variables.get(m.group(2)));
                    // returns [varType] [varName] = [value];
                    return variables.get(m.group(2)) + varName + " = " + primitive(m.group(2)) + ";";
                }
                // puts the new variable in our has map
                variables.put(varName, varType);
                // returns [varType] [varName] = [value];
                return varType + varName + " = " + primitive(m.group(2)) + ";";
            }
            // checks that the variable type matches what it was previously
            if (variables.get(varName).equals(varType)) {
                // returns [varName] = [value];
                return varName + " = " + typeExpression(m.group(2), varType) + ";";
            }
            // caused by re-assigning a variable to a new type
            throw new TypeMismatchException(varName, varType);
        // handles incrementing a variable
        } m = p.iIncrementor.matcher(line);
        if (m.find()) {
            // gets the varName from either group 1 or 3
            String varName = (m.group(1) == null) ? m.group(3) : m.group(1);
            // if group 2 is null then it is a [varName] [op] [value] incrementing
            if (m.group(2) == null) {
                // return [varName] [op] [values];
                return varName + " " + m.group(4) + " " + m.group(5) + ";";
            }
            // returns [varName][op];
            return varName + " " + m.group(2) + ";";
        }
        // throws an error if it does not match any pattern
        throw new MalformedVariableAssignmentException(line);
    }

    /**
     * checks the type of the passed in line
     * @param line line to check type
     * @param varType assumed type
     * @return the line if it is of the correct type
     * @throws CompileException
     */
    private String typeExpression(String line, String varType) throws CompileException {
        if (varType == "boolean ") { return booleanExpression(line); }
        if (varType == "int ") { return integerExpression(line); }
        if (varType == "String ") { return line; }
        if (varType == "char ") { return line; }
        throw new InvalidTypeExpressionException(line);
    }

    /**
     * Validates the passed in line as an integer expression
     * @param line line to be checked
     * @return the java equivalent of the line
     * @throws CompileException
     */
    private String integerExpression(String line) throws CompileException {
        Matcher m = p.iExpr.matcher(line);
        if (m.find()) {
            // if group 2 is blank then it is just a singleton
            if (m.group(2).equals("")) {
                return primitive(m.group(1));
            }
            // else checks the rest of the line
            return primitive(m.group(1)) + " " + m.group(2) + " " + integerExpression(m.group(3));
        }
        // throws except when it doesn't match
        throw new InvalidIntegerExpressionException(line);
    }
    
    /**
     * Validates the passed in line as a boolean expression
     * @param line line to be checked
     * @return the java equivalent of the line
     * @throws CompileException
     */
    private String booleanExpression(String line) throws CompileException {
        Matcher m = p.bExpr.matcher(line);
        if (m.find()) {
            // else checks the rest of the line
            return primitive(m.group(1)) + " " + m.group(2) + " " + primitive(m.group(3));
        // if it does not match boolean Expression, it checks if it is a boolean
        } m = p.bool.matcher(line);
        // if it is valid return
        if (m.find()) { return line; }
        // check if it is negated
        if (line.charAt(0) == '!') { return "!" + primitive(line.replace("!", "")); }
        // checks if it is an already defined variable
        if (variables.get(line).equals("boolean ")) { return line; }
        // throws except when it doesn't match
        throw new InvalidBooleanExpressionException(line);
    }

    /**
     * Used to check the if a singleton is valid
     * @param line line to check
     * @return the line if it matches
     * @throws InvalidTypeException
     */
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

    /**
     * Used to count a the curly braces
     * @param s the string to count from
     * @param c the character to count
     * @return the amount of times c appears in s
     */
    private int charCount(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    /**
     * recursively checks the passed in line for valid expressions
     * @param line line to be checked
     * @return the java equivalent of the passed in line
     * @throws CompileException
     */
    private String expression(String line) throws CompileException {
        if (line.equals("")) { return "";}
        // highest precedence 
        if (p.forI.matcher(line).find()) { return forILoop(line); }
        if (p.conditional.matcher(line).find()) { return conditional(line); }
        if (p.conElse.matcher(line).find()) { return conditional(line); }
        // splits the line into it's components
        if (line.contains("\0")) {
            String[] exprs = line.split("\0");
            String cur = "";
            String tail = "";
            int bracketCounter = 0;
            int i = 0;
            // counts the amount of opening and closing braces
            while(i < exprs.length) {
                if (exprs[i].contains("{")) {bracketCounter += charCount(exprs[i], '{');} 
                if (exprs[i].contains("}")) {bracketCounter -= charCount(exprs[i], '}');} 
                if (bracketCounter > 0) {cur += exprs[i] + '\0';} 
                else {cur += exprs[i] + '\0';break;}
                i++;
            }
            // if a brace doesn't close
            if (bracketCounter != 0) {
                throw new MissingCurlyBraceException(line);
            }
            // re-compresses the string
            for (int j = i+1; j < exprs.length; j++) {
                tail += exprs[j] + '\0';
            }
            // if the line is just one expression
            if (tail.strip().replaceAll("\0", "").equals("")) {
                // strip the last null terminal
                if (cur.contains("\0")) {
                    return expression(cur.substring(0, cur.lastIndexOf('\0')));
                }
                else {
                    return expression(cur);
                }
            }
            else {
                // recurses down both sides
                return expression(cur) + expression(tail);
            }
        }
        // lower precedence
        if (p.vAssn.matcher(line).find()) { return variableAssignment(line); }
        if (p.iIncrementor.matcher(line).find()) { return variableAssignment(line); }
        if (p.print.matcher(line).find()) { return print(line); }
        return primitive(line);
    }

    /**
     * Validates the conditional expression
     * @param line line to be converted
     * @return the java equivalent of line
     * @throws CompileException
     */
    public String conditional(String line) throws CompileException {
        String res = "";
        Matcher m = p.conditional.matcher(line);
        Matcher n = p.conElse.matcher(line);
        // matches if and elif and converts them to java
        if (m.find()) {
            if (m.group(1).equals("if")) {
                res += "if (" + booleanExpression(m.group(2)) + ") {";
            }
            else if (m.group(1).equals("elif")) {
                res += "else if (" + booleanExpression(m.group(2))  + ") {";
            }
            return res + expression(m.group(3)) + "}";
        }
        // matches else and converts it to java
        n.find();
        return res + "else {" + expression(n.group(1)) + "}";
    }

    /**
     * converts line to java
     * @param line line to be converted
     * @return the java equivalent of line
     * @throws CompileException
     */
    public String print(String line) throws CompileException {
        Matcher m = p.print.matcher(line);
        m.find();
        String res = (m.group(2).equals("")) ? "" : primitive(m.group(2));
        // >> is a println and > is a print
        if (m.group(1).equals(">>")) {
            return "System.out.println(" + res + ");"; 
        }
        return "System.out.print(" + res + ");";
    }
}