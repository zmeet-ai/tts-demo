package com.abcpen.ai.servicce;

import com.abcpen.ai.exception.ZMeetException;
import com.abcpen.ai.mo.EngineConfig;
import com.abcpen.ai.mo.label.ExtractTxtLabelRequest;
import com.abcpen.ai.mo.label.LabelResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@Slf4j
public class LabelServiceTest {



    LabelService labelService;



    @Before
    public void init() {
        labelService = new AbcPenLabelServiceImpl(Cons.penClient);
    }


    @Test
    public void testLabel() throws ZMeetException {
        LabelResult labelResult = labelService.extractTxtLabel(new ExtractTxtLabelRequest("当我以为苍蓝绝就这样永远完结的时候，我却在江西上饶望仙谷回到了故事的开头。悬挂在崖壁上的世界，像是水云天上那颗懵懂的兰花草生活的私密店，横亘在峡谷两侧的蓝月月桥，像是沧沿海那座相思桥。许清望月遇见你一首定情的幼稚，倒也成了信念，一直风都落在水月中，十里红妆的嫁娶中，藏在回到了云梦泽那天，那位东方园外牵着她的小兰花走进洞房，花烛夜望仙往前，原来世上真有这样一个地方，依然在上演。苍蓝泉把那天上人间的故事成全，他看过身边."));
        log.info(labelResult.toString());
    }

}