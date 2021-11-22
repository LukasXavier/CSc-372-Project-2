import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Old_compiler {

    private static int argc = 0;
    private static HashMap<String, String> variables;

    private static Pattern[] getPatterns() {
        // ^[\\w]+([\\s][+-[*]/%][\\s][\\w]+)+$

        // (left) (op) (right [recursive])
        // 5 + 4 - 3
        Pattern iExpr = Pattern.compile("^([\\w]+)[\\s]*((?:\\+|\\-|\\*|%|/?))[\\s]*(.+)*$");
        // ^([\\w]+)([\\s](\\||&|<|>|!|==)[\\s]([\\w]+))+$

        // (left) (op) (right [recursive])
        // true == true
        Pattern bExpr = Pattern.compile("(.+?)(?:\\||&|<|>|!|={2}?)(.+)");
        // ^([a-zA-Z]{1}[\\w]*)[\\s](=)[\\s]([\\w]+)$

        // (variableName) = (value)
        // stringVar = "stringLiteral"
        Pattern vAssn = Pattern.compile("^[\\s]*([a-zA-Z]{1}[\\w]*)[\\s]*[=][\\s]*(.+)$");

        // ("*")
        // "stringLiteral"
        Pattern sLit = Pattern.compile("^([\"][^\"]*[\"])$");
        // if[\\s]\\((.+)\\)[\\s]\\{(.+)\\}

        // if (condition) {(do)}
        // if (5 < var) { >{"you lost"}}
        Pattern conditional = Pattern.compile("^[\\s]*((?:if|elif?))[\\s]*(\\((.+?)(?:\\)))[\\s]*\\{{1}(.*)\\}{1}$");
        Pattern conElse = Pattern.compile("^[\\s]*((?:else?))[\\s]*\\{{1}(.*)\\}{1}$");

        // Pattern comments = Pattern.compile("(//.+)|(/[*].+[*]/)");

        // >{(content)} | >>{(content)}
        // >{"hello world"} >>{var}
        Pattern print = Pattern.compile("[\\s]*>\\{(.+)\\}|>>\\{(.+)\\}[\\s]*");

        // while (condition) {(do)}
        // while (i < 10) {i++}
        Pattern wLoop = Pattern.compile("^while[\\s]*\\((.+)\\)[\\s]*\\{(.+)\\}$");

        // fori ((incrementingVariable) (op) (amount), (starting inclusive), (end inclusive)) {(do)}
        // fori (i - 2, 100, 0) {>{i}}
        Pattern fiLoop = Pattern.compile("fori[\\s]*\\(([a-zA-Z]{1}[\\w]*)[\\s]*([+-[*]/])[\\s]*([\\w]+),[\\s]*([\\w]+),[\\s]*([\\w]+)\\)[\\s]*\\{(.+)\\}");
        Pattern args = Pattern.compile("^([a-zA-Z]{1}[\\w]*)[\\s]*=[\\s]*getArg\\(\\)$");
        Pattern ints = Pattern.compile("^[\\s]*[0-9]+[\\s]*$");
        Pattern chars = Pattern.compile("^\'[a-zA-Z]{1}\'$");
        Pattern bool = Pattern.compile("true|false");
        Pattern var = Pattern.compile(".+");
        return new Pattern[] {iExpr, bExpr, vAssn, sLit, conditional, print, wLoop, ints, chars, fiLoop, args, bool, var, conElse};
    }
    
    // DONE:
    private static String getArg(String[] args, Pattern p, String s) throws Exception {
        argc++;
        Matcher m = p.matcher(s);
        if (m.find()) {
            // if (args.length <= argc) {
            //     return m.group(1) + " = " + "None";
            // }
            variables.put(m.group(1), varType(args[argc]));
            return varType(args[argc]) + m.group(1) + " = " + args[argc] + ";";
        }
        throw new Exception();
    }

    // DONE:
    private static String fori(Pattern p, String s) throws Exception {
        Matcher m = p.matcher(s);
        if (m.find()) {
            return "for (" + vAssn(getPatterns()[2], m.group(1) + " = " + m.group(4)) + m.group(1) + " < " + m.group(5)
                   + "; " + m.group(1) + m.group(2) + "=" + m.group(3) + ") {" + expression(m.group(6)) + "}";
        }
        throw new Exception();
    }

    // WIP: DONE:?
    // parsed = [before operator, operator, after operator]
    private static String iExpr(String s) throws Exception {
        String[] parsed = getIExpr(s);
        if (parsed.length == 1) {
            return parsed[0];
        }
        else {
            return primitive(parsed[0]) + " " + parsed[1] + " " + iExpr(parsed[2]);
        }
    }

    // WIP: DONE:?
    private static String[] getIExpr(String s) throws Exception {
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '+') {
                return parseExpr(s, "+", i);
            }
            else if (chars[i] == '-') {
                return parseExpr(s, "-", i);
            }
            else if (chars[i] == '*') {
                return parseExpr(s, "*", i);
            }
            else if (chars[i] == '/') {
                return parseExpr(s, "/", i);
            }
            else if (chars[i] == '%') {
                return parseExpr(s, "%", i);
            }
        }
        return new String[] {primitive(s)};
    }

    // DONE:
    private static String[] parseExpr(String s, String operator, int i) {
        String[] parsed = new String[3];
        parsed[0] = s.substring(0, i);
        parsed[1] = operator;
        parsed[2] = s.substring(i+operator.length(), s.length());
        return parsed;
    }

    // WIP: DONE:?
    // parsed = [before operator, operator, after operator]
    private static String bExpr(Pattern p, String s) throws Exception {
        String[] parsed = getBExpr(s);
        if (parsed.length == 1) {
            return parsed[0];
        }
        else {
            if (parsed[0].equals("") && parsed[1].equals("!")) {
                return parsed[1] + " " + typeExpression(parsed[2]);
            }
            else {
                return typeExpression(parsed[0]) + " " + parsed[1] + " " + typeExpression(parsed[2]);
            }
            
        }
    }

    // WIP: DONE:?
    private static String[] getBExpr(String s) throws Exception {
        char[] chars = s.toCharArray();
        int equalCount = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '&') {
                return parseExpr(s, "&&", i);
            }
            else if (chars[i] == '|') {
                return parseExpr(s, "||", i);
            }
            else if (chars[i] == '<') {
                return parseExpr(s, "<", i);
            }
            else if (chars[i] == '>') {
                return parseExpr(s, ">", i);
            }
            else if (chars[i] == '!') {
                return parseExpr(s, "!", i);
            }
            else if (chars[i] == '=' && equalCount == 1) {
                String[] parsed = new String[3];
                parsed[0] = s.substring(0, i-1);
                parsed[1] = "==";
                parsed[2] = s.substring(i+1, s.length());
                return parsed;
            }
            else if (chars[i] == '=' && equalCount == 0) {
                equalCount++;
            }
            else if (chars[i] != '=' && equalCount == 1) {
                equalCount = 0;
            }
        }
        return new String[] {primitive(s)};
    }

    // WIP: DONE:? TODO: idk if I need that else 
    private static String primitive(String s) throws Exception {
        // System.out.println(s);
        Pattern[] patterns = getPatterns();
        Pattern ints = patterns[7];
        Pattern chars = patterns[8];
        Pattern bool = patterns[11];
        Pattern sLit = patterns[3];
        Matcher m = ints.matcher(s);
        if (m.find()) {
            return s;
        } m = chars.matcher(s);
        if (m.find()) {
            return s;
        } m = bool.matcher(s);
        if (m.find()) {
            return s;
        } m = sLit.matcher(s);
        if (m.find()) {
            return s;
        } m = patterns[0].matcher(s);
        if (m.find()) {
            return iExpr(s);
        }
        else {
            String temp = s.replaceAll("\\s", "").replaceAll("\0","");
            if (variables.get(s.strip()) != null) {
                return s;
            }
            else if (temp.equals("}") || temp.equals("")) {
                return "";
            }
        }
        throw new Exception();
    }
    
    // DONE:
    // TODO: Dis shit broken, fix the if (variables...) 
    private static String vAssn(Pattern p, String s) throws Exception {
        Matcher m = p.matcher(s);
        if (m.find()) {
            // TODO: needs checking for multi-expressions in m.group(2)
            if (variables.get(m.group(1)) == null) {
                variables.put(m.group(1), varType(m.group(2)));
                return varType(m.group(2)) + m.group(1) + " = " + primitive(m.group(2)) + ";"; 
            }
            else {
                String res = "";
                ArrayList<String> expressions = new ArrayList<String>();
                for (String str : m.group(2).strip().split("\0")) {
                    expressions.add(str);
                }
                if (variables.get(m.group(1)).equals(varType(m.group(2).strip().split("\0")[0]))) {
                    res += m.group(1) + " = " + typeExpression(expressions.get(0), variables.get(m.group(1))) + ";";
                }
                String temp = "";
                for (int i = 1; i < expressions.size(); i++) {
                    temp += expressions.get(i) + '\0';
                }
                return res += expression(temp);
            }
        }
        // TODO: TypeMismatchException
        throw new Exception();
    }

    // DONE: 
    private static String varType(String s) throws Exception {
        Pattern[] patterns = getPatterns();
        if (patterns[3].matcher(s).find()) {
            return "String ";
        }
        else if (patterns[8].matcher(s).find()) {
            return "char ";
        }
        else if (patterns[11].matcher(s).find()) {
            return "boolean ";
        }
        else if (patterns[7].matcher(s).find()) {
            return "int ";
        }
        else if (patterns[0].matcher(s).find()) {
            return "int ";
        }
        else if (patterns[1].matcher(s).find()) {
            return "boolean ";
        }
        throw new Exception();
    }

    private static int charCount(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }
    
    private static String conditional(Pattern p, String s) throws Exception {
        Matcher m = p.matcher(s);
        Pattern[] patterns = getPatterns();
        String res = "";
        System.out.println(s);
        Matcher n = getPatterns()[13].matcher(s);
        if (m.find()) {
            if (m.group(1).equals("if")) {
                res += "if (" + bExpr(patterns[1], m.group(3))  + ") {"; 
            }
            else if (m.group(1).equals("elif")) {
                res += "else if (" + bExpr(patterns[1], m.group(3))  + ") {"; 
            }
            return res + expression(m.group(4)) + "}";
        }
        else if (n.find()) {
            return res + "else {" + expression(n.group(2)) + "}";
        }
        throw new Exception();
    }
    
    // DONE:
    private static String print(Pattern p, String s) throws Exception {
        Matcher m = p.matcher(s);
        if (m.find()) {
            if (s.contains(">>")) {
                return "System.out.println(" + primitive(m.group(2)) + ");";
            } else {
                return "System.out.print(" + primitive(m.group(1)) + ");";
            }
        }
        throw new Exception();
    }

    // DONE:
    private static String typeExpression(String s, String type) throws Exception {
        Pattern[] patterns = getPatterns();
        Pattern bExpr = patterns[1];
        if (type == "boolean ") {
            return bExpr(bExpr, s);
        } 
        else if (type == "int ") {
            return iExpr(s);
        }
        throw new Exception();
    }

    // DONE:
    // WIP: I think
    private static String typeExpression(String s) throws Exception {
        Pattern[] patterns = getPatterns();
        Pattern iExpr = patterns[0];
        Pattern bExpr = patterns[1];
        Pattern var = patterns[12];
        Matcher m = bExpr.matcher(s);
        if (m.find()) {
            return bExpr(bExpr, s);
        } m = iExpr.matcher(s);
        if (m.find()) {
            return iExpr(s);
        } m = var.matcher(s);
        if (m.find()) {
            return primitive(s);
        }
        throw new Exception();
    }

    // DONE:
    // TODO: I believe this is done
    private static String expression(String s) throws Exception {
        if (s.contains("\0") && !s.equals("")) {
            String[] temp = s.split("\0");
            String res = "";
            for (String str : temp) {
                if (str.equals("")) {
                    continue;
                }
                res += expression(str.replaceAll("\0", ""));
            }
            return res;
        }
        Pattern[] patterns = getPatterns();
        Pattern vAssn = patterns[2];
        Pattern conditional = patterns[4];
        Pattern print = patterns[5];
        // Pattern wLoop = patterns[6];
        Pattern fiLoop = patterns[9];
        Pattern conElse = patterns[13];
        Matcher m = fiLoop.matcher(s);
        if (m.find()) {
            return fori(fiLoop, s);
        } m = vAssn.matcher(s);
        if (m.find()) {
            return vAssn(vAssn, s);
        } m = conditional.matcher(s);
        if (m.find()) {
            return conditional(conditional, s);
        } m = conElse.matcher(s);
        if (m.find()) {
            return conditional(conElse, s);
        } m = print.matcher(s);
        if (m.find()) {
            return print(print, s);
        } // m = wLoop.matcher(s);
        // if (m.find()) {
        //     return wLoop(wLoop, s);
        // // } 
        // if (s.strip().equals("}")) {
        //     return "}";
        // }
        return primitive(s);
    }

    public static void main(String[] args) throws Exception {
        FileParser fp = new FileParser();
        ArrayList<String> lines = fp.getLines(args);
        Pattern[] patterns = getPatterns();
        variables = new HashMap<String, String>();
        String expressionType;
        for (String s : lines) {
            // expressionType checks for the first word in the line
            // to get the expression type
            expressionType = s.strip().split(" ")[0];
            if (expressionType.equals("fori")) {
                System.out.println(fori(patterns[9], s));
            }
            // else if (expressionType.equals("while")) {
            //     System.out.println(wLoop(patterns[6], s));
            // }
            else if (expressionType.equals("if")) {
                if (charCount(s, '{') != charCount(s, '}')) {
                    throw new Exception();
                }
                System.out.println(conditional(patterns[4], s));
            }
            else if (expressionType.equals("elif")) {
                if (charCount(s, '{') != charCount(s, '}')) {
                    throw new Exception();
                }
                System.out.println(conditional(patterns[4], s));
            }
            else if (expressionType.equals("else")) {
                if (charCount(s, '{') != charCount(s, '}')) {
                    throw new Exception();
                }
                System.out.println(conditional(patterns[13], s));
            }
            else if (expressionType.contains(">{")) {
                System.out.println(print(patterns[5], s));
            }
            // if the line did not contain a key word first then
            // it is evaluated as either a getArg() call or a variable assignment
            else {
                if (s.contains("getArg") && !s.contains("\"")) {
                    // TODO: change s to m.group(1)
                    System.out.println(getArg(args, patterns[10], s));
                }
                else {
                    System.out.println(vAssn(patterns[2], s));
                }
            }
        }
    }
}