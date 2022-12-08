package com.abcpen.ai;

import com.abcpen.ai.exception.ZMeetException;
import com.abcpen.ai.mo.EngineConfig;
import com.abcpen.ai.mo.asr.AsrResult;
import com.abcpen.ai.mo.asr.RecognizeMo;
import com.abcpen.ai.mo.tts.TTSResult;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @projectName: tts
 * @className: Main
 * @author: ZhaoCheng
 * @description:
 * @date: 2022/12/7 6:01 PM
 * @version: 1.0
 */
public class Main {
    static final Logger log = LoggerFactory.getLogger(Main.class);

    public static final HelpFormatter helpFormatter = new HelpFormatter();

    public static void main(String[] args) throws ZMeetException {

        Options options = new Options();
        addAuthOptions(options);


        CommandLineParser cliParser = new DefaultParser();

        options.addOption("h", "help", false, "查看参数")
                .addOption(new Option(TTSCommand.TTS_LABEL, TTSCommand.TTS_LABEL, false, "使用语音转换"))
                .addOption(new Option(AsrCommand.ASR_LABEL, AsrCommand.ASR_LABEL, false, "使用asr识别"));

        TTSCommand.addTTSOptions(options);

        AsrCommand.addOptions(options);

        CommandLine cli = null;
        try {
            cli = cliParser.parse(options, args);
            EngineConfig engineConfig = parseAuthOptions(cli);
            ZMeetAiTemplate aiTemplate = new ZMeetAiTemplate(engineConfig);

            if (TTSCommand.checkIsTTS(cli)) {
                TTSResult progress = TTSCommand.progress(cli, aiTemplate.getTtsService());
                if (progress != null) {
                    log.info("tts result:{}", progress);
                    System.exit(0);
                }
            }

            if (AsrCommand.check(cli)) {
                AsrResult progress = AsrCommand.progress(cli, aiTemplate.getAsrService());
                if (progress != null) {
                    printAsrResult(progress);
                    System.exit(0);
                }
            }

        } catch (ParseException e) {
            helpFormatter.printHelp("认证信息 ", addAuthOptions(new Options())
                    .addOption(TTSCommand.TTS_LABEL, "tts", false, "使用tts -tts -help 查看")
                    .addOption(AsrCommand.ASR_LABEL, AsrCommand.ASR_LABEL, false, "使用asr  -asr -help 查看")
            );
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static String printAsrResult(AsrResult progress) {
        for (RecognizeMo recognizeItemMo : progress.getRecognizeItemMos()) {
            log.info("时间 [{}-{}] 说话人:{} 结果:{}",recognizeItemMo.getStartTime(),recognizeItemMo.getEndTime(),recognizeItemMo.getSpeakerTag(),recognizeItemMo.getWord());
        }
        return null;
    }

    private static EngineConfig parseAuthOptions(CommandLine cli) {
        String appId = cli.getOptionValue("a");
        String secret = cli.getOptionValue("s");
        String endPoint = cli.getOptionValue("e", "ai.abcpen.com");
        return new EngineConfig()
                .setAppid(appId)
                .setSecret(secret)
                .setEndpoint(endPoint);
    }


    private static Options addAuthOptions(Options options) {
        Option appId = new Option("a", "appId", true, "appId");
        appId.setRequired(true);
        Option secret = new Option("s", "secret", true, "app Secret");
        secret.setRequired(true);
        Option endpoint = new Option("e", "endpoint", true, "endpoint 为对应域名地址 \n列如 ai.abcpen.com 私有化部署的请填写 自己对应域名\n");
        options.addOption(appId).addOption(secret).addOption(endpoint);
        return options;
    }


}
