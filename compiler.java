import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class compiler {

    private static int argc = 0;

    private static void test(Pattern pattern, String test) {
        Matcher match = pattern.matcher(test);
        if (match.find()) {
            System.out.println(match.group());
        }
        else {
            System.out.println("No Match Found");
        }
    }

    private static ArrayList<String> cleanLines(ArrayList<String> lines) {
        int curlyBraceCounter = 0;
        boolean curlyBrace = false;
        ArrayList<String> temp = new ArrayList<String>();
        String compressed = "";
        for (String line : lines) {
            if (line.contains("{")) {
                if (line.contains("}")) {
                    if (curlyBrace) {
                        compressed += line + '\0';
                    } else {
                        temp.add(line);
                    }
                    continue;
                }
                curlyBraceCounter++;
                curlyBrace = true;
                compressed += line;
                continue;
            }
            if (line.contains("}")) {
                curlyBraceCounter--;
                if (curlyBraceCounter == 0) {
                    curlyBrace = false;
                    temp.add(compressed + line);
                    compressed = "";
                } else { 
                    compressed += line + '\0';
                }
                continue;
            }
            if (curlyBraceCounter == 0) {
                temp.add(line);
            }
            if (curlyBrace) {
                compressed += line;
            }
        }
        return temp;
    }

    private static ArrayList<String> getLines(String[] args) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader file = new BufferedReader(new FileReader(args[0])); 
            String line = file.readLine();
            int commentCounter = 0;
            boolean multiLineComment = false;

            while (line != null) {
                if (line.length() < 2) {
                    if (line.length() == 1) { lines.add(line); }
                    line = file.readLine();
                    continue;
                }
                if (line.substring(0,2).equals("/*")) {
                    multiLineComment = true;
                    commentCounter++;
                }
                else if (multiLineComment) {
                    if (commentCounter == 0) {
                        multiLineComment = false;
                        lines.add(line);
                    }
                    else if (line.substring(0, 2).equals("*/")) {
                        commentCounter--;
                    }
                } else {
                    lines.add(line);
                }
                line = file.readLine();
            }
            file.close();
            lines = cleanLines(lines);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return lines;
    }

    private static Pattern[] getPatterns() {
        // ^[\\w]+([\\s][+-[*]/%][\\s][\\w]+)+$

        // (left) (op) (right [recursive])
        // 5 + 4 - 3
        Pattern iExpr = Pattern.compile("(\\+|\\-|\\*|%|/)");
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
        Pattern conditional = Pattern.compile("\\s*if\\s*(\\((.+?)(?:\\)))");

        // Pattern comments = Pattern.compile("(//.+)|(/[*].+[*]/)");

        // >{(content)} | >>{(content)}
        // >{"hello world"} >>{var}
        Pattern print = Pattern.compile("^(>\\{(.+)\\}|>>\\{(.+)\\})$");

        // while (condition) {(do)}
        // while (i < 10) {i++}
        Pattern wLoop = Pattern.compile("^while[\\s]*\\((.+)\\)[\\s]*\\{(.+)\\}$");

        // fori ((incrementingVariable) (op) (amount), (starting inclusive), (end inclusive)) {(do)}
        // fori (i - 2, 100, 0) {>{i}}
        Pattern fiLoop = Pattern.compile("^fori[\\s]*\\(([a-zA-Z]{1}[\\w]*)[\\s]*([+-[*]/])[\\s]*([\\w]+),[\\s]*([\\w]+),[\\s]*([\\w]+)\\)[\\s]*\\{(.+)\\}$");
        Pattern args = Pattern.compile("^([a-zA-Z]{1}[\\w]*)[\\s]*=[\\s]*getArg\\(\\)$");
        Pattern ints = Pattern.compile("[0-9]+");
        Pattern chars = Pattern.compile("\'[a-zA-Z]\'");
        Pattern bool = Pattern.compile("true|false");
        return new Pattern[] {iExpr, bExpr, vAssn, sLit, conditional, print, wLoop, ints, chars, fiLoop, args, bool};
    }

    public static void test1() {
        // Pattern[] patterns = getPatterns();
        // test(patterns[0], "3 + 6 * 1");
        // test(patterns[0], "1 + 2-1");
        // test(patterns[1], "true == false & true | false");
        // test(patterns[1], "5 - 6");
        // test(patterns[3], "\"true = false\"");
        // test(patterns[3], "5 | 6");
        // test(patterns[2], "thing = 3");
        // test(patterns[2], "th*ng = rdd");
        // test(patterns[8], "34");
        // test(patterns[8], "d");
        // test(patterns[9], "'s'");
        // test(patterns[9], "a");
        // test(patterns[10], "true");
        // test(patterns[10], "a");
        // test(patterns[4], "if (3 + 2 = 5) {>{\"yee\"}}");
        // test(patterns[4], "if (3 + 2) {>{\"yee\"");
        // test(patterns[6], ">{\"yee\"}");
        // test(patterns[6], ">>{\"yee\"}");
        // test(patterns[6], ">>{\"yee\"}r");
        // test(patterns[7], "while(gamer) {fdsfds}");
        // test(patterns[7], "while    (gamer) {fdsfds}");
        // test(patterns[7], "while    (gamer) {}");
    }

    private static void validateConditional(String s) {
        if (s.contains("if")) {

        }
    }

    private static String getArg(String[] args, Pattern p, String s) throws Exception {
        argc++;
        Matcher m = p.matcher(s);
        if (m.find()) {
            if (args.length <= argc) {
                return m.group(1) + " = " + "None";
            }
           return m.group(1) + " = " + args[argc];
        }
        throw new Exception();
    }

    private static String fori(Pattern p, String s) throws Exception {
        Matcher m = p.matcher(s);
        if (m.find()) {
            String res = "";
            res += "for " + m.group(1) + " in range(" + m.group(4) + "," + m.group(5) + "," 
                + m.group(3) + "):\n\t";
            for (String e: m.group(6).split("" + '\0')) {
                res += expression(e);
            }
            return res;
        }
        throw new Exception();
    }

    // parsed = [before operator, operator, after operator]
    private static String iExpr(Pattern p, String s) throws Exception {
        String[] parsed = getIExpr(s);
        if (parsed.length == 1) {
            return parsed[0];
        }
        else {
            return primitive(parsed[0]) + " " + parsed[1] + " " + iExpr(p, parsed[2]);
        }
    }

    // parsed = [before operator, operator, after operator]
    private static String bExpr(Pattern p, String s) throws Exception {
        String[] parsed = getBExpr(s);
        if (parsed.length == 1) {
            return parsed[0];
        }
        else {
            return expression(parsed[0]) + " " + parsed[1] + " " + bExpr(p, parsed[2]);
        }
    }

    private static String primitive(String s) throws Exception {
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
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        } m = sLit.matcher(s);
        if (m.find()) {
            return s;
        }
        throw new Exception();
    }

    private static String[] parseExpr(String s, char operator, int i) {
        String[] parsed = new String[3];
        parsed[0] = s.substring(0, i);
        parsed[1] = Character.toString(operator);
        parsed[2] = s.substring(i+1, s.length());
        return parsed;
    }

    private static String[] getIExpr(String s) throws Exception {
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '+') {
                return parseExpr(s, '+', i);
            }
            else if (chars[i] == '-') {
                return parseExpr(s, '-', i);
            }
            else if (chars[i] == '*') {
                return parseExpr(s, '*', i);
            }
            else if (chars[i] == '/') {
                return parseExpr(s, '/', i);
            }
            else if (chars[i] == '%') {
                return parseExpr(s, '%', i);
            }
        }
        return new String[] {primitive(s)};
    }

    private static String[] getBExpr(String s) throws Exception {
        char[] chars = s.toCharArray();
        int equalCount = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '&') {
                return parseExpr(s, '&', i);
            }
            else if (chars[i] == '|') {
                return parseExpr(s, '|', i);
            }
            else if (chars[i] == '<') {
                return parseExpr(s, '<', i);
            }
            else if (chars[i] == '>') {
                return parseExpr(s, '>', i);
            }
            else if (chars[i] == '!') {
                return parseExpr(s, '!', i);
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
    
    private static String vAssn(Pattern p, String s) throws Exception {
        String res = "";
        Matcher m = p.matcher(s);
        if (m.find()) {
            res += varType(m.group(2)) + m.group(1) + " = " + primitive(m.group(2));
        }
        return res;
    }

    private static String varType(String s) throws Exception {
        Pattern[] patterns = getPatterns();
        if (patterns[3].matcher(s).find()) {
            return "";
        }
        else if (patterns[8].matcher(s).find()) {
            return "";
        }
        else if (patterns[11].matcher(s).find()) {
            return "";
        }
        else if (patterns[7].matcher(s).find()) {
            return "";
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
        String res = "";
        Matcher m = p.matcher(s);
        Pattern[] patterns = getPatterns();
        String[] split = new String[2];
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '{') {
                split[0] = s.substring(0, i);
                split[1] = s.substring(i+1, s.length());
                break;
            }
        }
        System.out.println(s);
        if (split.length < 2 || charCount(s, '{') != charCount(s, '}')) {
            System.out.println(charCount(s, '{'));
            System.out.println(charCount(s, '}'));
            throw new Exception();
        }
        System.out.println(split[0] + "|||" + split[1]);
        if (m.find()) {
            // if (bExpr) {(expression)}
            res += " if (" + bExpr(patterns[1], m.group(2))  + "):\n\t\t" + expression(split[1].replaceAll("}", ""));
        } 
        return res;
    }
    
    private static String print(Pattern p, String s) throws Exception {
        Matcher m = p.matcher(s);
        if (m.find()) {
            return "print(" + m.group(2) + ")";
        }
        throw new Exception();
    }
    // TODO:
    private static String wLoop(Pattern p, String s) {
        String res = "";
        Matcher m = p.matcher(s);
        if (m.find()) {
            res += "";
        }
        return res;
    }
    // TODO:
    private static String fiLoop(Pattern p, String s) {
        String res = "";
        Matcher m = p.matcher(s);
        if (m.find()) {
            res += "";
        }
        return res;
    }

    // TODO: I believe this is done
    private static String expression(String s) throws Exception {
        String res = "";
        Pattern[] patterns = getPatterns();
        Pattern iExpr = patterns[0];
        Pattern bExpr = patterns[1];
        Pattern vAssn = patterns[2];
        Pattern conditional = patterns[4];
        Pattern print = patterns[5];
        Pattern wLoop = patterns[6];
        Pattern fiLoop = patterns[9];
        Matcher m = conditional.matcher(s);
        if (m.find()) {
            return res += conditional(conditional, s);
        } m = bExpr.matcher(s);
        if (m.find()) {
            return res += bExpr(bExpr, s);
        } m = iExpr.matcher(s);
        if (m.find()) {
            return res += iExpr(iExpr, s);
        } m = vAssn.matcher(s);
        if (m.find()) {
            return res += vAssn(vAssn, s);
        }  m = print.matcher(s);
        if (m.find()) {
            return res += print(print, s);
        } m = wLoop.matcher(s);
        if (m.find()) {
            return res += wLoop(wLoop, s);
        } m = fiLoop.matcher(s);
        if (m.find()) {
            return res += fiLoop(fiLoop, s);
        }
        throw new Exception();
    }

    public static void main(String[] args) throws Exception {
        ArrayList<String> lines = getLines(args);
        Pattern[] patterns = getPatterns();
        String expressionType;
        for (String s : lines) {
            // expressionType checks for the first word in the line
            // to get the expression type
            expressionType = s.strip().split(" ")[0];
            if (expressionType.equals("fori")) {
                System.out.println(fori(patterns[9], s));
            }
            else if (expressionType.equals("while")) {
                System.out.println(wLoop(patterns[6], s));
            }
            else if (expressionType.equals("if")) {
                System.out.println(conditional(patterns[4], s));
            }
            else if (expressionType.contains(">{")) {
                System.out.println(print(patterns[5], s));
            }
            // if the line did not contain a key word first then
            // it is evaluated as either a getArg() call or a variable assignment
            else {
                if (s.contains("getArg") && !s.contains("\"")) {
                    System.out.println(getArg(args, patterns[10], s));
                }
                else {
                    System.out.println(vAssn(patterns[2], s));
                }
            }
        }
    }
}