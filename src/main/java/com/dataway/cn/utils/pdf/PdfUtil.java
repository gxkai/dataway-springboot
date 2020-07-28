package com.dataway.cn.utils.pdf;

import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;

/**
 * @author phil
 * @date 2020/07/23 14:35
 */
public class PdfUtil {
    private static final Logger logger = LoggerFactory.getLogger(PdfUtil.class);


    /**
     * document对象
     */
   private static Document document =  null;
 
   /**
     *  创建一个书写器，布局文本位置
     * @param leftSize 左页边距。
     * @param rightSize 右页边距。
     * @param onSize 上页边距。
     * @param underSize 下页边距。
     * @param path 存储位置
     * @throws Exception 初始化PDF错误
     */
   public PdfUtil(Integer leftSize , Integer rightSize , Integer onSize , Integer underSize, String path) throws Exception {
       try{
           // 新建document对象 第一个参数是页面大小。接下来的参数分别是左、右、上和下页边距。
           document = new Document(PageSize.A4, leftSize, rightSize, onSize, underSize);
           // 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
           PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
           // 打开文件
           document.open();
       }catch (Exception e){
           e.printStackTrace();
           logger.error("PDF初始化错误");
       }
   }

    /**
     * 无参构造
     */
   public PdfUtil(){}
 
   /**
     *  书写每一个段落选择的字体
     * @param fontType
     *             0 //楷体字
     *             1 //仿宋体
     *             2 //黑体
     *             字体需要可在追加
     * @return BaseFont
     */
   public BaseFont addFontType(Integer fontType)  {
       BaseFont baseFont = null;
       try{
 
           switch (fontType){
               case 0:
                   //楷体字
                   baseFont = BaseFont.createFont("楷体字", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                   break;
               case 1:
                   //仿宋体
                   baseFont = BaseFont.createFont("c://windows//fonts//SIMFANG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                   break;
               case 2:
                   //黑体
                   baseFont = BaseFont.createFont("c://windows//fonts//SIMHEI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                   break;
               default:
                   logger.error("暂无此字体");

           }
           return baseFont;
       }catch (Exception e){
           e.printStackTrace();
           logger.error("选择字体异常");
 
       }
       return null;
   }
 
   /**
     *  添加段落 -  段落位置（ 0 居左  1 居中 2 居右）
     * @param fontType 选择字体
     *             0 //楷体字
     *             1 //仿宋体
     *             2 //黑体
     * @param fontSize 字体大小
     * @param fontStyle 字体风格 Font.NORMAL，Font.BOLD
     * @param color 字体颜色
     * @param alignment   0 居左  1 居中 2 居右
     * @param text 文本内容
     */
   public void addParagraph(Integer fontType , Integer fontSize,BaseColor color ,Integer fontStyle ,Integer alignment ,String text){
       try{
           BaseFont chinese =addFontType(fontType);
           Font font = new Font(chinese, fontSize, fontStyle,color);
           Paragraph paragraph =new Paragraph(text,font);
           //居中显示
           paragraph.setAlignment(alignment);
           document.add(paragraph);
       }catch (Exception e){
           e.printStackTrace();
           System.out.println("添加段落异常");
       }
   }
 
   /**
     *  添加段落 -  首行缩进
     * @param fontType 选择字体
     *             0 //楷体字
     *             1 //仿宋体
     *             2 //黑体
     * @param fontSize 字体大小
     * @param color 字体颜色
     * @param index  首行缩进
     * @param text 文本内容
     */
   public void addTextIndent(Integer fontType , Integer fontSize,BaseColor color,Integer fontStyle  ,Integer index ,String text){
       try{
           BaseFont chinese =addFontType(fontType);
           Font font = new Font(chinese, fontSize, fontStyle,color);
           Paragraph paragraph =new Paragraph(text,font);
           //设置首行缩进
           paragraph.setFirstLineIndent(index);
           document.add(paragraph);
       }catch (Exception e){
           e.printStackTrace();
           System.out.println("添加段落异常");
       }
   }
 
   /**
     *  添加新的一页
     */
   public void addPage(){
       try{
         document.newPage();
       }catch (Exception e){
           e.printStackTrace();
           System.out.println("添加段落异常");
       }
   }
 
   /**
     *  换行
     *  传入1是一行，以此递增
     * @param lineNum 换的行数
     */
   public void newLine(Integer lineNum) {
       try{
           for(int i =0 ; i<lineNum ; i++){
               document.add(new Paragraph("\n"));
           }
       }catch (Exception e){
           e.printStackTrace();
           System.out.println("换行错误");
       }
   }
 
   /**
     *  关闭文档
     */
   public void close (){
 
       // 关闭文档
       document.close();
   }

    /**
     * 生成PDF文件示例
     * @param document：Document
     * @throws Exception ：
     */
    public void generatePdf(Document document) throws Exception {

        // 段落
        Paragraph paragraph = new Paragraph("美好的一天从早起开始！", PdfConstant.TITLE_FONT);
        //设置文字居中 0靠左   1，居中     2，靠右
        paragraph.setAlignment(1);
        //设置左缩进
        paragraph.setIndentationLeft(12);
        //设置右缩进
        paragraph.setIndentationRight(12);
        //设置首行缩进
        paragraph.setFirstLineIndent(24);
        //行间距
        paragraph.setLeading(20f);
        //设置段落上空白
        paragraph.setSpacingBefore(5f);
        //设置段落下空白
        paragraph.setSpacingAfter(10f);

        // 直线
        Paragraph p1 = new Paragraph();
        p1.add(new Chunk(new LineSeparator()));

        // 点线
        Paragraph p2 = new Paragraph();
        p2.add(new Chunk(new DottedLineSeparator()));

        // 超链接
        Anchor anchor = new Anchor("baidu");
        anchor.setReference("www.baidu.com");

        // 定位
        Anchor gotoP = new Anchor("goto");
        gotoP.setReference("#top");

        // 添加图片
        Image image = Image.getInstance("https://img-blog.csdn.net/20180801174617455?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zNzg0ODcxMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70");
        image.setAlignment(Image.ALIGN_CENTER);
        //依照比例缩放
        image.scalePercent(40);

        PdfTableUtil pdfTableUtil = new PdfTableUtil();
        // 表格
        PdfPTable table = pdfTableUtil.createTable(new float[]{40, 120, 120, 120, 80, 80});
        table.addCell(pdfTableUtil.createCell("美好的一天", PdfConstant.HEAD_FONT, Element.ALIGN_LEFT, 6, false));
        table.addCell(pdfTableUtil.createCell("早上9:00", PdfConstant.KEY_FONT, Element.ALIGN_CENTER));
        table.addCell(pdfTableUtil.createCell("中午11:00", PdfConstant.KEY_FONT, Element.ALIGN_CENTER));
        table.addCell(pdfTableUtil.createCell("中午13:00", PdfConstant.KEY_FONT, Element.ALIGN_CENTER));
        table.addCell(pdfTableUtil.createCell("下午15:00", PdfConstant.KEY_FONT, Element.ALIGN_CENTER));
        table.addCell(pdfTableUtil.createCell("下午17:00", PdfConstant.KEY_FONT, Element.ALIGN_CENTER));
        table.addCell(pdfTableUtil.createCell("晚上19:00", PdfConstant.KEY_FONT, Element.ALIGN_CENTER));
        Integer totalQuantity = 0;
        for (int i = 0; i < 5; i++) {
            table.addCell(pdfTableUtil.createCell("起床", PdfConstant.TEXT_FONT));
            table.addCell(pdfTableUtil.createCell("吃午饭", PdfConstant.TEXT_FONT));
            table.addCell(pdfTableUtil.createCell("午休", PdfConstant.TEXT_FONT));
            table.addCell(pdfTableUtil.createCell("下午茶", PdfConstant.TEXT_FONT));
            table.addCell(pdfTableUtil.createCell("回家", PdfConstant.TEXT_FONT));
            table.addCell(pdfTableUtil.createCell("吃晚饭", PdfConstant.TEXT_FONT));
            totalQuantity++;
        }
        table.addCell(pdfTableUtil.createCell("总计", PdfConstant.KEY_FONT));
        table.addCell(pdfTableUtil.createCell("", PdfConstant.TEXT_FONT));
        table.addCell(pdfTableUtil.createCell("", PdfConstant.TEXT_FONT));
        table.addCell(pdfTableUtil.createCell("", PdfConstant.TEXT_FONT));
        table.addCell(pdfTableUtil.createCell(totalQuantity + "件事", PdfConstant.TEXT_FONT));
        table.addCell(pdfTableUtil.createCell("", PdfConstant.TEXT_FONT));

        document.add(paragraph);
        document.add(anchor);
        document.add(p2);
        document.add(gotoP);
        document.add(p1);
        document.add(table);
        document.add(image);
    }
}
