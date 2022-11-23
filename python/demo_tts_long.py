#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import requests
import asyncio
import sys
import json
import logging
import os
import time
import argparse
from progress.bar import Bar
from urllib.parse import urlencode

from client_auth_secret import get_signature_flytek, get_signature_yitu


async def main():
    global args

    parser = argparse.ArgumentParser(description="ASR Server offline audio file demo",
                                     formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument('-u', '--url', type=str, metavar='URL',
                        help='server url', default='ai.abcpen.com')
    parser.add_argument('-l', '--log_path', type=str, metavar='LOG',
                        help='log file path', default='asr_res.log')

    args = parser.parse_args()
    
    ## 下面的app_id 和api_key仅供测试使用，生产环境请向商务申请(手机：18605811078, 邮箱：jiaozhu@abcpen.com)
    app_id = ""
    api_key = ""
    if (len(app_id)<=0 or len(api_key)<=0):
        print("Please apply appid and appsecret, demo will exit now")
        sys.exit(1)
    timestamp = str(int(time.time()))

    signa = get_signature_yitu(timestamp, app_id, api_key)
    querys = {
        "ts": timestamp,
        "appid": app_id,
        "signa": signa,
        "content": "满足用户高性价比需求的消费场景；通过流量集中分发，构成好品质加极致价格力的特卖场，满足极速版商城用户的购物需求，提高用户粘性，形成用户‘日销好货’的心智",
        "audioType": 5

    }
    url = "http://{}/v2/tts/long".format(args.url)
    response = requests.post(url, querys)
    print(response.text)

    response_json = json.loads(response.text)


if __name__ == "__main__":
    try:
        asyncio.run(main())
    except Exception as e:
        logging.info("Got ctrl+c exception-2: %s, exit process", repr(e))
