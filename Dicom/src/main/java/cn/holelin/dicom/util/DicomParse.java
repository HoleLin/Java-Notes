package cn.holelin.dicom.util;

import lombok.extern.slf4j.Slf4j;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.IOD;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.ValidationResult;
import org.dcm4che3.io.DicomInputStream;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @Description: Dicom Tag 解析类
 * @Author: HoleLin
 * @CreateDate: 2022/3/23 3:33 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/3/23 3:33 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
public class DicomParse {

    private static String testFile = "/dicom/test-125.dcm";
    private static String validationFile = "/validators/dicom-validation.xml";

    public static void main(String[] args) throws IOException {
        final URL resource = DicomParse.class.getResource(testFile);
        final URL resource2 = DicomParse.class.getResource(validationFile);
        final DicomInputStream dicomInputStream = new DicomInputStream(new File(resource.getFile()));
        final Attributes metaData = dicomInputStream.readFileMetaInformation();
        final Attributes attributes = dicomInputStream.readDataset(-1, it -> it.tag() == Tag.PixelData);
        final IOD validation = IOD.load(resource2.getPath());
        final ValidationResult validateResult = attributes.validate(validation);
        System.out.println(validateResult);
        System.out.println("PatientPosition: " + attributes.getString(Tag.PatientPosition));
        System.out.println("Modality:" + attributes.getString(Tag.Modality));
        System.out.println("ImagePositionPatient: " + attributes.getString(Tag.ImagePositionPatient));
        System.out.println("SliceThickness: " + attributes.getString(Tag.SliceThickness));
        System.out.println("ImageType: " + attributes.getString(Tag.ImageType));
        System.out.println("InstanceNumber: " + attributes.getString(Tag.InstanceNumber));
        System.out.println("Rows: " + attributes.getString(Tag.Rows));
        System.out.println("Columns: " + attributes.getString(Tag.Columns));
        System.out.println("Manufacturer: " + attributes.getString(Tag.Manufacturer));
        System.out.println("AcquisitionNumber: " + attributes.getString(Tag.AcquisitionNumber));
        System.out.println("StudyInstanceUID: " + attributes.getString(Tag.StudyInstanceUID));
        System.out.println("StudyID: " + attributes.getString(Tag.StudyID));
        System.out.println("SeriesInstanceUID: " + attributes.getString(Tag.SeriesInstanceUID));


        System.out.println("PatientID: " + attributes.getString(Tag.PatientID));
        System.out.println("PatientName: " + attributes.getString(Tag.PatientName));
        System.out.println("PatientAge: " + attributes.getString(Tag.PatientAge));
        System.out.println("PatientBirthDate: " + attributes.getString(Tag.PatientBirthDate));
        System.out.println("PatientSex: " + attributes.getString(Tag.PatientSex));
        System.out.println("StudyDescription: " + attributes.getString(Tag.StudyDescription));
        System.out.println("StudyDate: " + attributes.getString(Tag.StudyDate));
        System.out.println("StudyTime: " + attributes.getString(Tag.StudyTime));
        System.out.println("SeriesNumber: " + attributes.getString(Tag.SeriesNumber));
        System.out.println("SeriesDescription: " + attributes.getString(Tag.SeriesDescription));
        System.out.println("SeriesDate: " + attributes.getString(Tag.SeriesDate));
        System.out.println("SeriesTime: " + attributes.getString(Tag.SeriesTime));
        System.out.println("ImageOrientationPatient: " + attributes.getString(Tag.ImageOrientationPatient));
        System.out.println("SpacingBetweenSlices: " + attributes.getString(Tag.SpacingBetweenSlices));
        System.out.println("SliceLocation: " + attributes.getString(Tag.SliceLocation));
        System.out.println("FrameOfReferenceUID: " + attributes.getString(Tag.FrameOfReferenceUID));
        System.out.println("SOPClassUID: " + attributes.getString(Tag.SOPClassUID));
        System.out.println("SOPInstanceUID: " + attributes.getString(Tag.SOPInstanceUID));
        System.out.println("ContentDate: " + attributes.getString(Tag.ContentDate));
        System.out.println("ContentTime: " + attributes.getString(Tag.ContentTime));
        System.out.println("WindowWidth: " + attributes.getString(Tag.WindowWidth));
        System.out.println("WindowCenter: " + attributes.getString(Tag.WindowCenter));
        System.out.println("ProtocolName: " + attributes.getString(Tag.ProtocolName));
        System.out.println("InstitutionName: " + attributes.getString(Tag.InstitutionName));
        System.out.println("AccessionNumber: " + attributes.getString(Tag.AccessionNumber));
        System.out.println("ConvolutionKernel: " + attributes.getString(Tag.ConvolutionKernel));
        System.out.println("StationName: " + attributes.getString(Tag.StationName));
        System.out.println("BodyPartExamined: " + attributes.getString(Tag.BodyPartExamined));
        System.out.println("SpecificCharacterSet: " + attributes.getString(Tag.SpecificCharacterSet));
        System.out.println("ImagesInAcquisition: " + attributes.getString(Tag.ImagesInAcquisition));
        System.out.println("TransferSyntaxUID: " + attributes.getString(Tag.TransferSyntaxUID));
        System.out.println("AcquisitionTime: " + attributes.getString(Tag.AcquisitionTime));
        System.out.println("MediaStorageSOPClassUID: " + attributes.getString(Tag.MediaStorageSOPClassUID));
        System.out.println("PixelData: " + attributes.getString(Tag.PixelData));
        System.out.println("ManufacturerModelName: " + attributes.getString(Tag.ManufacturerModelName));
    }
}
