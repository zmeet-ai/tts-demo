package com.abcpen.ai;

import com.abcpen.ai.exception.ZMeetException;
import com.abcpen.ai.mo.asr.AsrRequest;
import com.abcpen.ai.mo.asr.AsrResult;
import com.abcpen.ai.mo.tts.TTSRequestV1;
import com.abcpen.ai.mo.tts.TTSRequestV2;
import com.abcpen.ai.mo.tts.TTSResult;
import com.abcpen.ai.servicce.AsrService;
import com.abcpen.ai.servicce.TTSService;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

import static com.abcpen.ai.Main.helpFormatter;

/**
 * @projectName: tts
 * @className: AsrArgs
 * @author: ZhaoCheng
 * @description: TODO
 * @date: 2022/12/8 5:24 PM
 * @version: 1.0
 */
public class AsrCommand {

    public static final String ASR_LABEL = "asr";

    public static Options addOptions(Options options) {
        return options
                .addOption(new Option("audio", "audio", true, "音频文件地址"));
    }


    public static boolean check(CommandLine commandLine) {
        return commandLine.hasOption("asr");
    }

    public static AsrResult progress(CommandLine cli, AsrService asrService) throws ZMeetException, IOException {

        if (cli.hasOption("help")) {
            helpFormatter.printHelp("asr 使用方式", addOptions(new Options()));
            return null;
        }

        String audio = cli.getOptionValue("audio");
        if (StringUtils.isBlank(audio)){
            helpFormatter.printHelp("-audio 缺少音频文件地址", addOptions(new Options()));
            return null;
        }

        return asrService.recognizeSync(new AsrRequest().setAudioUrl(audio));
    }
}
