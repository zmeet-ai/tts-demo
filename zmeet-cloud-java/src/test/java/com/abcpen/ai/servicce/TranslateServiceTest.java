package com.abcpen.ai.servicce;

import com.abcpen.ai.exception.ZMeetException;
import com.abcpen.ai.mo.translate.TranslateModel;
import com.abcpen.ai.mo.translate.TranslateRequest;
import com.abcpen.ai.mo.translate.TranslateResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@Slf4j
public class TranslateServiceTest {


    private TranslateService translateService;

    @Before
    public void setUp() throws Exception {
        translateService = new AbcPenTranslateServiceImpl(Cons.penClient);
    }

    @Test
    public void translate() throws ZMeetException {
        TranslateResult translate = translateService.translate(new TranslateRequest().setMode(TranslateModel.ZH_EN).setText("当我以为苍蓝绝就这样永远完结的时候，我却在江西上饶望仙谷回到了故事的开头"));
        log.info(translate.toString());
    }
}