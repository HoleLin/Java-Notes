package cn.holelin.dicom.controller;

import cn.holelin.dicom.entity.DicomFrame;
import cn.holelin.dicom.entity.DicomTag;
import cn.holelin.dicom.entity.DicomTagDict;
import cn.holelin.dicom.mapper.DicomTagDictMapper;
import cn.holelin.dicom.mapper.DicomTagMapper;
import cn.holelin.dicom.util.DicomDesensitized;
import cn.holelin.dicom.util.DicomParse;
import cn.holelin.dicom.util.validator.XmlValidator;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    private static String fileDirPath = "classpath:dicom/xxx/";
    private static String filePath = "classpath:dicom/xxx.dcm";
    @Autowired
    private DicomTagMapper dicomTagMapper;
    @Autowired
    private DicomTagDictMapper dicomTagDictMapper;

    /**
     *
     */
    @GetMapping("/desensitized")
    public void desensitized() throws IOException {
        final File dicomFile = ResourceUtils.getFile(filePath);
        final List<DicomTag> dicomTags = dicomTagMapper.queryNeedDesensitized();
        DicomFrame dicomFrame = new DicomFrame();
        dicomFrame.setFile(dicomFile);
        dicomFrame.setMetaData(DicomParse.parseMetaData(dicomFile));
        dicomFrame.setAttributes(DicomParse.parseAttributes(dicomFile));
        dicomFrame.setDataSet(DicomParse.parseDataSet(dicomFile));
        dicomFrame.setSourceFileName(dicomFile.getName());
        DicomParse.printTagInfo(dicomFrame.getAttributes(), dicomTags);
        DicomDesensitized.defaultDesensitization(dicomFrame, dicomTags);
        log.info("===========================================");
        DicomParse.printTagInfo(dicomFrame.getAttributes(), dicomTags);

    }

    @GetMapping("/parse")
    public void parse() throws IOException {
        List<DicomFrame> list = new ArrayList<>();
        final File file = ResourceUtils.getFile(fileDirPath);
        try (Stream<Path> paths = Files.walk(file.toPath())) {
            paths.filter(it -> !Files.isDirectory(it)).forEach(it -> {
                        try {
                            final File dicomFile = it.toFile();
                            DicomFrame dicomFrame = new DicomFrame();
                            dicomFrame.setFile(dicomFile);
                            dicomFrame.setMetaData(DicomParse.parseMetaData(dicomFile));
                            dicomFrame.setAttributes(DicomParse.parseAttributes(dicomFile));
                            dicomFrame.setDataSet(DicomParse.parseDataSet(dicomFile));
                            dicomFrame.setSourceFileName(dicomFile.getName());
                            list.add(dicomFrame);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
        log.info("" + list);
    }

    @GetMapping("/validated")
    public void validated() throws IOException {
        final File dicomFile = ResourceUtils.getFile(filePath);
        DicomFrame dicomFrame = new DicomFrame();
        dicomFrame.setFile(dicomFile);
        dicomFrame.setAttributes(DicomParse.parseAttributes(dicomFile));
        dicomFrame.setSourceFileName(dicomFile.getName());
//        final DbValidator dbValidator = new DbValidator(initMap());
//        log.error("result:{}", dbValidator.validated(dicomFrame));
        final XmlValidator xmlValidator = new XmlValidator();
        log.error("result:{}", xmlValidator.validated(dicomFrame));
    }

    public Map<Long, Set<String>> initMap() {
        Map<Long, Set<String>> map = new HashMap<>();
        final List<DicomTag> dicomTags = dicomTagMapper.queryNeedCheck();
        if (CollUtil.isNotEmpty(dicomTags)) {
            final List<Integer> tagIds = dicomTags.stream().map(DicomTag::getId).collect(Collectors.toList());
            final Map<Integer, Long> tempMap = dicomTags.stream().collect(Collectors.toMap(DicomTag::getId, DicomTag::getTagValue));
            final List<DicomTagDict> dicomTagDict = dicomTagDictMapper.queryDictByTagIds(tagIds);
            if (CollUtil.isNotEmpty(dicomTagDict)) {
                // 将字典项进行分组
                final Map<Integer, List<DicomTagDict>> group =
                        dicomTagDict.stream().collect(Collectors.groupingBy(DicomTagDict::getTagId));
                group.forEach((k, v) -> {
                    final Set<String> dictValueSet = v.stream().map(DicomTagDict::getDictValue).collect(Collectors.toSet());
                    map.put(tempMap.get(k), dictValueSet);
                });
            }
        }
        return map;
    }
}
