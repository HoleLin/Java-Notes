package com.holelin.mysql.service.impl;

import com.holelin.mysql.entity.MultipleDataSourcesEntity;
import com.holelin.mysql.jpa.dao.MultipleDataSourcesEntityDao;
import com.holelin.mysql.mybatis.mapper.MultipleDataSourcesEntityMapper;
import com.holelin.mysql.mybatis.second.mapper.SecondMultipleDataSourcesEntityMapper;
import com.holelin.mysql.service.MultipleDataSourceService;
import com.holelin.mysql.vo.Request;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MultipleDataSourceServiceImpl implements MultipleDataSourceService {

    @Autowired
    private MultipleDataSourcesEntityDao multipleDataSourcesEntityDao;
    @Autowired
    private MultipleDataSourcesEntityMapper multipleDataSourcesEntityMapper;
    @Autowired
    private SecondMultipleDataSourcesEntityMapper secondMultipleDataSourcesEntityMapper;

    @Override
    public List<MultipleDataSourcesEntity> queryFromJpa() {
        return multipleDataSourcesEntityDao.findAll();
    }

    @Override
    public void addJpa(Request request) {
        MultipleDataSourcesEntity entity = new MultipleDataSourcesEntity();
        BeanUtils.copyProperties(request, entity);
        multipleDataSourcesEntityDao.save(entity);
    }

    @Override
    public void addMyBatis(Request request) {
        MultipleDataSourcesEntity entity = new MultipleDataSourcesEntity();
        BeanUtils.copyProperties(request, entity);
        multipleDataSourcesEntityMapper.insert(entity);
    }

    @Override
    public List<MultipleDataSourcesEntity> queryFromMyBatis() {
        return multipleDataSourcesEntityMapper.query();
    }

    @Override
    public List<MultipleDataSourcesEntity> queryFromMyBatisOtherSource() {
        return secondMultipleDataSourcesEntityMapper.query();
    }
}
