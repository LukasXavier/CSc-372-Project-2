import java.util.regex.Pattern;

public class Patterns {
    public Pattern iExpr = Pattern.compile("^([\\w]+)[\\s]*((?:\\+|\\-|\\*|%|/?))[\\s]*(.+)*$");
    public Pattern iIncrementor = Pattern.compile("^(.+)(\\+{2}|-{2})$|^(.+)[\\s]*(\\+=|\\*=|/=|-=)[\\s]*(.+)$");
    public Pattern bExpr = Pattern.compile("^(.+)((?:\\||&|<|>|!|={2}?))(.+)$");
    public Pattern vAssn = Pattern.compile("^[\\s]*([a-zA-Z]{1}[\\w]*)[\\s]*[=][\\s]*(.+)$");
    public Pattern sLit = Pattern.compile("^([\"][^\"]*[\"])$");
    public Pattern conditional = Pattern.compile("^[\\s]*((?:if|elif?))[\\s]+\\((.+?)(?:\\))[\\s]*\\{{1}(.*)\\}{1}$");
    public Pattern conElse = Pattern.compile("^[\\s]*(?:else?)[\\s]+\\{{1}(.*)\\}{1}$");
    public Pattern print = Pattern.compile("^[\\s]*((?:>|>>?))\\{(.*)\\}[\\s]*$");
    public Pattern wLoop = Pattern.compile("^while[\\s]*\\((.+)\\)[\\s]*\\{(.+)\\}$");
    public Pattern forI = Pattern.compile("fori[\\s]+\\(([a-zA-Z]{1}[\\w]*)[\\s]*(\\+|-|\\*|/|)[\\s]*([\\w]+),[\\s]*([\\w]+),[\\s]*([\\w]+)\\)[\\s]*\\{(.+)\\}");
    public Pattern args = Pattern.compile("^([a-zA-Z]{1}[\\w]*)[\\s]*=[\\s]*getArg\\(\\)$");
    public Pattern ints = Pattern.compile("^[\\s]*[0-9]+[\\s]*$");
    public Pattern chars = Pattern.compile("^\'[a-zA-Z]{1}\'$");
    public Pattern bool = Pattern.compile("true|false");

    public Patterns() {}
}