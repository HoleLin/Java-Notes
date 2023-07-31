package cn.holelin.files.utils;


import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.blend.BlendMode;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.springframework.util.ResourceUtils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static void main(String[] args) throws Exception {
//        useItextEncryptPdf();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String time = dateTimeFormatter.format(now);
        String source = "C:\\Users\\YW\\Desktop\\安装指南.pdf";
        String output = "C:\\Users\\YW\\Desktop\\output7.pdf";
        String watermarkText = time + " 用户名 账号ID";
        watermarkSingle(source, output, watermarkText);
//        waterMarkFill(source, output, watermarkText);
    }


    private static void watermarkSingle(String filePath, String outputPath, String watermarkText) throws Exception {
        //创建新pdf文件
        File tmpPDF = new File(outputPath);
        //打开pdf文件
        File file = ResourceUtils.getFile("classpath:Alibaba-PuHuiTi-Medium.ttf");
        try (PDDocument doc = PDDocument.load(Paths.get(filePath).toFile());
             FileInputStream fileInputStream = new FileInputStream(file)) {
            doc.setAllSecurityToBeRemoved(true);
            PDFont font = PDType0Font.load(doc, fileInputStream, true);
            //遍历pdf所有页
            for (PDPage page : doc.getPages()) {
                try (PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                    final float fontHeight = 30;
                    final float width = page.getMediaBox().getWidth();
                    final float height = page.getMediaBox().getHeight();
                    final float stringWidth = font.getStringWidth(watermarkText) / 1000 * fontHeight;
                    final float diagonalLength = (float) Math.sqrt(width * width + height * height);
                    final float angle = (float) Math.atan2(height, width);
                    final float x = (diagonalLength - stringWidth) / 2;
                    final float y = -fontHeight / 4;
                    cs.transform(Matrix.getRotateInstance(angle, 0, 0));
                    cs.setFont(font, fontHeight);

                    final PDExtendedGraphicsState gs = new PDExtendedGraphicsState();
                    // 设置透明度
                    gs.setNonStrokingAlphaConstant(0.2f);
                    gs.setStrokingAlphaConstant(0.2f);
                    gs.setBlendMode(BlendMode.MULTIPLY);
                    gs.setLineWidth(3f);
                    cs.setGraphicsStateParameters(gs);

                    cs.beginText();
                    cs.newLineAtOffset(x, y);
                    cs.showText(watermarkText);
                    cs.endText();
                }
            }
            doc.save(tmpPDF);
        }
    }

    private static void watermark(String filePath, String outputPath, String watermarkText) throws Exception {
        //创建新pdf文件
        File tmpPDF = new File(outputPath);
        //打开pdf文件
        PDDocument doc = PDDocument.load(Paths.get(filePath).toFile());
        doc.setAllSecurityToBeRemoved(true);
        //遍历pdf所有页
        for (PDPage page : doc.getPages()) {
            PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
            //引入字体文件 解决中文汉字乱码问题
            PDFont font = PDType0Font.load(doc, new FileInputStream(ResourceUtils.getFile("classpath:Alibaba-PuHuiTi-Medium.ttf").getAbsolutePath()), true);
            float fontSize = 30;
            PDResources resources = page.getResources();
            PDExtendedGraphicsState r0 = new PDExtendedGraphicsState();
            // 水印透明度
            r0.setNonStrokingAlphaConstant(0.2f);
            r0.setAlphaSourceFlag(true);
            cs.setGraphicsStateParameters(r0);
            //水印颜色
            cs.setNonStrokingColor(200, 0, 0);

            cs.beginText();
            cs.setFont(font, fontSize);
            //根据水印文字大小长度计算横向坐标需要渲染几次水印
            float h = watermarkText.length() * fontSize;
            for (int i = 0; i <= 10; i++) {
                // 获取旋转实例
                cs.setTextMatrix(Matrix.getRotateInstance(-150, i * 100, 0));
                cs.showText(watermarkText);
                for (int j = 0; j < 20; j++) {
                    cs.setTextMatrix(Matrix.getRotateInstance(-150, i * 100, j * h));
                    cs.showText(watermarkText);
                }
            }
            cs.endText();
            cs.restoreGraphicsState();
            cs.close();
        }
        doc.save(tmpPDF);
    }

    /***
     * 添加水印文字，铺满页面
     * @param inputFile
     * @param outputFile
     * @param waterMarkName
     * @date 2022/10/30
     **/

    public static void waterMarkFill(String inputFile, String outputFile, String waterMarkName) {
        try {
            PdfReader reader = new PdfReader(inputFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                    outputFile));

            BaseFont base = BaseFont.createFont(ResourceUtils.getFile("classpath:Alibaba-PuHuiTi-Medium.ttf").getAbsolutePath()
                    , BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            com.itextpdf.text.Rectangle pageRect = null;
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.1f);
            gs.setStrokeOpacity(0.2f);
            int total = reader.getNumberOfPages() + 1;

            JLabel label = new JLabel();
            FontMetrics metrics;
            int textH = 0;
            int textW = 0;
            label.setText(waterMarkName);
            metrics = label.getFontMetrics(label.getFont());
            textH = metrics.getHeight();
            textW = metrics.stringWidth(label.getText());

            int interval = -5;

            PdfContentByte under;
            for (int i = 1; i < total; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                under = stamper.getOverContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                under.setFontAndSize(base, 10);

                // 水印文字成30度角倾斜
                //你可以随心所欲的改你自己想要的角度
                for (int height = interval + textH; height < pageRect.getHeight(); height = height + textH) {
                    for (int width = interval + textW; width < pageRect.getWidth() + textW; width = width + textW) {
                        under.showTextAligned(Element.ALIGN_LEFT, waterMarkName, width - textW, height - textH, 30);
                    }
                }
                // 添加水印文字
                under.endText();
            }
            //说三遍
            //一定不要忘记关闭流
            //一定不要忘记关闭流
            //一定不要忘记关闭流
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 添加单个水印
     * @param inputFile
     * @param outputFile
     * @param waterMarkName
     * @date 2022/10/30
     **/

    public static void waterMark(String inputFile, String outputFile, String waterMarkName) {
        try {
            PdfReader reader = new PdfReader(inputFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));

            BaseFont base = BaseFont.createFont(ResourceUtils.getFile("classpath:Alibaba-PuHuiTi-Medium.ttf").getAbsolutePath()
                    , BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            PdfGState gs = new PdfGState();
            //改透明度
            gs.setFillOpacity(0.1f);
            gs.setStrokeOpacity(0.1f);
            int total = reader.getNumberOfPages() + 1;

            JLabel label = new JLabel();
            label.setText(waterMarkName);

            PdfContentByte under;
            // 添加一个水印
            for (int i = 1; i < total; i++) {
                // 在内容上方加水印
                under = stamper.getOverContent(i);
                //在内容下方加水印
                //under = stamper.getUnderContent(i);
                gs.setFillOpacity(0.5f);
                under.setGState(gs);
                under.beginText();
                //改变颜色
                under.setColorFill(BaseColor.LIGHT_GRAY);
                //改水印文字大小
                under.setFontAndSize(base, 50);
                under.setTextMatrix(70, 200);
                //后3个参数，x坐标，y坐标，角度
                under.showTextAligned(Element.ALIGN_CENTER, waterMarkName, 300, 350, 55);

                under.endText();
            }
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void useItextEncryptPdf() throws IOException, DocumentException {
        String inputPdfFilePath = "C:\\Users\\YW\\Desktop\\power-off-recovery.pdf";
        String outputPdfFilePath = "C:\\Users\\YW\\Desktop\\encrypted.pdf";

        String userPassword = "Yw@309309"; // 用户密码
        String ownerPassword = "holelin"; // 所有者密码

        try {
            PdfReader reader = new PdfReader(inputPdfFilePath);

            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPdfFilePath));
            stamper.setEncryption(userPassword.getBytes(), ownerPassword.getBytes(),
                    PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY, PdfWriter.ENCRYPTION_AES_128);

            stamper.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private static void useItextCratePdfTest() throws FileNotFoundException, DocumentException {
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
