package cn.holelin.dicom.util.pacs;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/15 15:39
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/15 15:39
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class PacsStudySearchRequest {
    private String patientId;
    private String patientName;
    private String accessionNumber;
    private String studyInstanceUid;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
