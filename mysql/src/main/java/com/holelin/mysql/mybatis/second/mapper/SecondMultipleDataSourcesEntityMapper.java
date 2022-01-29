package com.holelin.mysql.mybatis.second.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.holelin.mysql.entity.MultipleDataSourcesEntity;

import java.util.List;

public interface SecondMultipleDataSourcesEntityMapper extends BaseMapper<MultipleDataSourcesEntity> {

    List<MultipleDataSourcesEntity> query();
}
