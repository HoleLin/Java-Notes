package com.holelin.mysql.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.holelin.mysql.entity.MultipleDataSourcesEntity;

import java.util.List;

public interface MultipleDataSourcesEntityMapper extends BaseMapper<MultipleDataSourcesEntity> {

    List<MultipleDataSourcesEntity> query();
}
