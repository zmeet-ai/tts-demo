let rp = require('request-promise');
const crypto = require('crypto');
const CryptoJS = require('crypto-js')
const moment = require('moment');

function get_sign_zmeet(appId, appSecret, timestamp) {
    let combined = appId + timestamp;
    let hashStr = crypto.createHmac('sha256', appSecret).update(combined).digest("hex");

    return hashStr.toLowerCase();
};

function get_sign_flytek(appId, appSecret, ts) {
    let md5 = CryptoJS.MD5(appId + ts).toString()
    let sha1 = CryptoJS.HmacSHA1(md5, appSecret)
    let base64 = CryptoJS.enc.Base64.stringify(sha1)
    return encodeURIComponent(base64)
}

async function ttsDemo(content, appKey, timestamp, sign) {
    let resTts = {
        desc: "",
        code: "",
        data: ""
    };

    let bodyRes, jsonData;
    let options;
    {
        jsonData = {
            "ts": moment().unix(),
            "appid": appKey,
            "signa": sign,
            content: content,
            audioType: 5
        };
        options = {
            method: 'POST',
            uri: 'https://ai.abcpen.com/v2/tts/long',
            form: jsonData,
            timeout: 5000,
            forever: true
        }
    }
    try {
        let ts = Date.now();
        bodyRes = await rp(options);
        console.info("tts --------------------------------->", content, bodyRes, typeof bodyRes, ", Duration: ", (Date.now() - ts) + "ms");
        if (bodyRes && bodyRes.length > 0) {
            bodyRes = JSON.parse(bodyRes);
        }
    } catch (err) {
        console.log("tts request error: ", err);
        return resTts;
    }
    return bodyRes;
};

(async () => {
    let appKey = ""
    let appSecret = ""
    if (appKey.length <= 0 || appSecret <= 0) {
        console.log("请向商务申请开发者密钥！");
        process.exit(0);
    }
    console.log("演示tts v2版本：", appKey, appSecret)

    let timestamp = moment().unix();
    let signa = get_sign_zmeet(appKey, appSecret, timestamp)

    console.log("sign sha256 is: ", signa);
    let tts2 = await ttsDemo("我是人工智能小芯，我是全世界最好听的语音！",
                appKey, timestamp, signa);
    console.log("tts2: ", tts2);

})();
