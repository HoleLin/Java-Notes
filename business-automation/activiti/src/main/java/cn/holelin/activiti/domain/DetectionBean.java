package cn.holelin.activiti.domain;

import lombok.Data;

import java.io.Serializable;
/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/12/5 10:25 上午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/12/5 10:25 上午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class DetectionBean implements Serializable {

    private String taskId;
    private String code;
}
