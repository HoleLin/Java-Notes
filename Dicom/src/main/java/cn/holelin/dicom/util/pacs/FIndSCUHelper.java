package cn.holelin.dicom.util.pacs;

import cn.holelin.dicom.enums.InformationModelEnum;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.net.Association;
import org.dcm4che3.net.DimseRSPHandler;
import org.dcm4che3.net.Priority;
import org.dcm4che3.net.Status;

import java.io.IOException;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/15 15:24
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/15 15:24
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class FIndSCUHelper extends AbstractDcm4cheTemplate {

    public FIndSCUHelper(String deviceName, String aeTitle, String remoteHostName,
                         Integer remotePort, String remoteAeTitle) {
        super(deviceName, aeTitle, remoteHostName, remotePort, remoteAeTitle);
    }

    @Override
    void execute(InformationModelEnum model, Attributes conditions) throws IOException, InterruptedException {
        // 执行c-find
        this.association.cfind(model.cuid, Priority.NORMAL, conditions,
                null, new DimseRSPHandler(association.nextMessageID()) {

                    int cancelAfter;
                    int numMatches;

                    @Override
                    public void onDimseRSP(Association as, Attributes cmd, Attributes data) {
                        super.onDimseRSP(as, cmd, data);
                        int status = cmd.getInt(Tag.Status, -1);
                        System.out.println("patientName: "+data.getString(Tag.PatientName));
                        if (Status.isPending(status)) {
                            ++numMatches;
                            if (cancelAfter != 0 && numMatches >= cancelAfter) {
                                try {
                                    cancel(as);
                                    cancelAfter = 0;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

}
