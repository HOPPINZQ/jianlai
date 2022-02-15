package com.test;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

public class WordUtil {

    private WordUtil(){}

    /**
     * 跨行合并
     * @param table
     * @param col    合并的列
     * @param fromRow    起始行
     * @param toRow    终止行
     * @Description
     * @Author Huangxiaocong 2018年11月26日 下午9:09:19
     */
    public static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for(int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            //第一个合并单元格用重启合并值设置
            if(rowIndex == fromRow) {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                //合并第一个单元格的单元被设置为“继续”
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 跨列合并
     * @param xwpfTableRow
     * @param row    所合并的行
     * @param fromCell    起始列
     * @param toCell    终止列
     * @Description
     * @Author Huangxiaocong 2018年11月26日 下午9:23:22
     */
    public static void mergeCellsHorizontal(XWPFTableRow xwpfTableRow, int row, int fromCell, int toCell) {
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cellMerge = xwpfTableRow.getCell(cellIndex);
            if ( cellIndex == row ) {
                cellMerge.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                cellMerge.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 换行
     * @param document
     */
    public static void addBlack(XWPFDocument document){
        XWPFParagraph paragraph1 = document.createParagraph();
        XWPFRun paragraphRun1 = paragraph1.createRun();
        paragraphRun1.setText("\r");
    }


    /**
     * 设置段落,有背景
     * @param document
     * @param text
     * @param color
     * @param size
     * @param fillColor
     * @param paragraphAlignment
     */
    public static void setLineAndBackground(XWPFDocument document,String text,String color,int size,String fillColor,ParagraphAlignment paragraphAlignment){
        XWPFParagraph firstParagraph = document.createParagraph();
        firstParagraph.setAlignment(paragraphAlignment);
        XWPFRun run = firstParagraph.createRun();
        run.setText(text);
        run.setColor(color);
        run.setFontSize(size);
        CTShd cTShd2 = run.getCTR().addNewRPr().addNewShd();
        cTShd2.setVal(STShd.CLEAR);
        cTShd2.setFill(fillColor);
    }

    /**
     * 设置段落
     * @param document
     * @param text
     * @param color
     * @param size
     */
    public static void setLine(XWPFDocument document,String text,String color,int size){
        setLineAndBackground(document,text,color,size,"ffffff",ParagraphAlignment.LEFT);
    }

    /**
     * 设置标题并居中
     * @param document
     * @param text
     * @param color
     * @param size
     */
    public static void setTitle(XWPFDocument document,String text,String color,int size){
        setLineAndBackground(document,text,color,size,"ffffff",ParagraphAlignment.CENTER);
    }


}
