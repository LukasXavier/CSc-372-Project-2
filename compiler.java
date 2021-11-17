import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class compiler {

    private static void test(Pattern pattern, String test) {
        Matcher match = pattern.matcher(test);
        if (match.find()) {
            System.out.println(match.group());
        }
        else {
            System.out.println("No Match Found");
        }
    }

    public static void main(String[] args) {

        // ArrayList<String> lines = new ArrayList<String>();
        // try {
        //     BufferedReader file = new BufferedReader(new FileReader(args[0])); 
        //     String line = file.readLine();
        //     while (line != null) {
        //         lines.add(line);
        //         line = file.readLine();
        //     }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        
        Pattern intExpression = Pattern.compile("^[\\w]+([\\s][+-[*]/%][\\s][\\w]+)+$");
        Pattern boolExpression = Pattern.compile("^([\\w]+)([\\s](\\||&|<|>|!|==)[\\s]([\\w]+))+$");
        Pattern varAssignment = Pattern.compile("^([\\w]+)[\\s](=)[\\s]([\\w]+)$");
        Pattern stringLiteral = Pattern.compile("^([\"][^\"]*[\"])$");
        Pattern conditional = Pattern.compile("if \\((.+)\\) \\{(.+)\\}");
        Pattern comments = Pattern.compile("(//.+)|(/[*].+[*]/)");
        Pattern print = Pattern.compile("^(>\\{(.+)\\}|>>\\{(.+)\\})$");
        Pattern loop = Pattern.compile("^(while[\\s]*\\((.+)\\)[\\s]*\\{(.+)\\})$");
        Pattern ints = Pattern.compile("[0-9]+");
        Pattern chars = Pattern.compile("\'[a-zA-Z]\'");
        Pattern bool = Pattern.compile("true|false");
        test(intExpression, "3 + 6 * 1");
        test(intExpression, "1 + 2-1");
        test(boolExpression, "true == false & true | false");
        test(boolExpression, "5 - 6");
        test(stringLiteral, "\"true = false\"");
        test(stringLiteral, "5 | 6");
        test(varAssignment, "thing = 3");
        test(varAssignment, "th*ng = rdd");
        test(ints, "34");
        test(ints, "d");
        test(chars, "'s'");
        test(chars, "a");
        test(bool, "true");
        test(bool, "a");
        test(conditional, "if (3 + 2 = 5) {>{\"yee\"}}");
        test(conditional, "if (3 + 2) {>{\"yee\"");
        test(print, ">{\"yee\"}");
        test(print, ">>{\"yee\"}");
        test(print, ">>{\"yee\"}r");
        test(loop, "while(gamer) {fdsfds}");
        test(loop, "while    (gamer) {fdsfds}");
        test(loop, "while    (gamer) {}");
        test(comments, "// yee");
        test(comments, "/* fdgdfdffgsd */");
    }
}