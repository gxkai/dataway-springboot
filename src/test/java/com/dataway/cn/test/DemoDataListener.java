package com.dataway.cn.test;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

/**
 * @author phil
 * @date 2020/07/27 11:15
 */
public class DemoDataListener extends AnalysisEventListener<DemoData>  {
    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println(demoData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        System.out.println("读完");
    }
}
