package com.abcpen.ai.servicce;

import com.abcpen.ai.callback.AsrResultCallBack;
import com.abcpen.ai.exception.ZMeetException;
import com.abcpen.ai.mo.EngineConfig;
import com.abcpen.ai.mo.tts.TTSRequestV2;
import com.abcpen.ai.mo.asr.AsrRequest;
import com.abcpen.ai.mo.asr.AsrResult;
import com.abcpen.ai.mo.asr.RecognizeMo;
import com.abcpen.ai.mo.tts.TTSResp;
import com.abcpen.ai.mo.tts.TTSResult;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class AbcAsrImplTest {


    AsrService asrService;



    @Before
    public void init() {
        asrService = new AbcAsrServiceImpl(Cons.penClient);
    }

    @Test
    public void recognizeSync() throws ZMeetException {
        AsrResult asrResult = asrService.recognizeSync(new AsrRequest().setAudioUrl("https://zos.abcpen.com/test/asr/7ec28385-5bab-4ff2-bc8f-1b2b8fc15fbf.wav"));
        for (RecognizeMo recognizeItemMo : asrResult.getRecognizeItemMos()) {
            log.info("[{}-{}] -> {} : {}", recognizeItemMo.getStartTime(), recognizeItemMo.getEndTime(), recognizeItemMo.getSpeakerTag(), recognizeItemMo.getWord());
        }
        String ttsWord = asrResult.getRecognizeItemMos().stream().map(new Function<RecognizeMo, String>() {
            @Override
            public String apply(RecognizeMo recognizeMo) {
                return recognizeMo.getWord();
            }
        }).collect(Collectors.joining());



    }

    @Test
    public void recognize() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        asrService.recognize(new AsrRequest().setAudioUrl("https://zos.abcpen.com/test/asr/7ec28385-5bab-4ff2-bc8f-1b2b8fc15fbf.wav"), new AsrResultCallBack<AsrResult>() {
            @Override
            public void onCompleted(AsrResult asrResult) {
                for (RecognizeMo recognizeItemMo : asrResult.getRecognizeItemMos()) {
                    log.info(recognizeItemMo.toString());
                }
                countDownLatch.countDown();
            }

            @Override
            public void onFail(ZMeetException exception) {
                log.error("onFail", exception);
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();

    }
}