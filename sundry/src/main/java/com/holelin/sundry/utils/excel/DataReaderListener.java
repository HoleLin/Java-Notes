package com.holelin.sundry.utils.excel;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.holelin.sundry.domain.ExcelData;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/9/8 13:50
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/9/8 13:50
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Slf4j
public class DataReaderListener extends AnalysisEventListener<ExcelData> {

    List<ExcelData> list = new ArrayList<>();
    private static final int BATCH_COUNT = 20;

    @Override
    public void invoke(ExcelData excelData, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSONUtil.toJsonStr(excelData));
        list.add(excelData);
//        if (list.size() >= BATCH_COUNT) {
//            // 保存数据
//            // 清理list
//            list.clear();
//        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        log.info("所有数据解析完成！");
    }
}
