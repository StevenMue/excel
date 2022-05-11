package IO;

import IO.commands.Requests;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;


public class IO {
    Scanner scanner;

    public IO() {
        this.scanner = new Scanner(System.in);
    }

    public IO(Scanner scanner) {
        this.scanner = scanner;
    }

    public Optional<String[]> getNextLine() {
        if (scanner.hasNextLine()) {
            String string = scanner.nextLine();
            if (string.isEmpty()) {
                return getNextLine();
            }

            Optional<String> optional = Requests.findMatch(string);
            if (optional.isPresent()) {
                if (optional.get().equals(string)) {
                    Optional<String[]> optionalStrings = splitInput(string);
                    if (optionalStrings.isPresent()) {
                        return optionalStrings;
                    }
                }
                return Optional.of(new String[]{optional.get(), ""});
            }
        }
        return Optional.empty();
    }

    private Optional<String[]> splitInput(String string) {
        if (string.contains("=")) {
            if (string.chars().filter(c -> c == '=').count() == 1) {
                String[] split = string.split("\s*=\s*");
                String regexVarName = "^\\$[A-Z]+\\d+";
                if (Pattern.compile(regexVarName).matcher(split[0]).matches()) {
                    return Optional.of(split);
                }
            }
            return Optional.empty();
        }
        return Optional.of(new String[]{string, ""});
    }
}
