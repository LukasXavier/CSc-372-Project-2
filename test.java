import java.util.regex.Matcher;

import exceptions.*;

public class test {

    public static void main(String[] args) throws IntExprException {
        Patterns p = new Patterns();
        String s = "if (x < 0) { fori(y + 1, 1, 20) { >{y}}}";
        Matcher m = p.forI.matcher(s);
        if (m.find()) {
            System.out.println(m.group());
        }
        try {
            method1();
        } catch (CompileException e) {
            System.out.println(e.getMessage());
        }
        
    }

    public static void method1() throws IntExprException {
        method2();
    }

    public static void method2() throws IntExprException {
        throw new IntExprException("bruh");
    }
}
