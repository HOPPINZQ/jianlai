//package com.test
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;
//import org.apache.poi.xwpf.usermodel.XWPFTable;
//import org.apache.poi.xwpf.usermodel.XWPFTableCell;
//import org.apache.poi.xwpf.usermodel.XWPFTableRow;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
//
///**
// * @Description 导出word文档
// * @Author  Huangxiaocong
// * 2018年12月1日  下午12:12:15
// */
//public class ExportWord {
//    private XWPFHelperTable xwpfHelperTable = null;
//    private XWPFHelper xwpfHelper = null;
//    public ExportWord() {
//        xwpfHelperTable = new XWPFHelperTable();
//        xwpfHelper = new XWPFHelper();
//    }
//    /**
//     * 创建好文档的基本 标题，表格  段落等部分
//     * @return
//     * @Author Huangxiaocong 2018年12月16日
//     */
//    public XWPFDocument createXWPFDocument() {
//        XWPFDocument doc = new XWPFDocument();
//        createTitleParagraph(doc);
//        createLineParagraph(doc);
//        createTableParagraph(doc, 10, 6);
//        return doc;
//    }
//    /**
//     * 创建标题的标题样式
//     * @param document
//     * @Author Huangxiaocong 2018年12月16日 下午5:28:38
//     */
//    public void createTitleParagraph(XWPFDocument document) {
//        XWPFParagraph titleParagraph = document.createParagraph();    //新建一个标题段落对象（就是一段文字）
//        titleParagraph.setAlignment(ParagraphAlignment.CENTER);//样式居中
//        XWPFRun titleFun = titleParagraph.createRun();    //创建文本对象
////        titleFun.setText(titleName); //设置标题的名字
//        titleFun.setBold(true); //加粗
//        titleFun.setColor("000000");//设置颜色
//        titleFun.setFontSize(20);    //字体大小
////        titleFun.setFontFamily("");//设置字体
//    }
//
//    /**
//     *
//     * @param document
//     */
//    public void createLineParagraph(XWPFDocument document) {
//        XWPFParagraph lineParagraph = document.createParagraph();    //新建一个标题段落对象（就是一段文字）
//        lineParagraph.setAlignment(ParagraphAlignment.LEFT);//样式居中
//        XWPFRun titleFun = lineParagraph.createRun();    //创建文本对象
//        titleFun.setBold(true); //加粗
//        titleFun.setColor("000000");//设置颜色
//        titleFun.setFontSize(14);    //字体大小
//    }
//
//    /**
//     * 设置表格
//     * @param document
//     * @param rows
//     * @param cols
//     * @Author Huangxiaocong 2018年12月16日
//     */
//    public void createTableParagraph(XWPFDocument document, int rows, int cols) {
////        xwpfHelperTable.createTable(xdoc, rowSize, cellSize, isSetColWidth, colWidths)
//        XWPFTable infoTable = document.createTable(rows, cols);
//        xwpfHelperTable.setTableWidthAndHAlign(infoTable, "9072", STJc.CENTER);
//        //合并表格
//        xwpfHelperTable.mergeCellsHorizontal(infoTable, 1, 1, 5);
//        xwpfHelperTable.mergeCellsVertically(infoTable, 0, 3, 6);
//        for(int col = 3; col < 7; col++) {
//            xwpfHelperTable.mergeCellsHorizontal(infoTable, col, 1, 5);
//        }
//        //设置表格样式
//        List<XWPFTableRow> rowList = infoTable.getRows();
//        for(int i = 0; i < rowList.size(); i++) {
//            XWPFTableRow infoTableRow = rowList.get(i);
//            List<XWPFTableCell> cellList = infoTableRow.getTableCells();
//            for(int j = 0; j < cellList.size(); j++) {
//                XWPFParagraph cellParagraph = cellList.get(j).getParagraphArray(0);
//                cellParagraph.setAlignment(ParagraphAlignment.CENTER);
//                XWPFRun cellParagraphRun = cellParagraph.createRun();
//                cellParagraphRun.setFontSize(12);
//                if(i % 2 != 0) {
//                    cellParagraphRun.setBold(true);
//                }
//            }
//        }
//        xwpfHelperTable.setTableHeight(infoTable, 560, STVerticalJc.CENTER);
//    }
//
//    /**
//     * 往表格中填充数据
//     * @param dataList
//     * @param document
//     * @throws IOException
//     * @Author Huangxiaocong 2018年12月16日
//     */
//    @SuppressWarnings("unchecked")
//    public void exportCheckWord(Map<String, Object> dataList, XWPFDocument document, String savePath) throws IOException {
//        //标题
//        XWPFParagraph paragraph = document.getParagraphArray(0);
//        XWPFRun titleFun = paragraph.getRuns().get(0);
//        titleFun.setText(String.valueOf(dataList.get("TITLE")));
//        //段落1
//        XWPFParagraph firstParagraph = document.createParagraph();
//        firstParagraph.setAlignment(ParagraphAlignment.LEFT);//设置段落内容靠右
//        firstParagraph.setIndentationLeft(200);//缩进
//        XWPFRun run = firstParagraph.createRun();
//        run.setText(String.valueOf(dataList.get("text1")));
//        run.setBold(true); //加粗
//        run.setFontSize(12);//字体大小
//        run.setFontFamily("华文中宋");
//        run.addBreak();//添加一个回车空行
//
//        //表格
//        List<List<Object>> tableData1 = (List<List<Object>>) dataList.get("table1");
//        XWPFTable table1 = document.getTableArray(0);
//
//
//        //段落2
//        run.setText(String.valueOf(dataList.get("text2")));
//        fillTableData(table1, tableData1);
//
//
//
//
//
////        List<List<Object>> tableData2 = (List<List<Object>>) dataList.get("table2");
////        XWPFTable table2 = document.getTableArray(0);
////        fillTableData(table2, tableData2);
//
//        xwpfHelper.saveDocument(document, savePath);
//    }
//    /**
//     * 往表格中填充数据
//     * @param table
//     * @param tableData
//     * @Author Huangxiaocong 2018年12月16日
//     */
//    public void fillTableData(XWPFTable table, List<List<Object>> tableData) {
//        List<XWPFTableRow> rowList = table.getRows();
//        for(int i = 0; i < tableData.size(); i++) {
//            List<Object> list = tableData.get(i);
//            List<XWPFTableCell> cellList = rowList.get(i).getTableCells();
//            for(int j = 0; j < list.size(); j++) {
//                XWPFParagraph cellParagraph = cellList.get(j).getParagraphArray(0);
//                XWPFRun cellParagraphRun = cellParagraph.getRuns().get(0);
//                cellParagraphRun.setText(String.valueOf(list.get(j)));
//            }
//        }
//    }
//}
