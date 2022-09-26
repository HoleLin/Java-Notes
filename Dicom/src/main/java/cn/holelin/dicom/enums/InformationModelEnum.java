package cn.holelin.dicom.enums;

import org.dcm4che3.data.UID;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/15 15:00
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/15 15:00
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum InformationModelEnum {
    /**
     * FIND操作
     */
    FIND(UID.StudyRootQueryRetrieveInformationModelFind, "STUDY"),
    /**
     * MOVE操作
     */
    MOVE(UID.StudyRootQueryRetrieveInformationModelMove, "STUDY");

    public String cuid;
    public String level;


    InformationModelEnum(String cuid, String level) {
        this.cuid = cuid;
        this.level = level;
    }
}
