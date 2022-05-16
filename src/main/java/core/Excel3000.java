package core;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import core.exceptions.UnknownFormatException;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Excel3000 {
    Table<Integer /*row*/, Integer /*col*/, String /*val*/> table;

    public Excel3000() {
        table = HashBasedTable.create();
    }

    public Table<Integer, Integer, String> getTable() {
        return table;
    }

    /**
     * @param table <br>required a {@link Table}< Integer, Integer, String >
     */
    public void setTable(Table<Integer, Integer, String> table) {
        this.table = table;
    }

    /**
     * @param cell    String <br> need to be in form of [A-Z]+[0-9]+ e.g. B1
     * @param content String <br> to be saved in that cell
     */
    public void setCell(String cell /*cell: String, Integer*/, String content) {
        int[] position = Cell.getInt(cell);
        table.put(position[0], position[1], content);
    }

    public String getCellAt(int row, int col) {
        return table.get(row, col);
    }

    public String getCellAt(String cell) {
        int[] position = Cell.getInt(cell);
        return table.get(position[0], position[1]);
    }

    /**
     * Evaluate the whole table.<br>
     * If an error occurs the table will be left unchanged.
     *
     * @return the updated object with a new table
     */
    public Excel3000 evaluate() {
        Table<Integer /*row*/, Integer /*col*/, String /*val*/> newTable = HashBasedTable.create();
        table.rowMap().forEach((rowKey, row) -> {
            row.forEach((colKey, value) -> {
                try {
                    value = unPackValue(value);
                } catch (UnknownFormatException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    return;
                }
                try {
                    String result = evaluate(value);
                    newTable.put(rowKey, colKey, result);
                } catch (UnknownFunctionOrVariableException e) {
                    System.err.println("Cell Error in [" + rowKey + ":" + colKey + "] = " + e.getMessage() + "\n" + "table will not change");
                }

            });
        });
        this.table = newTable;
        return this;
    }

    private String evaluate(String string) throws UnknownFunctionOrVariableException {
        Expression expression = new ExpressionBuilder(string.toLowerCase(Locale.ROOT)).build();
        return Double.toString(expression.evaluate());
    }

    /**
     * Replace all cell references with there value.
     *
     * @param string String<br>
     *               value of a cell.
     * @throws UnknownFormatException <br>
     *                                is thrown when a reference lead to an empty cell
     */
    private String unPackValue(String string) throws UnknownFormatException {
        Optional<List<String>> variablesOptional = getVariable(string);
        String unPackedString = string.replace("=", "");
        if (variablesOptional.isPresent()) {
            for (int i = 0; i < variablesOptional.get().size(); i++) {
                String variable = variablesOptional.get().get(i);
                int[] pos = Cell.getInt(variable);
                if (getCellAt(pos[0], pos[1]) != null) {
                    unPackedString = unPackedString.replace("$" + variable, getCellAt(pos[0], pos[1]));
                } else {
                    throw new UnknownFormatException("Reference to a empty cell");
                }
            }
            if (unPackedString.contains("$")) {
                return unPackValue(unPackedString);
            } else {
                return unPackedString;
            }
        }
        return string;
    }

    /**
     * Take a cell value and extract the cell references.<br>
     * A cell reference must be of [A-Z]+[0-9]+
     *
     * @param string String<br>
     *               value of a cell.
     * @return Optional<List < String>> containing a list with all found references
     */
    private Optional<List<String>> getVariable(String string) {
        String varRegex = "\\$[A-Z]+\\d+";
        Pattern pattern = Pattern.compile(varRegex);
        Matcher matcher = pattern.matcher(string);
        List list = new ArrayList();
        if (matcher.find()) {
            matcher.reset();
            while (matcher.find()) {
                list.add(matcher.group().substring(1));
            }
            return Optional.of(list);
        }
        return Optional.empty();
    }
}
