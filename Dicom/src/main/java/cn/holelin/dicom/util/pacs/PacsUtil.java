package cn.holelin.dicom.util.pacs;

import cn.holelin.dicom.enums.InformationModelEnum;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.net.IncompatibleConnectionException;
import org.dcm4che3.tool.common.CLIUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/15 15:30
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/15 15:30
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class PacsUtil {

    public void find(PacsStudySearchRequest request) throws IncompatibleConnectionException, GeneralSecurityException, IOException, InterruptedException {
        final FIndSCUHelper helper = new FIndSCUHelper("cjl-find-scu", "cjl-find-scu",
                "192.168.40.49", 105, "ebm-pacs");
        helper.template(InformationModelEnum.FIND, buildAttributes(request));
    }

    private Attributes buildAttributes(PacsStudySearchRequest request) {
        final Attributes attributes = new Attributes();
        final LocalDate startDate = request.getStartDate();
        final LocalDate endDate = request.getEndDate();
        if (Objects.nonNull(startDate) && Objects.nonNull(endDate)) {
            final String days = startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            CLIUtils.addAttributes(attributes, CLIUtils.toTags(new String[]{"StudyDate"}), days);

        }
        final LocalTime startTime = request.getStartTime();
        final LocalTime endTime = request.getEndTime();
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            final String times = startTime.format(DateTimeFormatter.ofPattern("hhmmss")) + "-" + endTime.format(DateTimeFormatter.ofPattern("hhmmss"));
            CLIUtils.addAttributes(attributes, CLIUtils.toTags(new String[]{"StudyTime"}), times);
        }
        attributes.setString(Tag.QueryRetrieveLevel, VR.CS, InformationModelEnum.FIND.level);
        final String studyInstanceUid = request.getStudyInstanceUid();
        if (StringUtils.hasText(studyInstanceUid)) {
            attributes.setString(Tag.StudyInstanceUID, VR.CS, studyInstanceUid);
        }

        final String patientId = request.getPatientId();
        if (StringUtils.hasText(patientId)) {
            attributes.setString(Tag.PatientID, VR.CS, patientId);
        }
        final String patientName = request.getPatientName();
        if (StringUtils.hasText(patientName)) {
            attributes.setString(Tag.PatientName, VR.CS, patientName);
        }
        final String accessionNumber = request.getAccessionNumber();
        if (StringUtils.hasText(accessionNumber)) {
            attributes.setString(Tag.AccessionNumber, VR.CS, accessionNumber);
        }
        return attributes;
    }

    public static void main(String[] args) throws IncompatibleConnectionException, GeneralSecurityException, IOException, InterruptedException {
        final PacsStudySearchRequest request = new PacsStudySearchRequest();
        final PacsUtil pacsUtil = new PacsUtil();
        pacsUtil.find(request);

    }
}
