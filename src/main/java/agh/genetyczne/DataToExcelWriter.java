package agh.genetyczne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataToExcelWriter {
    private String filename;
    private double[][] targets;

    void copyTargets(double[][] targets) {
        this.targets = targets;
    }

    void writeToExcel() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Summary");

        XSSFRow rowHead = sheet.createRow((short) 0);
        int parametersCount = targets[0].length - 1;
        for (int i = 0; i < parametersCount; i++) {
            rowHead.createCell(i).setCellValue("X" + (i + 1));
        }
        rowHead.createCell(parametersCount).setCellValue("Expected");
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        rowHead.cellIterator().forEachRemaining(cell -> cell.setCellStyle(cellStyle));

        int rowsCount = targets.length;
        for (short r = 0; r < rowsCount; r++) {
            XSSFRow row = sheet.createRow(r+1);
            for (short i = 0; i <= parametersCount; i++) {
                row.createCell(i).setCellValue(targets[r][i]);
            }
        }

        filename = "results.xlsx";
        FileOutputStream fileOut = new FileOutputStream(filename);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        System.out.println("Your excel file has been generated!");
    }
}