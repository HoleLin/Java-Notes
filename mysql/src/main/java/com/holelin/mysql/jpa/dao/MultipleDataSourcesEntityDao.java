package com.holelin.mysql.jpa.dao;

import com.holelin.mysql.entity.MultipleDataSourcesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultipleDataSourcesEntityDao extends JpaRepository<MultipleDataSourcesEntity,Integer> {
    
}
