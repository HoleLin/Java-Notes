package cn.holelin.files.utils;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/7/29 14:25
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/7/29 14:25
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class PdfUtil {

    public static void main(String[] args) throws DocumentException, FileNotFoundException {
        final String path = ResourceUtils.getFile("classpath:pdf").toString();

        final FileOutputStream os = new FileOutputStream(path + File.separator + "createSamplePDF.pdf");

        //页面大小
        Rectangle rect = new Rectangle(PageSize.B5.rotate());
        //页面背景色
        rect.setBackgroundColor(BaseColor.ORANGE);

        Document doc = new Document(rect);

        PdfWriter writer = PdfWriter.getInstance(doc, os);

        //PDF版本(默认1.4)
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_2);

        //文档属性
        doc.addTitle("Title@sample");
        doc.addAuthor("Author@rensanning");
        doc.addSubject("Subject@iText sample");
        doc.addKeywords("Keywords@iText");
        doc.addCreator("Creator@iText");

        //页边空白
        doc.setMargins(10, 20, 30, 40);

        doc.open();
        doc.add(new Paragraph("Hello World"));

        doc.close();
    }
}
