package core;


/**
 * This class provides the functions to parse the variables names to integer positions
 */
public class Cell {
    static final String REGEX_ALPHA ="[A-Z]+";
    static final String REGEX_DIGIT ="[0-9]+";

    /**
     * @param cell the cell name in form of [A-Z]+[0-9]+
     * @return the column position of this cell as String [A-Z]+
     */
    public static String getStringCol(String cell){
        return cell.replaceAll(REGEX_DIGIT,"");
    }

    /**
     * @param cell the cell name in form of [A-Z]+[0-9]+
     * @return the column position of this cell as integer
     */
    public static int getIntCol(String cell){
        return getInt(cell)[0];
    }

    /**
     * @param cell the cell name in form of [A-Z]+[0-9]+
     * @return the row position of this cell as integer
     */
    public static int getIntRow(String cell){

        return Integer.parseInt(cell.replaceAll(REGEX_ALPHA,""));
    }

    /**
     * @param cell the cell name in form of [A-Z]+[0-9]+
     * @return the position int[col,row] in the table
     */
    public static int[] getInt(String cell){

        int[] result = new int[2];
        String cellWithoutNr = cell.replaceAll(REGEX_DIGIT,"");
        StringBuilder sb = new StringBuilder();
        for(int i =0;i<cellWithoutNr.length();i++){
            sb.append((int)cellWithoutNr.charAt(i)-65);
        }
        result[1]=Integer.parseInt(sb.toString());
        result[0]=Integer.parseInt(cell.replaceAll(REGEX_ALPHA,""))-1;
        return result;
    }

}
