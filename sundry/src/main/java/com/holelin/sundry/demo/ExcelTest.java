package com.holelin.sundry.demo;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelTest {
    public static void main(String[] args) throws IOException {

        List<LinkedHashMap<String, Object>> dataList = new ArrayList<>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("name", "鸡蛋");
        map.put("code", "001");
        map.put("price", "3.94");
        dataList.add(map);
        LinkedHashMap<String, Object> map1 = new LinkedHashMap<>();
        map1.put("name", "鸡肉");
        map1.put("code", "101");
        map1.put("price", "19.94");
        dataList.add(map1);
        LinkedHashMap<String, Object> map2 = new LinkedHashMap<>();
        map2.put("name", "方便面");
        map2.put("code", "123001");
        map2.put("price", "1.00");
        dataList.add(map2);
        LinkedHashMap<String, Object> map3 = new LinkedHashMap<>();
        map3.put("name", "五花肉");
        map3.put("code", "666");
        map3.put("price", "10.00");
        dataList.add(map3);

        String name = "测试数据2";
        ExportExcelForXlsx(dataList, name,DEVICE_TYPE_PROJECT);
        addExcelForXlsx(dataList, name);
        readExcelForXlsx(name);

    }

    public static final String DEVICE_TYPE_PROJECT = "Project";


    /**
     * 导出excel
     *
     * @param dataList 数据
     * @param name     文件名称
     */
    public static void ExportExcelForXls(List<LinkedHashMap<String, Object>> dataList, String name) {
        LinkedHashMap<String, Object> titleList = dataList.get(0);
        // 创建HSSFWorkbook对象
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建HSSFSheet对象
        HSSFSheet sheet = wb.createSheet(name);

        // -----------------------------数据填充 ---------------------------------
        // 设置标题
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(name);
        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, titleList.size() - 1));
        // 设置列名称
        HSSFRow cellTitleRow = sheet.createRow(1);
        AtomicInteger jt = new AtomicInteger();
        LinkedHashMap<String, Object> tem = dataList.get(0);
        tem.forEach((k, v) -> {
            HSSFCell cell = cellTitleRow.createCell(jt.get());
            cell.setCellValue(k);
            jt.getAndIncrement();
        });
        // 填充sql结果
        for (int i = 0; i < dataList.size(); i++) {
            HSSFRow row = sheet.createRow(i + 2);
            AtomicInteger j = new AtomicInteger();
            LinkedHashMap<String, Object> ltem = dataList.get(i);
            ltem.forEach((k, v) -> {
                HSSFCell cell = row.createCell(j.get());
                cell.setCellValue(v.toString());
                j.getAndIncrement();
            });
        }
        // -----------------------------------------------------------------------
        // 输出Excel文件
        try {
            FileOutputStream output = new FileOutputStream("d:\\" + name + ".xls");
            wb.write(output);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void ExportExcelForXlsx(List<LinkedHashMap<String, Object>> dataList, String fileName,String sheetName) {
        LinkedHashMap<String, Object> titleList = dataList.get(0);
        // 创建HSSFWorkbook对象
        XSSFWorkbook wb = new XSSFWorkbook();
        // 创建HSSFSheet对象
        XSSFSheet sheet = wb.createSheet(sheetName);

        // -----------------------------数据填充 ---------------------------------
        // 设置标题
        XSSFRow titleRow = sheet.createRow(0);
        XSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(fileName);
        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, titleList.size() - 1));
        // 设置列名称
        XSSFRow cellTitleRow = sheet.createRow(1);
        AtomicInteger jt = new AtomicInteger();
        LinkedHashMap<String, Object> tem = dataList.get(0);
        tem.forEach((k, v) -> {
            XSSFCell cell = cellTitleRow.createCell(jt.get());
            cell.setCellValue(k);
            jt.getAndIncrement();
        });
        // 填充sql结果
        for (int i = 0; i < dataList.size(); i++) {
            XSSFRow row = sheet.createRow(i + 2);
            AtomicInteger j = new AtomicInteger();
            LinkedHashMap<String, Object> ltem = dataList.get(i);
            ltem.forEach((k, v) -> {
                XSSFCell cell = row.createCell(j.get());
                cell.setCellValue(v.toString());
                j.getAndIncrement();
            });
        }
        // -----------------------------------------------------------------------
        // 输出Excel文件
        try {
            FileOutputStream output = new FileOutputStream("d:\\" + fileName + ".xlsx");
            wb.write(output);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 追加到已有excel
     *
     * @param dataList 数据
     * @param name     文件名
     */

    public static void addExcelForXls(List<LinkedHashMap<String, Object>> dataList, String name) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("d://" + name + ".xls");  //获取d://test.xls,建立数据的输入通道
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fileInputStream);  //使用POI提供的方法得到excel的信息
        HSSFWorkbook Workbook = new HSSFWorkbook(poifsFileSystem);//得到// 文档对象
        HSSFSheet sheet = Workbook.getSheet(name);  //根据name获取sheet表
        HSSFRow row = sheet.getRow(0);  //获取第一行
        System.out.println("最后一行的行号 :" + sheet.getLastRowNum() + 1);  //分别得到最后一行的行号，和第3条记录的最后一个单元格
        System.out.println("最后一个单元格 :" + row.getLastCellNum());  //分别得到最后一行的行号，和第3条记录的最后一个单元格
        // HSSFRow startRow=sheet.createRow((short)(sheet.getLastRowNum()+1)); // 追加开始行
        // -----------------追加数据-------------------
        int start = sheet.getLastRowNum() + 1; //插入数据开始行
        for (int i = 0; i < dataList.size(); i++) {
            HSSFRow startRow = sheet.createRow(i + start);
            AtomicInteger j = new AtomicInteger();
            LinkedHashMap<String, Object> ltem = dataList.get(i);
            ltem.forEach((k, v) -> {
                HSSFCell cell = startRow.createCell(j.get());
                cell.setCellValue(v.toString());
                j.getAndIncrement();
            });
        }
        // 输出Excel文件
        FileOutputStream out = new FileOutputStream("d://" + name + ".xls");  //向d://test.xls中写数据
        out.flush();
        Workbook.write(out);
        out.close();
    }
    public static void addExcelForXlsx(List<LinkedHashMap<String, Object>> dataList, String name) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("d://" + name + ".xlsx");  //获取d://test.xls,建立数据的输入通道
        XSSFWorkbook Workbook = new XSSFWorkbook(fileInputStream);//得到// 文档对象
        XSSFSheet sheet = Workbook.getSheetAt(0);//根据name获取sheet表
        XSSFRow row = sheet.getRow(0);//获取第一行
        System.out.println("最后一行的行号 :" + sheet.getLastRowNum() + 1);  //分别得到最后一行的行号，和第3条记录的最后一个单元格
        System.out.println("最后一个单元格 :" + row.getLastCellNum());  //分别得到最后一行的行号，和第3条记录的最后一个单元格
        // HSSFRow startRow=sheet.createRow((short)(sheet.getLastRowNum()+1)); // 追加开始行
        // -----------------追加数据-------------------
        int start = sheet.getLastRowNum() + 1; //插入数据开始行
        for (int i = 0; i < dataList.size(); i++) {
            XSSFRow startRow = sheet.createRow(i + start);
            AtomicInteger j = new AtomicInteger();
            LinkedHashMap<String, Object> ltem = dataList.get(i);
            ltem.forEach((k, v) -> {
                XSSFCell cell = startRow.createCell(j.get());
                cell.setCellValue(v.toString());
                j.getAndIncrement();
            });
        }
        // 输出Excel文件
        FileOutputStream out = new FileOutputStream("d://" + name + ".xlsx");  //向d://test.xls中写数据
        out.flush();
        Workbook.write(out);
        out.close();
    }
    /**
     * 读取excel
     *
     * @param name 文件名
     */

    public static void readExcelForXls(String name) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("d:\\" + name + ".xls");  //获取d://test.xls,建立数据的输入通道
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fileInputStream);  //使用POI提供的方法得到excel的信息
        HSSFWorkbook Workbook = new HSSFWorkbook(poifsFileSystem);//得到文档对象
        HSSFSheet sheet = Workbook.getSheet(name);  //根据name获取sheet表
        HSSFRow row = sheet.getRow(1);  //获取第二行（第一行一般是标题）
        int lastRow = sheet.getLastRowNum(); // 返回的是值从0开始的
        System.out.println("总行数：" + (lastRow + 1));
        int lastCell = row.getLastCellNum(); // 返回的值是从1开始的.....
        System.out.println("总列数：" + lastCell);

        for (int i = 0; i <= lastRow; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < lastCell; j++) {
                    HSSFCell cell = row.getCell(j);
                    if (cell != null) System.out.println(cell.getStringCellValue());
                }
            }

        }
    }
    public static void readExcelForXlsx(String name) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("d:\\" + name + ".xlsx");  //获取d://test.xls,建立数据的输入通道
        XSSFWorkbook Workbook = new XSSFWorkbook(fileInputStream);//得到文档对象
        XSSFSheet sheet = Workbook.getSheetAt(0);//根据name获取sheet表
        XSSFRow row = sheet.getRow(1);
        int lastRow = sheet.getLastRowNum(); // 返回的是值从0开始的
        System.out.println("总行数：" + (lastRow + 1));
        int lastCell = row.getLastCellNum(); // 返回的值是从1开始的.....
        System.out.println("总列数：" + lastCell);

        for (int i = 0; i <= lastRow; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < lastCell; j++) {
                    XSSFCell cell = row.getCell(j);
                    if (cell != null) System.out.println(cell.getStringCellValue());
                }
            }

        }
    }
}
