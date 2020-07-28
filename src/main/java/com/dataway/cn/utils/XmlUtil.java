package com.dataway.cn.utils;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

/**
 * @author Phil
 */
public class XmlUtil {
    private static final Logger logger = LoggerFactory.getLogger(XmlUtil.class);

    /**
     * org.dom4j.Document 转 Map
     * @param e: dom的节点
     * @return java.util.Map<String,Object>
     */
    public static Map<String,Object> dom2Map(Element e){
        Map<String,Object> map = new HashMap<>(16);
        List<Element> list = e.elements();
        if(list.size() > 0){
            for (Object o : list) {
                Element iter = (Element) o;
                List<Object> mapList = new ArrayList<>();
                if (iter.elements().size() > 0) {
                    Map<String,Object> m = dom2Map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!"java.util.ArrayList".equals(obj.getClass().getName())) {
                            mapList = new ArrayList<>();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if ("java.util.ArrayList".equals(obj.getClass().getName())) {
                            mapList = CastUtil.castList(obj,Object.class);
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!"java.util.ArrayList".equals(obj.getClass().getName())) {
                            mapList = new ArrayList<>();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if ("java.util.ArrayList".equals(obj.getClass().getName())) {
                            mapList = CastUtil.castList(obj,Object.class);
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), iter.getText());
                    }
                }
            }
        }else {
            map.put(e.getName(), e.getText());
        }
        return map;
    }

    /**
     * 格式化xml
     * @param xml:需要格式化的target
     * @return xmlString
     */
    public static String formatXml(String xml) {
        String requestXml = null;
        try {
            // 拿取解析器
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(xml));
            if (null != document) {
                StringWriter stringWriter = new StringWriter();
                // 格式化,每一级前的空格
                OutputFormat format = new OutputFormat("    ", true);
                // xml声明与内容是否添加空行
                format.setNewLineAfterDeclaration(false);
                // 是否设置xml声明头部
                format.setSuppressDeclaration(false);
                // 是否分行
                format.setNewlines(true);
                XMLWriter writer = new XMLWriter(stringWriter, format);
                writer.write(document);
                writer.flush();
                writer.close();
                requestXml = stringWriter.getBuffer().toString();
            }
            return requestXml;
        } catch (Exception e) {
            logger.error("格式化xml，失败 -->"+e.getMessage());
            return null;
        }
    }

    /**
     * Map转XML
     * @param map: 需要转换的Map
     * @param sb:可以new一个StringBuffer
     * @return String
     */
    public static String mapToXml(Map<?, ?> map, StringBuffer sb) {
        Set<?> set = map.keySet();
        for (Object o : set) {
            String key = (String) o;
            Object value = map.get(key);
            if (value instanceof HashMap) {
                sb.append("<").append(key).append(">");
                mapToXml((HashMap<?, ?>) value, sb);
                sb.append("</").append(key).append(">");
            }else if (value instanceof ArrayList) {
                List<?> list = (ArrayList<?>) map.get(key);
                for (Object item : list) {
                    sb.append("<").append(key).append(">");
                    Map<?, ?> hm = (HashMap<?, ?>) item;
                    mapToXml(hm, sb);
                    sb.append("</").append(key).append(">");
                }
            } else {
                sb.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
            }
        }
        return sb.toString();
    }

    /**
     * XML转Map
     * @param xml：String类型的xml
     * @return java.util.Map<String, Object>
     */
    public static Map<String, Object> xmlToMap(String xml) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        // 获取根节点
        assert doc != null;
        Element rootElt = doc.getRootElement();
        return dom2Map(rootElt);
    }

    /**
     * 创建一个xml节点
     * @param nodeName:节点名称
     * @param nodeValue:节点值
     * @return String节点
     */
    public static String createNode(String nodeName ,String nodeValue){
        String out = "";
        if(nodeName == null || "".equals(nodeName)){
            logger.error("创建XML节点失败!节点名称不能为空!");
            return out;
        }
        if(nodeValue == null){
            nodeValue = "";
        }
        out = "<"+nodeName +">"+nodeValue+"</"+nodeName+">";
        return out;
    }

    /**
     * 测试方法
     * @param args：
     */
    public static void main(String[] args) {

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>123</root>";
        Map<String,Object> map = new LinkedHashMap<>(16);
        map.put("1",1);
        map.put("2",1);
        map.put("3",1);

        Map<String,Object> map1 = new HashMap<>();
        map1.put("map",map);
        System.out.println(mapToXml(map1,new StringBuffer()));

        System.out.println(xmlToMap(xml));
    }

}
