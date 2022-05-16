package IO;



import core.Excel3000;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

public class CSVWriter extends ExcelReaderWriter{


    public CSVWriter excelToCSV() throws IOException {
            String FILE_NAME_WRITE = "./Excel3000.csv";
            CSVPrinter output = new CSVPrinter(new FileWriter(FILE_NAME_WRITE), CSVFormat.DEFAULT);

            String tsv = new XSSFExcelExtractor(workbook).getText();
            BufferedReader reader = new BufferedReader(new StringReader(tsv));
            reader.lines().map(line -> line.split("\t")).forEach(values -> {
                try {
                    output.printRecord(values);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            output.close();
            return this;
    }
}
