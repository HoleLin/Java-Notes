package com.holelin.mysql.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.holelin.mysql.entity.MultipleDataSourcesEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MultipleDataSourcesEntityMapper extends BaseMapper<MultipleDataSourcesEntity> {

    List<MultipleDataSourcesEntity> query();
}
