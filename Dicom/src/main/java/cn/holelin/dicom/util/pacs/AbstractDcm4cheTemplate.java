package cn.holelin.dicom.util.pacs;

import cn.holelin.dicom.enums.InformationModelEnum;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.UID;
import org.dcm4che3.net.ApplicationEntity;
import org.dcm4che3.net.Association;
import org.dcm4che3.net.Connection;
import org.dcm4che3.net.Device;
import org.dcm4che3.net.IncompatibleConnectionException;
import org.dcm4che3.net.QueryOption;
import org.dcm4che3.net.TransferCapability;
import org.dcm4che3.net.pdu.AAssociateRQ;
import org.dcm4che3.net.pdu.ExtendedNegotiation;
import org.dcm4che3.net.pdu.PresentationContext;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.EnumSet;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/8/15 10:20
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/8/15 10:20
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public abstract class AbstractDcm4cheTemplate {

    /**
     * 本地设备
     */
    private Device device = new Device();

    /**
     * 本地pacs名称
     */
    private ApplicationEntity ae = new ApplicationEntity();

    /**
     * 本地连接
     */
    private Connection local = new Connection();

    /**
     * 远程连接 即Find操作的目标服务
     */
    private Connection remote = new Connection();

    /**
     * 连接请求
     */
    private AAssociateRQ aarq = new AAssociateRQ();

    /**
     * DICOM连接
     */
    public Association association;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


    public AbstractDcm4cheTemplate(String deviceName, String aeTitle, String remoteHostName,
                                   Integer remotePort, String remoteAeTitle) {
        device.setDeviceName(deviceName);
        ae.setAETitle(aeTitle);
        remote.setHostname(remoteHostName);
        remote.setPort(remotePort);
        aarq.setCalledAET(remoteAeTitle);

        device.addConnection(local);
        device.addApplicationEntity(ae);
        ae.addConnection(local);
        ae.addTransferCapability(new TransferCapability(null, "*", TransferCapability.Role.SCP, "*"));
        device.setExecutor(executorService);
        device.setScheduledExecutor(scheduledExecutorService);
    }

    private void connect() throws IncompatibleConnectionException, GeneralSecurityException, IOException, InterruptedException {
        association = ae.connect(local, remote, aarq);
    }

    private void disconnect() {
        if (Objects.nonNull(association) && association.isReadyForDataTransfer()) {
            try {
                association.waitForOutstandingRSP();
                association.release();
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void template(InformationModelEnum model, Attributes conditions) throws IncompatibleConnectionException, GeneralSecurityException, IOException, InterruptedException {
        aarq.addPresentationContext(new PresentationContext(
                1, model.cuid,
                UID.ImplicitVRLittleEndian,
                UID.ExplicitVRLittleEndian,
                UID.ExplicitVRBigEndian
        ));
        aarq.addPresentationContextFor(model.cuid, UID.ImplicitVRLittleEndian);
        //rq.addPresentationContextFor(this.QueryType, UID.ExplicitVRLittleEndian);

        final EnumSet<QueryOption> queryOptions = EnumSet.allOf(QueryOption.class);

        aarq.addExtendedNegotiation(new ExtendedNegotiation(model.cuid, QueryOption.toExtendedNegotiationInformation(queryOptions)));

        connect();

        try {
            execute(model, conditions);
        } finally {
            disconnect();
        }
    }

    /**
     * @param model
     * @param conditions
     */
    abstract void execute(InformationModelEnum model, Attributes conditions) throws IOException, InterruptedException;
}
