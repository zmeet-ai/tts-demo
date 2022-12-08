package com.abcpen.ai.servicce;

import com.abcpen.ai.mo.EngineConfig;

/**
 * @projectName: tts
 * @className: Cons
 * @author: ZhaoCheng
 * @description: TODO
 * @date: 2022/12/8 4:20 PM
 * @version: 1.0
 */
public class Cons {
    public static AbcPenClient penClient = new AbcPenClient(new EngineConfig()
            .setAppid("test1")
            .setSecret("2258ACC4-199B-4DCB-B6F3-C2485C63E85A")
            .setEndpoint("ai.abcpen.com"));
}
