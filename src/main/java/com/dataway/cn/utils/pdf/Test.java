package com.dataway.cn.utils.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author phil
 * @date 2020/07/23 15:59
 */
public class Test {

    /**
     * Pdf 测试类
     * @param args ：
     * @throws Exception：
     */
    public static void main(String[] args) throws Exception {
        testText();
        testTable();
    }
    /**
     * 测试
     */
    public static void testTable(){
        try {
            // 1.新建document对象  建立一个Document对象
            Document document = new Document(PageSize.A4);

            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File("E:\\PDFDemo.pdf");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            // 水印
            writer.setPageEvent(new PdfWaterMark("HELLO ITEXTPDF"));
            // 页眉/页脚
            writer.setPageEvent(new PdfHeaderFooterUtil());

            // 设置密码为：用户密码，打开密码
            writer.setEncryption("123".getBytes(), "456".getBytes(),
                    PdfWriter.ALLOW_SCREENREADERS,
                    PdfWriter.STANDARD_ENCRYPTION_128);

            // 3.打开文档
            document.open();
            // 标题
            document.addTitle("Title@PDF-Java");
            // 作者
            document.addAuthor("Author@umiz");
            // 主题
            document.addSubject("Subject@iText pdf sample");
            // 关键字
            document.addKeywords("Keywords@iTextpdf");
            // 创建者
            document.addCreator("Creator@umiz`s");

            // 4.向文档中添加内容
            new PdfUtil().generatePdf(document);

            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testText() throws Exception {
        PdfUtil pdfUtil = new PdfUtil(80, 80, 120, 120,"E:\\test.pdf");
        pdfUtil.addParagraph(1,24,new BaseColor(179, 91, 68), Font.NORMAL,0,"我是0个测试案例--居左");
        pdfUtil.newLine(10);
        pdfUtil.addParagraph(1,24,BaseColor.GREEN,Font.NORMAL,1,"我是1个测试案例--居中 空行");
        pdfUtil.addParagraph(1,24,new BaseColor(140, 109, 179),Font.NORMAL,2,"我是2个测试案例--居右");
        pdfUtil.addPage();
        pdfUtil.addParagraph(1,24,BaseColor.RED,Font.NORMAL,1,"我是3个测试案例--新增页");
        pdfUtil.addParagraph(1,24,new BaseColor(179, 91, 68),Font.NORMAL,0,"我是0个测试案例--居左");
        pdfUtil.addTextIndent(1,24,new BaseColor(41, 124, 179),Font.NORMAL,28,"我是3个测试案例--首行缩进");
        pdfUtil.close();
    }

}
