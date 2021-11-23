import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    
    public static void main(String[] args) {
        Patterns ps = new Patterns();
        Pattern p = ps.print;
        String s = ">>{'test'}";
        Matcher m = p.matcher(s);
        if (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                System.out.println(m.group(i));
            }
        }
        
    }
}
