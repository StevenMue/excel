import IO.*;
import core.Excel3000;

import java.util.Optional;

public class Application {
    public static void main(String[] args) {
        Excel3000 excel3000= new Excel3000();
        IO io = new IO();
        Optional<String[]> optional;
        ExcelReaderWriter excelWriter = new ExcelReaderWriter();
        while (true) {
            optional =  io.getNextLine();
            if (optional.isPresent()) {
                switch (optional.get()[0]) {
                    case "EXIT" -> {
                        return;
                    }
                    case "WRITE" -> {
                        excelWriter.createXSSFWorkbook(excel3000.getTable()).write();
                    }
                    case "READ" -> {
                        excel3000.setTable(excelWriter.createTable());
                    }
                    case "EVALUATE" -> {
                        excel3000.evaluate();
                    }
                    default -> {
                        excel3000.setCell(optional.get()[0].replace("$", ""), optional.get()[1]);
                    }
                }
            }
        }
    }
}
