import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class compiler {
    public static void main(String[] args) {
        Pattern p1 = Pattern.compile(".*");
        String s1 = "test";
        Matcher m1 = p1.matcher(s1);
        if (m1.find()) {
            System.out.println(m1.group(0));
        }
    }
}