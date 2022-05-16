package IO;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class is used for writing or reading XSSF data.<br>
 * It can create or read a {@link core.Excel3000} table.<br>
 * The file name and save location are fix.
 */
public class ExcelReaderWriter {


    private static final String FILE_NAME = "./Excel3000.xlsx";
    private static final String FILE_NAME_WRITE = "./Excel3000Write.xlsx";


    XSSFWorkbook workbook;

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }

    /**
     * Create a {@link XSSFWorkbook workbook} with one {@link XSSFSheet sheet} with a given table.
     * @param table required a {@link com.google.common.collect.Table Table}< Integer, Integer, String >
     * @return this {@link ExcelReaderWriter} object
     */
    public ExcelReaderWriter createXSSFWorkbook(Table<Integer,Integer,String> table){
        workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Excel3000_Table");
        table.rowMap().forEach(((r, integerStringMap) -> {
            Row row = sheet.createRow(r);
         integerStringMap.forEach((c, value) -> {
           Cell cell = row.createCell(c);
           cell.setCellValue(value);
         });
        }));
        return this;
    }

    /**
     * Write a given {@link XSSFWorkbook workbook} in a file.
     * @param workbook
     * @return this {@link ExcelReaderWriter} object
     */
    public ExcelReaderWriter write(XSSFWorkbook workbook){
        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
        }catch (FileNotFoundException exception){
            exception.printStackTrace();
        }catch (IOException exception){
            exception.printStackTrace();
        }
        return this;
    }

    /**
     * Write the {@link XSSFWorkbook workbook} from the object in a file.
     * @return this {@link ExcelReaderWriter} object
     */
    public ExcelReaderWriter write(){
        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME_WRITE);
            workbook.write(outputStream);
        }catch (FileNotFoundException exception){
            exception.printStackTrace();
        }catch (IOException exception){
            exception.printStackTrace();
        }finally {
            return this;
        }
    }

    private FileInputStream read(){
        try {
            FileInputStream inputStream = new FileInputStream(FILE_NAME);
            return inputStream;
        }catch (FileNotFoundException exception){
            exception.printStackTrace();
        }catch (IOException exception){
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Read the first {@link XSSFSheet sheet} from this object {@link XSSFWorkbook workbook}.<br>
     * Create and save the first {@link XSSFSheet sheet} in a {@link Table table}< Integer, Integer, String >.<br>
     * It can only be saved {@link CellType} NUMERIC, FORMULAR, STRING.<br> Others will be saved as empty.
     * @return {@link Table }< Integer, Integer, String >
     */
    public Table<Integer,Integer,String> createTable(){
        try {
            workbook = new XSSFWorkbook(read());
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = workbook.getSheetAt(0);
        Table<Integer,Integer,String> table = HashBasedTable.create();
        sheet.forEach(row->{
            row.forEach(cell -> {
                switch (cell.getCellType()){
                    case NUMERIC -> table.put(row.getRowNum(),cell.getColumnIndex(),Double.toString(cell.getNumericCellValue()));
                    case FORMULA -> table.put(row.getRowNum(),cell.getColumnIndex(),cell.getCellFormula());
                    case STRING -> table.put(row.getRowNum(),cell.getColumnIndex(),cell.getStringCellValue());
                    case ERROR -> table.put(row.getRowNum(),cell.getColumnIndex(),"");
                    case BOOLEAN -> table.put(row.getRowNum(),cell.getColumnIndex(), "");
                    case BLANK -> table.put(row.getRowNum(),cell.getColumnIndex(),"");
                    default -> table.put(row.getRowNum(),cell.getColumnIndex(), "");
                }
            });
        });
        return table;
    }
}
