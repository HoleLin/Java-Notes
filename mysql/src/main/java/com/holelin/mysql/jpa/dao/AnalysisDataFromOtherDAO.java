package com.holelin.mysql.jpa.dao;


import com.holelin.mysql.entity.AnalysisDataFromOther;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @Description: 处理联网平台的数据
 * @Author: HoleLin
 * @CreateDate: 2020/9/9 9:50
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/9/9 9:50
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public interface AnalysisDataFromOtherDAO extends JpaRepository<AnalysisDataFromOther, String> {

    /**
     * 清空数据
     */
    @Modifying
    @Query(
            value = "truncate table analysis_data_other",
            nativeQuery = true
    )
    void truncateTable();

    /**
     * 查询数据表中id最大的记录
     *
     * @param taskId
     * @return
     */
    @Query(value = "select max(other.id) from AnalysisDataFromOther other where other.taskId=:taskId")
    long queryMaxID(@Param("taskId") Long taskId);

    /**
     * 刪除
     *
     * @param taskId 任务
     * @param maxId  最大ID
     * @param num    删除数量
     * @return
     */
    @Modifying
    @Query(value = "delete from analysis_data_other  where task_id=:taskId and id<=:maxId limit :num", nativeQuery = true)
    int deleteByTaskId(@Param("taskId") Long taskId, @Param("maxId") Long maxId, @Param("num") Integer num);

    int countByTaskId(@Param("taskId") Long taskId);
}
