#!/usr/bin/env python
# -*- coding: utf-8 -*-

import requests
import json

import requests
import json
import time
import argparse
import hashlib
import hmac
import time
import base64
from loguru import logger


def get_signature(ts, app_id, app_secret):
    tt = (app_id + ts).encode("utf-8")
    md5 = hashlib.md5()
    md5.update(tt)
    baseString = md5.hexdigest()
    baseString = bytes(baseString, encoding="utf-8")

    apiKey = app_secret.encode("utf-8")
    signa = hmac.new(apiKey, baseString, hashlib.sha1).digest()
    signa = base64.b64encode(signa)
    signa = str(signa, "utf-8")
    return signa


# 下面的app_id 和api_key仅供测试使用，生产环境请向商务申请(手机：18605811078, 邮箱：jiaozhu@abcpen.com)
app_id = "test1"
app_secret = "2258ACC4-199B-4DCB-B6F3-C2485C63E85A"


def test_tts_short():
    # 设置API接口的URL
    url = "https://tts-dev.abcpen.com/v3/tts/emotion1/gen"

    emotions = [
        "neutral",
        "happy",
        "angry",
        "sad",
        "fear",
        "hate",
        "surprise",
        "arousal",
    ]

    timestamp = str(int(time.time()))

    text = f"从外界对费玉清的一贯印象来看，他不仅是一位音乐天才，更是一个内心深沉、低调谦和的人。凭借数十年的坚守和卓越才华，他被誉为娱乐圈的楷模"
    signa = get_signature(timestamp, app_id, app_secret)
    # signa, timestamp = generate_signature(app_id, app_secret)
    query_post_apply = {
        "ts": timestamp,
        "appid": app_id,
        "signa": signa,
        "text": text,
        "emotion": "开心",
    }

    # 使用requests的post方法发送请求
    response = requests.post(url, data=query_post_apply)

    # 检查响应状态码
    assert response.status_code == 200

    # 打印响应内容
    # logger.info(f"for {emotion}, result -> {response.content}")
    logger.info(f"result -> {response.content}")


if __name__ == "__main__":
    try:
        test_tts_short()
    except KeyboardInterrupt as err:
        logger.warning(f"exception: {err}")
    except Exception as err:
        logger.error(f"exception: {err}")
