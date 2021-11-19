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
        Pattern iExpr = Pattern.compile("^([\\w]+)[\\s]*([+-[*]/%])[\\s]*(.+)$");
        // ^([\\w]+)([\\s](\\||&|<|>|!|==)[\\s]([\\w]+))+$

        // (left) (op) (right [recursive])
        // true == true
        Pattern bExpr = Pattern.compile("^([\\w]+)[\\s]*(\\||&|<|>|!|==)[\\s]*(.+)+$");
        // ^([a-zA-Z]{1}[\\w]*)[\\s](=)[\\s]([\\w]+)$

        // (variableName) = (value)
        // stringVar = "stringLiteral"
        Pattern vAssn = Pattern.compile("^([a-zA-Z]{1}[\\w]*)[\\s]*[=][\\s]*(.+)$");

        // ("*")
        // "stringLiteral"
        Pattern sLit = Pattern.compile("^([\"][^\"]*[\"])$");
        // if[\\s]\\((.+)\\)[\\s]\\{(.+)\\}

        // if (condition) {(do)}
        // if (5 < var) { >{"you lost"}}
        Pattern conditional = Pattern.compile("^[\\s]*if[\\s]*\\((.+)\\)[\\s]*\\{(.+)\\}$");

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

    private static String getArg(String[] args, Pattern p, String s) throws Exception {
        argc++;
        Matcher m = p.matcher(s);
        if (m.find()) {
            if (args.length == argc) {
                System.err.println("Error: index out of bounds in arguments, " + argc + " out of " + args.length);
                System.exit(0);
            }
           return "int " + m.group(1) + " = " + Integer.parseInt(args[argc]) + ";";
        }
        throw new Exception();
    }

    private static String fori(Pattern p, String s) throws Exception {
        Matcher m = p.matcher(s);
        if (m.find()) {
            String res = "";
            res += "for (int " + m.group(1) + " = " + m.group(3) + "; " + m.group(1) + "< " + m.group(5);
            res += "; " + m.group(1) + m.group(2) + m.group(2) + ") {";
            for (String e: m.group(6).split("" + '\0')) {
                res += expression(e);
            }
            return res;
        }
        throw new Exception();
    }

    // TODO: not done
    private static String iExpr(Pattern p, String s) {
        String res = "";
        Matcher m = p.matcher(s);
        if (m.find()) {
            // (leftside) (op) (rightside)
            res += m.group(1) + " " + m.group(2) + " " + iExpr(p, m.group(3));
        } else {
            m = getPatterns()[7].matcher(s);
            if (m.find()) {
                res += iExpr(getPatterns()[7], s);
            }
        }
        return res;
    }

    // TODO:
    private static String bExpr(Pattern p, String s) {
        String res = "";
        Matcher m = p.matcher(s);
        if (m.find()) {
            res += "";
        }
        return res;
    }
    // TODO:
    private static String vAssn(Pattern p, String s) {
        String res = "";
        Matcher m = p.matcher(s);
        if (m.find()) {
            res += "";
        }
        return res;
    }
    // TODO:
    private static String conditional(Pattern p, String s) {
        String res = "";
        Matcher m = p.matcher(s);
        if (m.find()) {
            res += "";
        }
        return res;
    }
    // TODO:
    private static String print(Pattern p, String s) {
        String res = "";
        Matcher m = p.matcher(s);
        if (m.find()) {
            res += "";
        }
        return res;
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
    private static String expression(String s) {
        String res = "";
        Pattern[] patterns = getPatterns();
        Pattern iExpr = patterns[0];
        Pattern bExpr = patterns[1];
        Pattern vAssn = patterns[2];
        Pattern conditional = patterns[4];
        Pattern print = patterns[5];
        Pattern wLoop = patterns[6];
        Pattern fiLoop = patterns[9];
        Matcher m = iExpr.matcher(s);
        if (m.find()) {
            return res += iExpr(iExpr, s);
        } m = bExpr.matcher(s);
        if (m.find()) {
            return res += bExpr(bExpr, s);
        } m = vAssn.matcher(s);
        if (m.find()) {
            return res += vAssn(vAssn, s);
        } m = conditional.matcher(s);
        if (m.find()) {
            return res += conditional(conditional, s);
        } m = print.matcher(s);
        if (m.find()) {
            return res += print(print, s);
        } m = wLoop.matcher(s);
        if (m.find()) {
            return res += wLoop(wLoop, s);
        } m = fiLoop.matcher(s);
        if (m.find()) {
            return res += fiLoop(fiLoop, s);
        }
        return res;
    }

    public static void main(String[] args) throws Exception {
        ArrayList<String> lines = getLines(args);
        Pattern[] patterns = getPatterns();
        for (String s : lines) {
            // TODO: finish implementing the different outer most expression calls
            if (s.contains("getArg")) {
                System.out.println(getArg(args, patterns[10], s));
            }
            else if (s.contains("fori")) {
                System.out.println(fori(patterns[9], s));
            }
            else if (s.contains("while")) {
                // TODO:
            }
            else if (s.contains("if")) {
                // TODO:
            }
        }
    }
}