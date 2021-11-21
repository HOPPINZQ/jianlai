package com.hoppinzq.test.poi;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelTest {

    public static void main(String[] args) throws IOException {
        File file = new File("D://222.xlsx");
        FileOutputStream outputStream = new FileOutputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet excelSheet = workbook.createSheet("excel");

        //demo 单独下拉列表
        addValidationToSheet(workbook, excelSheet, new String[]{"百度", "阿里巴巴"}, 'C', 1, 200);

        //demo 级联下拉列表
        Map<String, List<String>> data = new HashMap<>();
        data.put("百度系列", Arrays.asList("百度地图", "百度知道", "百度音乐"));
        data.put("阿里系列", Arrays.asList("淘宝", "支付宝", "钉钉"));
        addValidationToSheet(workbook, excelSheet, data, 'A', 'B', 1, 200);

        //demo 自动填充
        Map<String, String> kvs = new HashMap<>();
        kvs.put("百度", "www.baidu.com");
        kvs.put("阿里", "www.taobao.com");
        addAutoMatchValidationToSheet(workbook, excelSheet, kvs, 'D', 'E', 1, 200);

        // 隐藏存储下拉列表数据的sheet；可以注释掉该行以便查看、理解存储格式
        //hideTempDataSheet(workbook, 1);

        workbook.write(outputStream);
        outputStream.close();
    }

    /**
     * 给sheet页，添加下拉列表
     *
     * @param workbook    excel文件，用于添加Name
     * @param targetSheet 级联列表所在sheet页
     * @param options     级联数据 ['百度','阿里巴巴']
     * @param column      下拉列表所在列 从'A'开始
     * @param fromRow     下拉限制开始行
     * @param endRow      下拉限制结束行
     */
    public static void addValidationToSheet(Workbook workbook, Sheet targetSheet, Object[] options, char column, int fromRow, int endRow) {
        String hiddenSheetName = "sheet" + workbook.getNumberOfSheets();
        Sheet optionsSheet = workbook.createSheet(hiddenSheetName);
        String nameName = column + "_parent";

        int rowIndex = 0;
        for (Object option : options) {
            int columnIndex = 0;
            Row row = optionsSheet.createRow(rowIndex++);
            Cell cell = row.createCell(columnIndex++);
            cell.setCellValue(option.toString());
        }

        createName(workbook, nameName, hiddenSheetName + "!$A$1:$A$" + options.length);

        DVConstraint constraint = DVConstraint.createFormulaListConstraint(nameName);
        CellRangeAddressList regions = new CellRangeAddressList(fromRow, endRow, (int) column - 'A', (int) column - 'A');
        targetSheet.addValidationData(new HSSFDataValidation(regions, constraint));
    }

    /**
     * 给sheet页  添加级联下拉列表
     *
     * @param workbook    excel
     * @param targetSheet sheet页
     * @param options     要添加的下拉列表内容  ， keys 是下拉列表1中的内容，每个Map.Entry.Value 是对应的级联下拉列表内容
     * @param keyColumn   下拉列表1位置
     * @param valueColumn 级联下拉列表位置
     * @param fromRow     级联限制开始行
     * @param endRow      级联限制结束行
     */
    public static void addValidationToSheet(Workbook workbook, Sheet targetSheet, Map<String, List<String>> options, char keyColumn, char valueColumn, int fromRow, int endRow) {
        String hiddenSheetName = "sheet" + workbook.getNumberOfSheets();
        Sheet hiddenSheet = workbook.createSheet(hiddenSheetName);
        List<String> firstLevelItems = new ArrayList<>();

        int rowIndex = 0;
        for (Map.Entry<String, List<String>> entry : options.entrySet()) {
            String parent = formatNameName(entry.getKey());
            firstLevelItems.add(parent);
            List<String> children = entry.getValue();

            int columnIndex = 0;
            Row row = hiddenSheet.createRow(rowIndex++);
            Cell cell = null;

            for (String child : children) {
                cell = row.createCell(columnIndex++);
                cell.setCellValue(child);
            }

            char lastChildrenColumn = (char) ((int) 'A' + children.size() - 1);
            createName(workbook, parent, String.format(hiddenSheetName + "!$A$%s:$%s$%s", rowIndex, lastChildrenColumn, rowIndex));

            DVConstraint constraint = DVConstraint.createFormulaListConstraint("INDIRECT($" + keyColumn + "1)");
            CellRangeAddressList regions = new CellRangeAddressList(fromRow, endRow, valueColumn - 'A', valueColumn - 'A');
            targetSheet.addValidationData(new HSSFDataValidation(regions, constraint));
        }

        addValidationToSheet(workbook, targetSheet, firstLevelItems.toArray(), keyColumn, fromRow, endRow);

    }

    /**
     * 根据用户在keyColumn选择的key, 自动填充value到valueColumn
     *
     * @param workbook    excel
     * @param targetSheet sheet页
     * @param keyValues   匹配关系 {'百度','www.baidu.com'},{'淘宝','www.taobao.com'}
     * @param keyColumn   要匹配的列（例如 网站中文名称）
     * @param valueColumn 匹配到的内容列（例如 网址）
     * @param fromRow     下拉限制开始行
     * @param endRow      下拉限制结束行
     */
    public static void addAutoMatchValidationToSheet(Workbook workbook, Sheet targetSheet, Map<String, String> keyValues, char keyColumn, char valueColumn, int fromRow, int endRow) {
        String hiddenSheetName = "sheet" + workbook.getNumberOfSheets();
        Sheet hiddenSheet = workbook.createSheet(hiddenSheetName);

        // init the search region(A and B columns in hiddenSheet)
        int rowIndex = 0;
        for (Map.Entry<String, String> kv : keyValues.entrySet()) {
            Row totalSheetRow = hiddenSheet.createRow(rowIndex++);

            Cell cell = totalSheetRow.createCell(0);
            cell.setCellValue(kv.getKey());

            cell = totalSheetRow.createCell(1);
            cell.setCellValue(kv.getValue());
        }

        for (int i = fromRow; i <= endRow; i++) {
            Row totalSheetRow = targetSheet.getRow(i);
            if (totalSheetRow == null) {
                totalSheetRow = targetSheet.createRow(i);
            }

            Cell cell = totalSheetRow.getCell((int) valueColumn - 'A');
            if (cell == null) {
                cell = totalSheetRow.createCell((int) valueColumn - 'A');
            }

            String keyCell = String.valueOf(keyColumn) + (i + 1);
            String formula = String.format("IF(ISNA(VLOOKUP(%s,%s!A:B,2,0)),\"\",VLOOKUP(%s,%s!A:B,2,0))", keyCell, hiddenSheetName, keyCell, hiddenSheetName);

            cell.setCellFormula(formula);
        }

        // init the keyColumn as comboList
        addValidationToSheet(workbook, targetSheet, keyValues.keySet().toArray(), keyColumn, fromRow, endRow);
    }

    private static Name createName(Workbook workbook, String nameName, String formula) {
        Name name = workbook.createName();
        name.setNameName(nameName);
        name.setRefersToFormula(formula);
        return name;
    }

    /**
     * 隐藏excel中的sheet页
     *
     * @param workbook
     * @param start    需要隐藏的 sheet开始索引
     */
    private static void hideTempDataSheet(HSSFWorkbook workbook, int start) {
        for (int i = start; i < workbook.getNumberOfSheets(); i++) {
            workbook.setSheetHidden(i, true);
        }
    }

    /**
     * 不可数字开头
     *
     * @param name
     * @return
     */
    static String formatNameName(String name) {
        name = name.replaceAll(" ", "").replaceAll("-", "_").replaceAll(":", ".");
        if (Character.isDigit(name.charAt(0))) {
            name = "_" + name;
        }

        return name;
    }

}

