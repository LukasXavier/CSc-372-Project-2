/**
 * @author Eric Mendoza (ericmendoza@email.arizona.edu)
 * @author Luke Ramirez (lucasxavier@email.arizona.edu)
 * <p>File: Patterns.java
 * <p>Assignment: Project 2
 * <p>Course: CSc 372, Fall 21
 * 
 * <p>Purpose: This class contains all the regex Patterns
 */

 import java.util.regex.Pattern;

public class Patterns {
    // [varName]: any alphanumeric string that starts with a letter

    // integer Expression
    // [anything] [+|-|*|/|%] [anything]
    public Pattern iExpr = Pattern.compile("^([\\w]+)[\\s]*((?:\\+|\\-|\\*|%|/?))[\\s]*(.+)*$");

    // integer incrementor
    // [anything][++|--] | [anything][+=|-=|*=|/=]
    public Pattern iIncrementor = Pattern.compile("^(.+)(\\+{2}|-{2})$|^(.+)[\\s]*(\\+=|\\*=|/=|-=)[\\s]*(.+)$");

    // boolean Expression
    // [anything] [&|<|>|!|||==] [anything]
    public Pattern bExpr = Pattern.compile("^(.+)((?:\\||&|<|>|!|={2}?))(.+)$");

    // variable Assignment
    // [varName] = [anything]
    public Pattern vAssn = Pattern.compile("^[\\s]*([a-zA-Z]{1}[\\w]*)[\\s]*[=][\\s]*(.+)$");

    // string Literal
    // "[anything except a double quote]"
    public Pattern sLit = Pattern.compile("^([\"][^\"]*[\"])$");
    
    // conditional
    // [if|else] ([anything]) {[anything]}
    public Pattern conditional = Pattern.compile("^[\\s]*((?:if|elif?))[\\s]+\\((.+?)(?:\\))[\\s]*\\{{1}(.*)\\}{1}$");
    
    // conditional else
    // [else] {[anything]}
    public Pattern conElse = Pattern.compile("^[\\s]*(?:else?)[\\s]+\\{{1}(.*)\\}{1}$");

    // print
    // [>|>>]{[anything]}
    public Pattern print = Pattern.compile("^[\\s]*((?:>|>>?))\\{(.*)\\}[\\s]*$");

    // while Loop (deprecated/not used)
    // while ([anything]) {[anything]}
    public Pattern wLoop = Pattern.compile("^while[\\s]*\\((.+)\\)[\\s]*\\{(.+)\\}$");

    // for i Loop
    // fori ([varName] [+|-|*|/] [anything], [anything], [anything]) {[anything])}
    public Pattern forI = Pattern.compile("fori[\\s]+\\(([a-zA-Z]{1}[\\w]*)[\\s]*(\\+|-|\\*|/|)[\\s]*([\\w]+),[\\s]*([\\w]+),[\\s]*([\\w]+)\\)[\\s]*\\{(.+)\\}");
    
    // get Arguments
    // [varName] = getArg()
    public Pattern args = Pattern.compile("^([a-zA-Z]{1}[\\w]*)[\\s]*=[\\s]*getArg\\(\\)$");
    
    // integers
    // [number]
    public Pattern ints = Pattern.compile("^[\\s]*[0-9]+[\\s]*$");

    // characters
    // '[char]'
    public Pattern chars = Pattern.compile("^\'[^\']{1}\'$");

    // booleans
    // [true|false]
    public Pattern bool = Pattern.compile("true|false");

    public Patterns() {}
}