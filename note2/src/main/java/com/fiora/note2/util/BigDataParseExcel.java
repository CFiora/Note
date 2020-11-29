package com.fiora.note2.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.annotation.PreDestroy;

/**
 * XSSF and SAX (Event API)
 */
@NoArgsConstructor
public abstract class BigDataParseExcel extends DefaultHandler {
    private SharedStringsTable sst;
    private String lastContents;
    private boolean nextIsString;
    private int sheetIndex = -1;
    private List<String> rowlist = new ArrayList<String>();
    private int curRow = 0; //当前行
    private int curCol = 0; //当前列索引
    private int preCol = 0; //上一列列索引
    private int titleRow = 2; //标题行，一般情况下为0
    private int rowsize = 0; //列数
    @Setter @Getter
    private Connection con;
    @Setter @Getter
    private PreparedStatement pst;
    @Setter @Getter
    private int count = 0;
    private static String sql = "insert into net_disk_temp(`code`,`name`,`path`,`size`,`time`,`md5`,`directory`) values(?,?,?,?,?,?,'0')";
    public BigDataParseExcel (Connection con) throws SQLException {
        this.con = con;
        this.con.setAutoCommit(false);
        this.setPst(con.prepareStatement(sql));
    }

    @PreDestroy
    public void destroy() throws SQLException {
        this.pst.executeBatch();
        this.con.commit();
        this.pst.clearBatch();
    }

    //excel记录行操作方法，以sheet索引，行索引和行元素列表为参数，对sheet的一行元素进行操作，元素为String类型
    public abstract void optRows(int sheetIndex, int curRow, List<String> rowlist) throws SQLException;
    //只遍历一个sheet，其中sheetId为要遍历的sheet索引，从1开始，1-3

    /**
     * @param filename
     * @param sheetId  sheetId为要遍历的sheet索引，从1开始，1-3
     * @throws Exception
     */
    public void processOneSheet(String filename, int sheetId) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        // rId2 found by processing the Workbook
        // 根据 rId# 或 rSheet# 查找sheet
        InputStream sheet2 = r.getSheet("rId" + sheetId);
        sheetIndex++;
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
    }

    /**
     * 遍历 excel 文件
     */
    public void process(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        Iterator<InputStream> sheets = r.getSheetsData();
        while (sheets.hasNext()) {
            curRow = 0;
            sheetIndex++;
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader();
        //.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        // c => 单元格
        if (name.equals("c")) {
            // 如果下一个元素是 SST 的索引，则将nextIsString标记为true
            String cellType = attributes.getValue("t");
            String rowStr = attributes.getValue("r");
            curCol = this.getRowIndex(rowStr);
            if (cellType != null && cellType.equals("s")) {
                nextIsString = true;
            } else {
                nextIsString = false;
            }
        }

        // 置空
        lastContents = "";
    }

    public void endElement(String uri, String localName, String name)
            throws SAXException {
        // 根据SST的索引值的到单元格的真正要存储的字符串
        // 这时characters()方法可能会被调用多次
        if (nextIsString) {
            try {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx))
                        .toString();
            } catch (Exception e) {
            }
        }

        // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
        // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
        if (name.equals("v")) {
            String value = lastContents.trim();
            value = value.equals("") ? " " : value;
            int cols = curCol - preCol;
            if (cols > 1) {
                for (int i = 0; i < cols - 1; i++) {
                    rowlist.add(preCol, "");
                }
            }
            preCol = curCol;
            rowlist.add(curCol - 1, value);
        } else {
            //如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
            if (name.equals("row")) {
                int tmpCols = rowlist.size();
                if (curRow > this.titleRow && tmpCols < this.rowsize) {
                    for (int i = 0; i < this.rowsize - tmpCols; i++) {
                        rowlist.add(rowlist.size(), "");
                    }
                }
                try {
                    optRows(sheetIndex, curRow, rowlist);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (curRow == this.titleRow) {
                    this.rowsize = rowlist.size();
                }
                rowlist.clear();
                curRow++;
                curCol = 0;
                preCol = 0;
            }
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        //得到单元格内容的值
        lastContents += new String(ch, start, length);
    }

    //得到列索引，每一列c元素的r属性构成为字母加数字的形式，字母组合为列索引，数字组合为行索引，
    //如AB45,表示为第（A-A+1）*26+（B-A+1）*26列，45行
    public int getRowIndex(String rowStr) {
        rowStr = rowStr.replaceAll("[^A-Z]", "");
        byte[] rowAbc = rowStr.getBytes();
        int len = rowAbc.length;
        float num = 0;
        for (int i = 0; i < len; i++) {
            num += (rowAbc[i] - 'A' + 1) * Math.pow(26, len - i - 1);
        }
        return (int) num;
    }

    public int getTitleRow() {
        return titleRow;
    }

    public void setTitleRow(int titleRow) {
        this.titleRow = titleRow;
    }

    //如何使用
    public static void main(String[] args) throws Exception {
        String Url = "jdbc:mysql://localhost:3306/mysql?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";//参数参考MySql连接数据库常用参数及代码示例
        String name = "root";//数据库用户名
        String psd = "root";//数据库密码
        String jdbcName = "com.mysql.cj.jdbc.Driver";//连接MySql数据库
        int batchSize = 5000;
        Class.forName(jdbcName);//向DriverManager注册自己
        Connection con = DriverManager.getConnection(Url, name, psd);//与数据库建立连接
        BigDataParseExcel xlx = new BigDataParseExcel(con){
            @Override
            public void optRows(int sheetIndex, int curRow, List<String> rowlist) throws SQLException {
                this.setCount(this.getCount()+1);
                if (this.getCount() <= 2){
                    System.out.println("第" + (this.getCount()) + "行记录跳过");
                } else {
                    System.out.println("第" + (this.getCount()) + "行记录在工作");
                    this.getPst().setString(1, rowlist.get(0));
                    this.getPst().setString(2, rowlist.get(1));
                    this.getPst().setString(3, rowlist.get(2));
                    this.getPst().setString(4, rowlist.get(3));
                    this.getPst().setString(5, rowlist.get(4));
                    this.getPst().setString(6, rowlist.get(5));
                    this.getPst().addBatch();
                    if (this.getCount() % batchSize == 0) {//可以设置不同的大小；如50，100，500，1000等等
                        this.getPst().executeBatch();
                        this.getCon().commit();
                        this.getPst().clearBatch();
                    }
                }
            }
        };
        String filePathPrefix = "D:\\1500T\\";
//        String[] files = {"2018更新资源0.xlsx","2019更新资源0.xlsx","2020更新资源0.xlsx","KTV0.xlsx","百家0.xlsx","电视剧0.xlsx",
//            "电影0.xlsx","动漫0.xlsx","高清经典0.xlsx","更新资源0.xlsx","纪录片0.xlsx",
//            "教育0.xlsx","蓝光专区0.xlsx","体育0.xlsx","图书0.xlsx","音乐0.xlsx",
//            "游戏IT0.xlsx","中美百万0.xlsx","综艺0.xlsx"};
        String file = "图书0.xlsx";
        xlx.process(filePathPrefix + file);
        xlx.destroy();
        con.close();
    }
}
