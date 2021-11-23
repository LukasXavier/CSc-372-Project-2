package extra;
import java.util.regex.Pattern;

public enum patternsEnums {
    IXPR(Pattern.compile("(\\+|\\-|\\*|%|/|[0-9])")),
    FORI(Pattern.compile("fori[\\s]*\\(([a-zA-Z]{1}[\\w]*)[\\s]*([+-[*]/])[\\s]*([\\w]+),[\\s]*([\\w]+),[\\s]*([\\w]+)\\)[\\s]*\\{(.+)\\}"));

    private Pattern p;

    patternsEnums(Pattern p) {
        this.p = p;
    }

    public Pattern getPattern() {
        return p;
    }
}
