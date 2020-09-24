package com.holelin.mysql.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Description: 对比分析 数据源1
 * @Author: HoleLin
 * @CreateDate: 2020/9/9 9:36
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/9/9 9:36
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Entity
@Table(name = "analysis_data_oneself")
public class AnalysisDataFromOneSelf {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    /**
     * 所属任务ID
     */
    @Basic
    private Long taskId;

    /**
     * 组织机构编码
     */
    @Basic
    private String organizationCode;
    /**
     * 组织机构名称
     */
    @Basic
    private String organizationName;
    /**
     * 设备名称
     */
    @Basic
    private String equipmentName;
    /**
     * 设备编码
     */
    @Basic
    private String equipmentCode;
    /**
     * 行政区域
     */
    @Basic
    private String civilCode;
}
