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

    /**
     * Read the next line from the InputStream
     * @return Optional < String[2] > <br>
     * if the scanner hase no next line it will return a empty optional. <br>
     * if the line caintains a "=" <br>
     * it will return a optional with 2 strings split between the "="<br>
     * else the optional will have the input in the first index and the second is ""
     */
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
                        return optionalStrings;
                }

                return Optional.of(new String[]{optional.get(), ""});

            }else {
                error("The input hase a wrong format.");
            }
        }
        return Optional.empty();
    }

    /**
     * Will split the input string in two strings.<br>
     * The "=" is the position where it will split.
     * @param string
     * @return Optional< String[2] > <br>
     * The first position in the array is the left string of the "=" the second position will be the right string.<br>
     * if there is no "=" it will return a Optional <br>
     * with the input string on the first postition and the second will be "".<br>
     * if there are more than one "=" it will return a empty optional.
     */
    private Optional<String[]> splitInput(String string) {
        if (string.contains("=")) {
            if (string.chars().filter(c -> c == '=').count() == 1) {
                String[] split = string.split("\s*=\s*");
                String regexVarName = "^\\$[A-Z]+\\d+";
                if (Pattern.compile(regexVarName).matcher(split[0]).matches()) {
                    return Optional.of(split);
                }
            }
            error("More than one '='");
            return Optional.empty();
        }
        return Optional.of(new String[]{string, ""});
    }

    public void error(String msg){
        System.err.println(msg);
    }
}
