package agh.genetyczne;

import lombok.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class DataToExcelWriter {
    private String filename;
    private double[][] targets;
    private List<String> results;

    public DataToExcelWriter()
    {
        results = new ArrayList<>();
    }

    void prepareGoodResult()
    {
        for (int i=0;i<results.size();i++)
        {
            String value = results.get(i);
            //value = value.replace('.', ',');
            value = replaceX(value);
            results.set(i, value);
        }
    }

    String replaceX(String v)
    {
        for(int i=0;i<24;i++)
        {
            char newChar = (char)(i+65);
            String oldChar = "x" + (i + 1);
            v = v.replace(oldChar, String.valueOf(newChar) + "x");
        }
        return v;
    }

    String replaceY(String v, int row)
    {
        for(int i=0;i<24;i++)
        {
            char oldChar = (char)(i+65);
            String newChar = String.valueOf(oldChar) + row;
            v = v.replace(String.valueOf(oldChar) + "x", newChar);
        }
        return v;
    }

    void copyTargets(double[][] targets) {
        this.targets = targets;
    }

    void addNewResult(String result) {
        results.add(result);
    }

    @SneakyThrows
    void writeToExcel() {
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

        prepareGoodResult();
        int startColumnIndex = parametersCount + 1;
        CellStyle cellStyleResult = workbook.createCellStyle();
        cellStyleResult.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        cellStyleResult.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        for (short c = 0; c < results.size(); c++)
        {
            int columnIndex = startColumnIndex + c;
            XSSFRow row = sheet.getRow(0);
            XSSFCell cell = row.createCell(columnIndex);
            cell.setCellValue("Result " + c);
            cell.setCellStyle(cellStyleResult);
            for(int i = 1 ; i <= rowsCount ; i++)
            {
                XSSFRow rowDetail = sheet.getRow(i);
                String formula = replaceY(results.get(c), i + 1);
                rowDetail.createCell(columnIndex, CellType.FORMULA).setCellFormula(formula);
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