package org.holelin.websocket.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/1/24 3:31 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/1/24 3:31 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class ConsultationAppealRequest {

    private String hospitalName;
    private String hospitalId;

    private String specialistName;
    private String specialistUserId;

    private String appeal;

}
