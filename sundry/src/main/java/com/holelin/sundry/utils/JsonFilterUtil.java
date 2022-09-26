package com.holelin.sundry.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/27 15:30
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/27 15:30
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(value = 2, jvmArgs = {"-Xms1G", "-Xmx1G"})
public class JsonFilterUtil {

    public static final String PATH = "../Scryn.json";
    static JSONObject jsonObject = null;
    static Map<String, CommonAttributeType> map = null;

    static {
        final File file = new File(PATH);
        final String content;
        try {
            content = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        jsonObject = JSON.parseObject(content);
        map = findJsonObjectType(jsonObject, "");
    }

    enum CommonAttributeType {
        STRING,
        JSON_OBJECT,
        JSON_ARRAY
    }

    @Data
    @Builder
    static class CommonAttributeBean {

        private String jsonPath;
        private CommonAttributeType attributeType;
    }

    @Data
    static class CommonAttributeTreeBean {

        private String fatherJsonPath;
        private String jsonPath;
        private CommonAttributeType attributeType;
    }


    private static List<CommonAttributeBean> initAttributes(Map<String, CommonAttributeType> map, Integer num) {
        List<CommonAttributeBean> result = new ArrayList<>();
        final String[] keys = map.keySet().toArray(new String[0]);
        for (int i = 0; i < num; i++) {
            final String key = keys[ThreadLocalRandom.current().nextInt(keys.length)];
            final CommonAttributeType commonAttributeType = map.get(key);
            result.add(CommonAttributeBean.builder().jsonPath(key)
                    .attributeType(commonAttributeType).build());
        }
        return result;
    }

    private static List<CommonAttributeTreeBean> convert(List<CommonAttributeBean> attributeBeans) {
        List<CommonAttributeTreeBean> result = new ArrayList<>();
        if (CollUtil.isNotEmpty(attributeBeans)) {
            final HashMap<String, CommonAttributeTreeBean> map = new HashMap<>();
            final TreeMap<Integer, List<CommonAttributeBean>> treeMap = new TreeMap<>(Integer::compareTo);
            final String s = ".";

            attributeBeans.forEach(it -> {
                final String jsonPath = it.jsonPath;
                if (jsonPath.contains(s)) {
                    final int count = StringUtils.countMatches(jsonPath, s);
                    if (treeMap.containsKey(count)) {
                        treeMap.get(count).add(it);
                    } else {
                        List<CommonAttributeBean> list = new ArrayList<>();
                        list.add(it);
                        treeMap.put(count, list);
                    }

                } else {
                    if (treeMap.containsKey(0)) {
                        treeMap.get(0).add(it);
                    } else {
                        List<CommonAttributeBean> list = new ArrayList<>();
                        list.add(it);
                        treeMap.put(0, list);
                    }
                }

            });

            // 遍历TreeMap
            if (CollUtil.isNotEmpty(treeMap)) {
                treeMap.entrySet().stream().flatMap(it -> it.getValue().stream()).forEach(
                        it -> {
                            final String jsonPath = it.jsonPath;
                            if (jsonPath.contains(s)) {
                                // low level
                                final String[] split = jsonPath.split("\\.");
                                if (split.length != 0) {
                                    final String first = split[0];
                                    if (map.containsKey(first)) {
                                        CommonAttributeTreeBean fatherBean = map.get(first);
                                        for (int i = 1; i < split.length; i++) {
                                            final String pre = split[i - 1];
                                            if (map.containsKey(pre)) {
                                                fatherBean = map.get(pre);
                                            }
                                            final String current = split[i];
                                            final CommonAttributeTreeBean treeBean = new CommonAttributeTreeBean();
                                            treeBean.jsonPath = current;
                                            treeBean.fatherJsonPath = fatherBean.getJsonPath();
                                            if (map.containsKey(current)) {
                                                final CommonAttributeTreeBean currentBean = map.get(current);
                                                treeBean.attributeType = currentBean.getAttributeType();
                                            } else {
                                                treeBean.attributeType = it.getAttributeType();
                                            }
                                            map.put(current, treeBean);
                                            result.add(treeBean);
                                        }
                                    }
                                }

                            } else {
                                // top level
                                final CommonAttributeTreeBean treeBean = new CommonAttributeTreeBean();
                                treeBean.fatherJsonPath = "";
                                treeBean.attributeType = it.getAttributeType();
                                treeBean.jsonPath = jsonPath;
                                map.put(jsonPath, treeBean);
                                result.add(treeBean);
                            }
                        }
                );
            }
        }
        return result;
    }

    public static final String PRE_STRING = "$.";

    public static void main(String[] args) throws IOException, RunnerException {
        Options opt = new OptionsBuilder()
                // 包含语义
                // 可以用方法名，也可以用XXX.class.getSimpleName()
                .include(JsonFilterUtil.class.getSimpleName())
                // 预热10轮
                .warmupIterations(10)
                // 代表正式计量测试做10轮，
                // 而每次都是先执行完预热再执行正式计量，
                // 内容都是调用标注了@Benchmark的代码。
                .measurementIterations(10)
                //  forks(3)指的是做3轮测试，
                // 因为一次测试无法有效的代表结果，
                // 所以通过3轮测试较为全面的测试，
                // 而每一轮都是先预热，再正式计量。
                .forks(3)
                .output("/Users/holelin/Desktop/Benchmark6.log")
                .build();

        new Runner(opt).run();

    }


    @Benchmark
    public static void test() throws IOException {

        final int randomNum = ThreadLocalRandom.current().nextInt(map.size());
        final List<CommonAttributeBean> commonAttributeBeans = initAttributes(map, randomNum);
        final List<CommonAttributeTreeBean> convert = convert(commonAttributeBeans);
        final List<CommonAttributeTreeBean> treeBeans = convert.stream()
                .filter(it -> StringUtils.isEmpty(it.getFatherJsonPath())).collect(Collectors.toList());
        final Map<String, List<CommonAttributeTreeBean>> group = convert.stream()
                .collect(Collectors.groupingBy(CommonAttributeTreeBean::getFatherJsonPath));

        filter(jsonObject, treeBeans, group);
    }

    public static Map<String, CommonAttributeType> findJsonObjectType(JSONObject object, String prefix) {
        Map<String, CommonAttributeType> map = new HashMap<>(64);
        object.forEach((key, value) -> {
            if (value instanceof JSONArray) {
                final JSONArray array = (JSONArray) value;
                if (array.size() != 0) {
                    final Object item = array.get(0);
                    if (item instanceof JSONObject) {
                        map.putAll(findJsonObjectType((JSONObject) item, key));
                    }
                }
                putMap(prefix, map, key, CommonAttributeType.JSON_ARRAY);
            } else if (value instanceof JSONObject) {
                putMap(prefix, map, key, CommonAttributeType.JSON_OBJECT);
            } else if (value instanceof Integer) {
                putMap(prefix, map, key, CommonAttributeType.STRING);

            } else if (value instanceof String) {
                putMap(prefix, map, key, CommonAttributeType.STRING);

            } else if (value instanceof Double) {
                putMap(prefix, map, key, CommonAttributeType.STRING);

            } else if (value instanceof BigDecimal) {
                putMap(prefix, map, key, CommonAttributeType.STRING);
            }
        });
        return map;
    }

    private static void putMap(String prefix, Map<String, CommonAttributeType> map, String key,
            CommonAttributeType type) {
        if (StringUtils.isNotEmpty(prefix)) {
            map.put(prefix + "." + key, type);
        } else {
            map.put(key, type);
        }
    }

    public static HashMap<String, Object> filter(Object jsonObject,
            List<CommonAttributeTreeBean> treeBeans,
            Map<String, List<CommonAttributeTreeBean>> group) {
        final HashMap<String, Object> content = new HashMap<>(16);
        for (CommonAttributeTreeBean treeBean : treeBeans) {
            // 获取单个属性
            final String jsonPath = treeBean.getJsonPath();
            final CommonAttributeType attributeType = treeBean.getAttributeType();
            final String realPath = PRE_STRING + jsonPath;
            //
            if (JSONPath.contains(jsonObject, realPath)) {
                if (CommonAttributeType.STRING.equals(attributeType)) {
                    final String value = String.valueOf(JSONPath.eval(jsonObject, realPath));
                    content.put(jsonPath, value);
                } else if (CommonAttributeType.JSON_OBJECT.equals(attributeType)) {
                    final JSONObject value = (JSONObject) JSONPath.eval(jsonObject, realPath);
                    content.put(jsonPath, value);
                } else if (CommonAttributeType.JSON_ARRAY.equals(attributeType)) {
                    final JSONArray values = (JSONArray) JSONPath.eval(jsonObject, realPath);
                    // 判断是否有必要继续向下获取
                    if (CollUtil.isNotEmpty(group) && group.containsKey(jsonPath)) {
                        final List<CommonAttributeTreeBean> childBean = group.get(jsonPath);
                        final List<HashMap<String, Object>> list = new ArrayList<>();
                        for (Object value : values) {
                            list.add(filter(value, childBean, group));
                        }
                        content.put(jsonPath, list);
                    } else {
                        //  添加当前节点
                        content.put(jsonPath, values);
                    }
                }
            }
        }
        return content;
    }
}
