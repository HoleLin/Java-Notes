package cn.holelin.dicom.mapper;

import cn.holelin.dicom.entity.DicomTagDict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DicomTagDictMapper {
    /**
     * 通过Dicom Tag Id来查询tag对应值的字典项
     * @param tagIds tagId列表
     * @return 字典项列表
     */
    List<DicomTagDict> queryDictByTagIds(List<Integer> tagIds);
}
