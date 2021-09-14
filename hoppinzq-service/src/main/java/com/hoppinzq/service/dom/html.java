package com.hoppinzq.service.dom;

import java.util.ArrayList;
import java.util.List;

import static com.hoppinzq.service.dom.BlockDom.TD;
import static com.hoppinzq.service.dom.BlockDom.H1;

/**
 * @author:ZhangQi
 **/
public class html {

    public static void main(String[] args) {
        Td td1 = DomFactory.createDom(TD);
        td1.setTagTitle("th");
        td1.addValue("服务");
        td1.addStyle(Style.BACKGROUND,"red");
        String[] domClass={"qwe","dsadas"};
        td1.setDomClass(domClass);
        td1.setDomId("td1");
        td1.setDomIsHidden(true);
        td1.setDomTitle("lalala");
        List<DomData> domData=new ArrayList<>();
        DomData domData1=new DomData("id","111");
        DomData domData2=new DomData("yes","qwe");
        domData.add(domData1);
        domData.add(domData2);
        td1.setDomDataList(domData);
        Td td2 = DomFactory.createDom(TD);
        td2.addValue("方法");
        td2.setTagTitle("th");
        Tr tr1=new Tr(td1,td2);
        Thead thead=new Thead(tr1);
        Td td3=new Td("service1");
        Td td4=new Td("method1");
        Tr tr2=new Tr(td3,td4);
        Td td5=new Td("service2");
        Td td6=new Td("method2");
        Tr tr3=new Tr(td5,td6);
        Tbody tbody=new Tbody(tr2,tr3);
        Table table=new Table(thead,tbody);
        table.addStyle(Style.BORDER,"1px solid #000");
        String[] f={"11.2px","33.2px","-1px","3px"};
        table.setMargin(f);
        table.setDomId("table1");
        H1 h1 = DomFactory.createDom(H1);
        h1.addValue("qeqweqweqweeqweqwe");
        h1.setNum(2);
        h1.setDomId("wqe");
        System.err.println(table.toFormatHtml());
        System.err.println(h1.toFormatHtml());
    }
}
