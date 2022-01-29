package com.holelin.mysql.service;

import com.holelin.mysql.entity.MultipleDataSourcesEntity;
import com.holelin.mysql.vo.Request;

import java.util.List;

public interface MultipleDataSourceService {

    List<MultipleDataSourcesEntity> queryFromJpa();

    void addJpa(Request request);
    void addMyBatis(Request request);

    List<MultipleDataSourcesEntity> queryFromMyBatis();

    List<MultipleDataSourcesEntity> queryFromMyBatisOtherSource();
}
