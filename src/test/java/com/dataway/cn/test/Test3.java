package com.dataway.cn.test;

import com.alibaba.excel.EasyExcel;
import com.dataway.cn.utils.FileUtil;
import com.dataway.cn.utils.SecurityUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

/**
 * @author phil
 * @date 2020/05/23 13:52
 */
public class Test3 {
    public static Integer tx(String pa){
        return 4;
    }

    @Test
    public void test(){

        TreeMap<String,Object> treeMap = new TreeMap<>();
        treeMap.put("1",123);
        treeMap.put("3",123);
        treeMap.put("2",123);
        treeMap.put("4",123);
        System.out.println(treeMap);

        LinkedHashMap<String,Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("1",123);
        linkedHashMap.put("3",123);
        linkedHashMap.put("2",123);
        linkedHashMap.put("4",123);
        System.out.println(linkedHashMap);


        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("1",123);
        hashMap.put("3",123);
        hashMap.put("2",123);
        hashMap.put("4",123);
        System.out.println(hashMap);
    }

    /**
     * 最简单的读
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
     * <p>3. 直接读即可
     */
    @Test
    public void simpleRead() {
        String fileName = FileUtil.getPath() + "temp" + File.separator + "demo.xlsx";
        System.out.println(fileName);
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet().doRead();
    }

    /**
     * 最简单的写
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 直接写即可
     */
    @Test
    public void simpleWrite() {
        String fileName = FileUtil.getPath() + "write" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去读，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, DemoData.class).sheet("模板")
                .doWrite(data());
    }

    private List<DemoData> data() {
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    @Test
    public void md5Test(){
        System.out.println("===========================");
        String data1 = SecurityUtil.stringToMd5("中文");
        String data2 = SecurityUtil.stringToMd5("中文");
        String data3 = SecurityUtil.stringToMd5("English");
        String data4 = SecurityUtil.stringToMd5("English");
        System.out.println(data1);
        System.out.println(data2);
        System.out.println(data3);
        System.out.println(data4);
    }

}
