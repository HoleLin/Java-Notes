package cn.holelin.files.utils;

import cn.hutool.core.io.IoUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;

/**
 * @author yanjiaqing
 * @date 2022/10/30
 **/
@Slf4j
public class PdfUtils {

    /***
     * 获取输入文件流
     * @param
     * @return {@link ByteArrayOutputStream}
     * @date 2022/10/30
     **/

    public static ByteArrayOutputStream getInputStream() {
        InputStream ins = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            //读取文件
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            org.springframework.core.io.Resource[] resources = resolver.getResources("verify" + File.separator + "verify.pdf");
            org.springframework.core.io.Resource resource = resources[0];
            ins = resource.getInputStream();

            byteArrayOutputStream = new ByteArrayOutputStream();
            int bytesRead;
            int len = 8192;
            byte[] buffer = new byte[len];
            while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("获取文件异常");
//            throw new BusinessException(e.getMessage());
        } finally {
            IoUtil.close(ins);
            IoUtil.close(byteArrayOutputStream);
        }
        return byteArrayOutputStream;
    }

    /***
     * 使用map中的参数填充pdf，map中的key和pdf表单中的field对应
     * @param fieldValueMap
     * @param inputStream
     * @return {@link ByteArrayOutputStream}
     * @date 2022/10/30
    **/

    public static ByteArrayOutputStream fillParam(Map<String, String> fieldValueMap, byte[] inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        PdfReader reader = null;
        PdfStamper stamper = null;
        BaseFont base = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            reader = new PdfReader(inputStream);
            stamper = new PdfStamper(reader, byteArrayOutputStream);
            stamper.setFormFlattening(true);
            base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            AcroFields acroFields = stamper.getAcroFields();
            for (String key : acroFields.getFields().keySet()) {
                acroFields.setFieldProperty(key, "textfont", base, null);
                acroFields.setFieldProperty(key, "textsize", new Float(12), null);
            }
            if (fieldValueMap != null) {
                for (String fieldName : fieldValueMap.keySet()) {
                    acroFields.setField(fieldName, fieldValueMap.get(fieldName));
                }
            }
        } catch (Exception e) {
            log.error("填充参数异常2: {}", e.getMessage());
//            throw new BusinessException(e.getMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (Exception e) {
                    log.error("流关闭失败 stamper : {}", e.getMessage());
//                        throw new BusinessException(e.getMessage());
                }
            }
        }
        return byteArrayOutputStream;
    }

    /***
     * 获取pdf表单中的fieldNames
     * @param pdfFileName
     * @return {@link Set< String>}
     * @date 2022/10/30
    **/

    public static Set<String> getTemplateFileFieldNames(String pdfFileName) {
        Set<String> fieldNames = new TreeSet<String>();
        PdfReader reader = null;
        try {
            reader = new PdfReader(pdfFileName);
            Set<String> keys = reader.getAcroFields().getFields().keySet();
            System.out.println("keys = " + keys);
            for (String key : keys) {
                int lastIndexOf = key.lastIndexOf(".");
                int lastIndexOf2 = key.lastIndexOf("[");
                fieldNames.add(key.substring(lastIndexOf != -1 ? lastIndexOf + 1 : 0, lastIndexOf2 != -1 ? lastIndexOf2 : key.length()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return fieldNames;
    }

    /***
     * 读取文件数组
     * @param filePath
     * @return {@link byte[]}
     * @date 2022/10/30
    **/

    public static byte[] fileBuff(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            //System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] file_buff = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < file_buff.length && (numRead = fi.read(file_buff, offset, file_buff.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != file_buff.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        fi.close();
        return file_buff;
    }

    /***
     * 合并pdf
     * @param files
     * @param savepath
     * @date 2022/10/30
    **/

    public static void mergePdfFiles(String[] files, String savepath) {
        Document document = null;
        try {
            document = new Document(); //默认A4大小
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(savepath));
            document.open();
            for (int i = 0; i < files.length; i++) {
                PdfReader reader = null;
                try {
                    reader = new PdfReader(files[i]);
                    int n = reader.getNumberOfPages();
                    for (int j = 1; j <= n; j++) {
                        document.newPage();
                        PdfImportedPage page = copy.getImportedPage(reader, j);
                        copy.addPage(page);
                    }
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭PDF文档流，OutputStream文件输出流也将在PDF文档流关闭方法内部关闭
            if (document != null) {
                document.close();
            }

        }
    }

    /***
     * pdf转图片
     * @param file
     * @param imageFilePath
     * @date 2022/10/30
    **/

    public static boolean pdf2Img(File file, String imageFilePath) {
        try {
            //生成图片保存
            byte[] data = pdfToPic(PDDocument.load(file));
            File imageFile = new File(imageFilePath);
            System.out.println("pdf转图片文件地址:" + imageFilePath);
            return true;
        } catch (Exception e) {
            System.out.println("pdf转图片异常：");
            e.printStackTrace();
        }

        return false;
    }

    /***
     * pdf转图片
     * @param pdDocument
     * @return {@link byte[]}
     * @author yanjiaqing
     * @date 2022/10/30
    **/

    private static byte[] pdfToPic(PDDocument pdDocument) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        java.util.List<BufferedImage> piclist = new ArrayList<BufferedImage>();
        try {
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            for (int i = 0; i < pdDocument.getNumberOfPages(); i++) {//
                // 0 表示第一页，300 表示转换 dpi，越大转换后越清晰，相对转换速度越慢
                BufferedImage image = renderer.renderImageWithDPI(i, 108);
                piclist.add(image);
            }
            // 总高度 总宽度 临时的高度 , 或保存偏移高度 临时的高度，主要保存每个高度
            int height = 0, width = 0, _height = 0, __height = 0,
                    // 图片的数量
                    picNum = piclist.size();
            // 保存每个文件的高度
            int[] heightArray = new int[picNum];
            // 保存图片流
            BufferedImage buffer = null;
            // 保存所有的图片的RGB
            List<int[]> imgRGB = new ArrayList<int[]>();
            // 保存一张图片中的RGB数据
            int[] _imgRGB;
            for (int i = 0; i < picNum; i++) {
                buffer = piclist.get(i);
                heightArray[i] = _height = buffer.getHeight();// 图片高度
                if (i == 0) {
                    // 图片宽度
                    width = buffer.getWidth();
                }
                // 获取总高度
                height += _height;
                // 从图片中读取RGB
                _imgRGB = new int[width * _height];
                _imgRGB = buffer.getRGB(0, 0, width, _height, _imgRGB, 0, width);
                imgRGB.add(_imgRGB);
            }

            // 设置偏移高度为0
            _height = 0;
            // 生成新图片
            BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            int[] lineRGB = new int[8 * width];
            int c = new Color(128, 128, 128).getRGB();
            for (int i = 0; i < lineRGB.length; i++) {
                lineRGB[i] = c;
            }
            for (int i = 0; i < picNum; i++) {
                __height = heightArray[i];
                // 计算偏移高度
                if (i != 0)
                    _height += __height;
                imageResult.setRGB(0, _height, width, __height, imgRGB.get(i), 0, width); // 写入流中

                // 模拟页分隔
                if (i > 0) {
                    imageResult.setRGB(0, _height + 2, width, 8, lineRGB, 0, width);
                }
            }
            // 写流
            ImageIO.write(imageResult, "jpg", baos);
        } catch (Exception e) {
            System.out.println("pdf转图片异常：");
            e.printStackTrace();
        } finally {
            try {
                pdDocument.close();
            } catch (Exception ignore) {
            }
        }

        return baos.toByteArray();
    }

    /***
     * 添加水印文字跟图片
     * @param bytes
     * @param outputStream
     * @param text
     * @param textWidth
     * @param textHeight
     * @param imgFile
     * @param imgWidth
     * @param imgHeight
     * @return {@link OutputStream}
     * @author yanjiaqing
     * @date 2022/10/30
    **/

    public static OutputStream addWaterMark(byte[] bytes, OutputStream outputStream, String text, int textWidth, int textHeight,
                                            String imgFile, int imgWidth, int imgHeight) throws Exception {
        // 待加水印的文件
        PdfReader reader = new PdfReader(bytes);

        // 加完水印的文件
        PdfStamper stamper = new PdfStamper(reader, outputStream);

        // 设置字体
        BaseFont font = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED); // 华文宋体

        //BaseFont font = BaseFont.createFont("C:\\Windows\\Fonts\\simhei.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); // 标准黑体

        // 水印透明度
        PdfGState gs = new PdfGState();
        gs.setFillOpacity(0.5f);
        gs.setStrokeOpacity(0.5f);

        // PDF总页数
        int total = reader.getNumberOfPages() + 1;

        // 循环对每页插入水印
        PdfContentByte content;
        for (int i = 1; i < total; i++) {

            // 水印在之前文本之上
            content = stamper.getOverContent(i);

            // 透明度
            content.setGState(gs);

            // 图片水印
            if (imgFile != null) {

                Image image = null;
                if (imgFile != null) {

                    image = Image.getInstance(imgFile);
                    image.setAbsolutePosition(imgWidth, imgHeight);

                    // 设置图片的显示大小
                    image.scaleToFit(100, 125);
                }

                content.addImage(image);
            }

            // 文字水印
            if (text != null) {

                content.beginText();

                // 设置颜色 默认为蓝色
                content.setColorFill(new BaseColor(204, 204, 204));
                //content.setColorFill(Color.BLUE);

                // 设置字体及字号
                content.setFontAndSize(font, 25);

                // 设置起始位置
                content.setTextMatrix(textWidth, textHeight);

                // 字符间距
                content.setCharacterSpacing(2);

                // 中间水印
                content.showTextAligned(Element.ALIGN_LEFT, text, textWidth, textHeight, 45);

                // 底部水印
//                for (int k = 0; k < text.length(); k++) {
//                    // 距离底边的距离
//                    content.setTextRise(10);
//
//                    // 将char转成字符串
//                    content.showText(String.valueOf(text.charAt(k)));
//                }

                content.endText();
            }
        }

        stamper.close();
        reader.close();
        return outputStream;
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

            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);

            PdfGState gs = new PdfGState();
            //改透明度
            gs.setFillOpacity(0.5f);
            gs.setStrokeOpacity(0.4f);
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
                under.setFontAndSize(base, 150);
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

            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);

            com.itextpdf.text.Rectangle pageRect = null;
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.3f);
            gs.setStrokeOpacity(0.4f);
            int total = reader.getNumberOfPages() + 1;

            JLabel label = new JLabel();
            FontMetrics metrics;
            int textH = 0;
            int textW = 0;
            label.setText(waterMarkName);
            metrics = label.getFontMetrics(label.getFont());
            textH = metrics.getHeight();
            textW = metrics.stringWidth(label.getText());

            int interval = -10;

            PdfContentByte under;
            for (int i = 1; i < total; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                under = stamper.getOverContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                under.setFontAndSize(base, 20);

                // 水印文字成30度角倾斜
                //你可以随心所欲的改你自己想要的角度
                for (int height = interval + textH; height < pageRect.getHeight(); height = height + textH * 3) {
                    for (int width = interval + textW; width < pageRect.getWidth() + textW; width = width + textW * 2) {
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

}

