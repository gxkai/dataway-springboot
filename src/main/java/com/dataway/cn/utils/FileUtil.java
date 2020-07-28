package com.dataway.cn.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

/**
 * File工具类
 * @author phil
 * @date 2020/06/20 13:19
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static InputStream getResourcesFileInputStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("" + fileName);
    }

    public static String getPath() {
        return FileUtil.class.getResource("/").getPath();
    }

    public static File createNewFile(String pathName) {
        File file = new File(getPath() + pathName);
        if (file.exists()) {
            file.deleteOnExit();
        } else {
            if (!file.getParentFile().exists()) {
                boolean flag = file.getParentFile().mkdirs();
                if (flag){
                    logger.info("成功创建文件夹:"+file.getParentFile());
                }else{
                    logger.error("创建文件夹失败！");
                }
            }
        }
        logger.info("成功创建文件："+file.getParentFile()+"/"+file.getName());
        return file;
    }

    public static File readFile(String pathName) {
        return new File(getPath() + pathName);
    }

    public static File readUserHomeFile(String pathName) {
        return new File(System.getProperty("user.home") + File.separator + pathName);
    }

    public static void main(String[] args) {
        System.out.println(File.separator);
    }
}
