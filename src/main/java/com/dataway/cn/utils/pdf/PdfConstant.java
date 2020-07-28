package com.dataway.cn.utils.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author phil
 * @date 2020/7/23 0023 15:32
 */
public class PdfConstant {
    private static final Logger logger = LoggerFactory.getLogger(PdfConstant.class);

    /**
     * 基础字体
     */
    private static BaseFont bfChinese;

    //初始化字体
    static {
        try {
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            logger.error("初始化字体失败！");
        }
    }

    /**
     * 定义全局的字体静态变量,标题字体
     */
    public static final Font TITLE_FONT = new Font(bfChinese, 16, Font.BOLD);
    /**
     * 页眉字体
     */
    public static final Font HEAD_FONT = new Font(bfChinese, 14, Font.BOLD);
    /**
     * 页脚字体
     */
    public static final Font BOTTOM_FONT = new Font(bfChinese, 14, Font.BOLD);
    /**
     * 关键字字体
     */
    public static final Font KEY_FONT = new Font(bfChinese, 10, Font.BOLD);
    /**
     * 正文字体
     */
    public static final Font TEXT_FONT = new Font(bfChinese, 10, Font.NORMAL);
    /**
     * 最大宽度
     */
    public static final int MAX_WIDTH = 520;

}
