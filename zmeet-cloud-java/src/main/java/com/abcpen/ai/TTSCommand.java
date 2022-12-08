package com.abcpen.ai;

import com.abcpen.ai.exception.ZMeetException;
import com.abcpen.ai.mo.tts.TTSRequestV1;
import com.abcpen.ai.mo.tts.TTSRequestV2;
import com.abcpen.ai.mo.tts.TTSResult;
import com.abcpen.ai.servicce.TTSService;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.abcpen.ai.Main.helpFormatter;

/**
 * @projectName: tts
 * @className: TTSArgs
 * @author: ZhaoCheng
 * @description:
 * @date: 2022/12/8 5:24 PM
 * @version: 1.0
 */
public class TTSCommand {

    static final Logger log = LoggerFactory.getLogger(TTSCommand.class);


    private static final Map<String, String> v2SpidMapping = new HashMap<>();

    static {
        v2SpidMapping.put("0", "yunxiao");
        v2SpidMapping.put("1", "yunni");
        v2SpidMapping.put("2", "yunbei");
        v2SpidMapping.put("3", "yunyang");
        v2SpidMapping.put("4", "yunxia");
        v2SpidMapping.put("5", "yunxi");
        v2SpidMapping.put("6", "yunjian");
        v2SpidMapping.put("7", "yunyi");
        v2SpidMapping.put("8", "yunxiao");
        v2SpidMapping.put("9", "yunni");
        v2SpidMapping.put("10", "yunbei");
        v2SpidMapping.put("11", "yunyang");
        v2SpidMapping.put("12", "yunxia");
        v2SpidMapping.put("13", "yunxi");
        v2SpidMapping.put("14", "yunjian");
    }

    public static final String TTS_LABEL = "tts";

    public static boolean checkIsTTS(CommandLine commandLine) {
        return commandLine.hasOption("tts");
    }

    public static Options addTTSOptions(Options options) {
        return options
                .addOption(new Option("at", "at", true, "audio_type" +
                        "可不填，不填时默认为 3。\n" +
                        "audioType=3 返回 16K 采样率的 mp3\n" +
                        "audioType=4 返回 8K 采样率的 mp3\n" +
                        "audioType=5 返回 24K 采样率的 mp3\n" +
                        "audioType=6 返回 48k采样率的mp3\n" +
                        "audioType=7 返回 16K 采样率的 pcm 格式\n" +
                        "audioType=8 返回 8K 采样率的 pcm 格式\n" +
                        "audioType=9 返回 24k 采样率的pcm格式\n" +
                        "audioType=10 返回 8K 采样率的 wav 格式\n" +
                        "audioType=11 返回 16K 采样率的 wav 格式"))
                .addOption(new Option("f", "file", true, "tts 以文件形式输入 文件地址"))
                .addOption(new Option("c", "content", true, "tts content 转换文本 必填"))
                .addOption(new Option("sid", "spkid", true, "tts spk id 转换语音包id " +
                        "\n====================\n" +
                        "0：女声（柔和）；\n" +
                        "1，女声（正式）；\n" +
                        "2，女生（柔和带正式）；\n" +
                        "3：男声（柔和）\n" +
                        "4：男声（柔和带正式）；\n" +
                        "5：男声（闽南话）;\n" +
                        "6：女生（闽南话）。\n" +
                        "7. yunxiao 云霄\n" +
                        "8. yunni 云妮 \n" +
                        "9. yunbei 云北\n" +
                        "10. yunyang 云阳\n" +
                        "11. yunxia 云夏\n" +
                        "12. yunxi 云曦\n" +
                        "13. yunjian 云剑\n" +
                        "14. yunyi 云逸\n" +
                        "\n====================\n"))
                .addOption("speed", "speed", true, "语速 0.75-1.25 可以不设置,默认是 1.0")
                .addOption("volume", "volume", true, "音量 0.75-1.25 可以不设置,默认是 1.0")
                .addOption(new Option("v", "version", true, "api 使用版本  默认为v1 可选择 v2"));
    }

    public static TTSResult progress(CommandLine cli, TTSService ttsService) throws ZMeetException, IOException {

        if (cli.hasOption("help")) {
            helpFormatter.printHelp("tts 使用方式", addTTSOptions(new Options()));
            return null;
        }

        String content = cli.getOptionValue("c");

        if (StringUtils.isBlank(content)) {
            String file = cli.getOptionValue("f");
            if (StringUtils.isNotBlank(file)) {
                content = FileUtils.readFileToString(new File(file));

            }
        }

        if (StringUtils.isBlank(content)) {
            helpFormatter.printHelp("-c 输入文本 or -f 文件地址 ", addTTSOptions(new Options()));
            return null;
        }


        Integer audioType = Integer.parseInt(cli.getOptionValue("at", "3"));
        String sid = cli.getOptionValue("sid", "0");
        String version = cli.getOptionValue("v", "v1");
        float speed = Float.parseFloat(cli.getOptionValue("speed", "1.0"));
        float volume = Float.parseFloat(cli.getOptionValue("volume", "1.0"));

        if (StringUtils.equals(version, "v1")) {
            log.info("========use v1 api tts========");
            TTSRequestV1 ttsRequestV1 = new TTSRequestV1().setContent(content)
                    .setSpkid(Integer.parseInt(sid)).setSpeed(speed).setVolume(volume)
                    .setAudioType(audioType);
            log.info("args is {}", ttsRequestV1.toString());

            return ttsService.ttsV1(ttsRequestV1);
        } else {
            log.info("========use v2 api tts========");
            String model = v2SpidMapping.get(sid);
            TTSRequestV2 ttsRequestV2 = new TTSRequestV2().setContent(content)
                    .setModel(model)
                    .setSpeed((int) (speed * 100))
                    .setVolume((int) (volume * 100))
                    .setAudioType(audioType);
            return ttsService.ttsV2(ttsRequestV2);
        }

    }
}
