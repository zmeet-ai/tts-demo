package com.abcpen.ai.servicce;

import com.abcpen.ai.exception.ZMeetException;
import com.abcpen.ai.mo.EngineConfig;
import com.abcpen.ai.mo.tts.TTSRequestV1;
import com.abcpen.ai.mo.tts.TTSRequestV2;
import com.abcpen.ai.mo.tts.TTSResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class TTSTest {

    public static AbcPenClient penClientV1 = new AbcPenClient(new EngineConfig()
            .setAppid("1545")
            .setSecret("80041d07-db1d-40fc-bdbb-df8e2a45f693")
            .setEndpoint("ai.abcpen.com"));


    TTSService ttsService;

    TTSService ttsServiceV1;

    @Before
    public void init() {
        ttsService = new AbcPenTTSServiceImpl(Cons.penClient);
        ttsServiceV1 = new AbcPenTTSServiceImpl(penClientV1);
    }


    @Test
    public void testV1() throws ZMeetException {
        TTSResult ttsResult = ttsServiceV1.ttsV1(new TTSRequestV1().setContent("2022年海南省普通高考普通类考生成绩分布表显示，900分的考生全海南省一共3名。25日，男孩母亲回应称，得知成绩时自己很开心，清华北大两所高校都已取得联系，孩子目前有些迷茫，暂未作出决定，打算先选好专业"));
        log.info(ttsResult.toString());
    }


    @Test
    public void testV2() throws ZMeetException {
        TTSResult ttsResult = ttsService.ttsV2(new TTSRequestV2().setContent("2022年海南省普通高考普通类考生成绩分布表显示，900分的考生全海南省一共3名。25日，男孩母亲回应称，得知成绩时自己很开心，清华北大两所高校都已取得联系，孩子目前有些迷茫，暂未作出决定，打算先选好专业"));
        log.info(ttsResult.toString());
    }

}