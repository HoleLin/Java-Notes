package com.holelin.sundry;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.holelin.sundry.domain.ExcelData;
import com.holelin.sundry.utils.excel.DataReaderListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class EasyExcelTest {

    @Test
    public void readExcel(){
        String fileName = "";
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(fileName, ExcelData.class, new DataReaderListener()).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
    }
}
