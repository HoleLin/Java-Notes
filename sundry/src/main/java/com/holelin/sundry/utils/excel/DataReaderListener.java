package com.holelin.sundry.utils.excel;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.holelin.sundry.domain.DepartmentBucket;
import com.holelin.sundry.domain.ExcelData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

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
    private static String configDepartment = "[{\"departmentCode\":\"xxxxx\",\"simpleName\":\"S0\",\"includes\":[\"i1\",\"i2\",\"i3\",\"i4\",\"i5\",\"i6\"],\"sort\":1},\n" +
            "{\"departmentCode\":\"xxx\",\"simpleName\":\"S1\",\"sort\":2},\n" +
            "{\"departmentCode\":\"xxx\",\"simpleName\":\"S2\",\"excludes\":[\"xxx1\"],\"sort\":3},\n" +
            "{\"departmentCode\":\"xxx\",\"simpleName\":\"S3\",\"sort\":4},\n" +
            "{\"departmentCode\":\"xxx\",\"simpleName\":\"S4\",\"sort\":5},\n" +
            "{\"departmentCode\":\"xxx\",\"simpleName\":\"S5\",\"includes\":[\"xxx2\"],\"sort\":6},\n" +
            "{\"departmentCode\":\"xxx\",\"simpleName\":\"S6\",\"sort\":7},\n" +
            "{\"departmentCode\":\"xxx\",\"simpleName\":\"S7\",\"includes\":[\"xxx3\"],\"sort\":8},\n" +
            "{\"departmentCode\":\"xxx\",\"simpleName\":\"S8\",\"sort\":9},\n" +
            "{\"departmentCode\":\"xxx\",\"simpleName\":\"S9\",\"sort\":10},\n" +
            "{\"departmentCode\":\"xxx\",\"simpleName\":\"S10\",\"sort\":11},\n" +
            "{\"departmentCode\":\"xxx\",\"simpleName\":\"S11\",\"alias\":[\"xxx4\"],\"sort\":12},\n" +
            "{\"departmentCode\":\"xxx\",\"simpleName\":\"S12\",\"alias\":[\"xxx5\",\"xxx6\",\"xxx7\"],\"sort\":13}]\n";


    private static final String OTHER = "其他";
    private static final Map<String, List<String>> map = new HashMap<>();

    @Override
    public void invoke(ExcelData excelData, AnalysisContext analysisContext) {
        List<Config> configList = JSONUtil.toList(JSONUtil.parseArray(configDepartment), Config.class);
        String before = excelData.getDepartmentName();
        log.info("before:{}", before);
        String after = transformDepartmentInfo(before, configList);
        log.info("after:{}", after);
        if (map.containsKey(after)) {
            List<String> strings = map.get(after);
            strings.add(before);
            map.put(after, strings);
        } else {
            List<String> strings = new ArrayList<>();
            strings.add(before);
            map.put(after, strings);
        }
    }

    private static String transformDepartmentInfo(String sourceDepartmentInfo, List<Config> configList) {
        List<DepartmentBucket> container = new ArrayList<>(configList.size());
        // 遍历配置的组织机构
        int inConfigIndex = 0;
        for (Config config : configList) {
            String simpleName = config.getSimpleName();
            List<String> alias = config.getAlias();
            List<String> excludes = config.getExcludes();
            List<String> includes = config.getIncludes();
            // 判断存在与必包含
            if (CollectionUtil.isNotEmpty(includes)) {
                if (includes.contains(sourceDepartmentInfo)) {
                    DepartmentBucket bucket = DepartmentBucket.builder()
                            .departmentName(simpleName)
                            .index(0).build();
                    container.add(bucket);
                    break;
                }
            }
            boolean isExclude = false;
            if (CollectionUtil.isNotEmpty(excludes)) {
                for (String exclude : excludes) {
                    int index = sourceDepartmentInfo.indexOf(exclude);
                    if (index >= 0) {
                        // 包含了排除字符串 中断循环
                        isExclude = true;
                        break;
                    }
                }
            }
            if (isExclude) {
                inConfigIndex++;
                continue;
            } else {
                // 如果存在别名则使用别名,否则使用简易名称
                if (CollectionUtil.isNotEmpty(alias)) {
                    int aliasIndex = 0;
                    for (String item : alias) {
                        // 判断 sourceDepartmentInfo是否包含config,找到直接返回
                        int index = sourceDepartmentInfo.indexOf(item);
                        // 排除字段
                        if (index >= 0) {
                            DepartmentBucket bucket = DepartmentBucket.builder()
                                    .departmentName(simpleName)
                                    .index(index).build();
                            container.add(bucket);
                        } else {
                            aliasIndex++;
                        }
                    }
                    if (aliasIndex >= alias.size()) {
                        inConfigIndex++;
                    }
                } else {
                    // 判断 sourceDepartmentInfo是否包含config,找到直接返回
                    int index = sourceDepartmentInfo.indexOf(simpleName);
                    if (index >= 0) {
                        DepartmentBucket bucket = DepartmentBucket.builder()
                                .departmentName(simpleName)
                                .index(index).build();
                        container.add(bucket);
                    } else {
                        inConfigIndex++;
                    }
                }
            }
        }
        if (inConfigIndex >= configList.size()) {
            DepartmentBucket bucket = DepartmentBucket.builder()
                    .departmentName(OTHER)
                    .index(-1).build();
            container.add(bucket);
        }
        container.sort(Comparator.comparingInt(DepartmentBucket::getIndex));
        // 排序后取出最匹配的结果 即第一个
        List<String> collect = container.stream()
                .map(DepartmentBucket::getDepartmentName)
                .collect(Collectors.toList());
        return collect.get(0);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        log.info("所有数据解析完成！");
        log.info("{}", map);

    }

    /**
     *
     */
    @Data
    private class Config {
        /**
         * 组织机构编码
         */
        private String departmentCode;
        /**
         * 简易名称
         */
        private String simpleName;
        /**
         * 别名
         */
        private List<String> alias;
        /**
         * 排除列表--局部字符串
         */
        private List<String> excludes;
        /**
         * 包含列表--完整字符串
         */
        private List<String> includes;
        /**
         * 排序字段
         */
        private Integer sort;
    }
}
