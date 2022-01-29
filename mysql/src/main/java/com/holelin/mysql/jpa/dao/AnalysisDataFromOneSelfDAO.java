package com.holelin.mysql.jpa.dao;


import com.holelin.mysql.entity.AnalysisDataFromOneSelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @Description: 处理一机一档的数据
 * @Author: HoleLin
 * @CreateDate: 2020/9/9 9:49
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/9/9 9:49
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public interface AnalysisDataFromOneSelfDAO extends JpaRepository<AnalysisDataFromOneSelf, String> {

    /**
     * 清空数据
     */
    @Modifying
    @Query(
            value = "truncate table analysis_data_oneself",
            nativeQuery = true
    )
    void truncateTable();

}
