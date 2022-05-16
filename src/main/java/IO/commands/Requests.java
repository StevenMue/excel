package IO.commands;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Requests {
    EXIT("(exit|Exit|EXIT)"),
    EVALUATE("(evaluate|Evaluate|EVALUATE)"),
    FORMULAR(".+=(.|^=)+"),
    READ("(read|Read|READ)[(table|Table|TABLE)]?"),
    WRITE("(write|Write|WRITE)[(table|Table|TABLE)]?"),
    CSV("(csv|Csv|CSV)[(table|Table|TABLE)]?");
    final String regex;

    Requests(String regex){
        this.regex=regex;
    }

    public boolean matches(String string) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
    public static Optional<String> findMatch(String string){
        for (Requests value : Requests.values()) {
            if(value.matches(string)){
                if(value==FORMULAR){
                    return Optional.of(string);
                }else {
                    return Optional.of(value.toString());
                }
            }
        }
        return Optional.empty();
    }
}
