import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class compiler {
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
        
        Pattern intExpression = Pattern.compile("^([a-zA-Z]{1}[\\w]*)[\\s]([+-[*]/%])[\\s]([\\w]+$)");
        Pattern boolExpression = Pattern.compile("^([\\w]+)[\\s]([\\|&<>=!])[\\s]([\\w]+$)");
        
        Pattern ints = Pattern.compile("[0-9]*");
        String intExpTest1 = "gr3ger + rr";
        String intExpTest2 = "d + 24";
        String boolExpTest1 = "true = false";
        String boolExpTest2 = "5 | 6";
        Matcher m1 = intExpression.matcher(intExpTest1);
        Matcher m2 = intExpression.matcher(intExpTest2);
        Matcher m3 = boolExpression.matcher(boolExpTest1);
        Matcher m4 = boolExpression.matcher(boolExpTest2);
        if (m1.find()) {
            System.out.println(m1.group());
        }
        else {
            System.out.println("No Match Found");
        }
        if (m2.find()) {
            System.out.println(m2.group(0));
        }
        else {
            System.out.println("No Match Found");
        }
        if (m3.find()) {
            System.out.println(m3.group());
        }
        else {
            System.out.println("No Match Found");
        }
        if (m4.find()) {
            System.out.println(m4.group(0));
        }
        else {
            System.out.println("No Match Found");
        }
    }
}