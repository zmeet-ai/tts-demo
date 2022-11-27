let rp = require('request-promise');
const crypto = require('crypto');

function verifySha256Sign(appKey, timestamp, appSecret) {
    let combined = appKey + timestamp;
    let hashStr = crypto.createHmac('sha256', appSecret).update(combined).digest("hex");

    return hashStr.toLowerCase();
};

async function ttsDemo(content, spkid, appKey, timestamp, sign) {
    let resTts = {
        message: "",
        code: "",
        urlTts: ""
    };

    let bodyRes, jsonData;
    let options;
    {
        jsonData = {
            spkid: spkid,
            content: content,
            audioType: 11
        };
        options = {
            method: 'POST',
            //uri: 'http://tts.yitutech.com/api/tts',
            uri: 'https://ai.abcpen.com/api/tts',
            //uri: 'http://127.0.0.1:3010/api/tts',
            form: jsonData,
            headers: {
                "x-dev-id": appKey,
                "x-signature": sign,
                "x-request-send-timestamp": timestamp
            },
            timeout: 5000,
            forever: true
        }
    }
    try {
        let ts = Date.now();
        bodyRes = await rp(options);
        console.info("tts --------------------------------->", content, bodyRes, typeof bodyRes, ", Duration: ", (Date.now() - ts) + "ms");
        if (typeof bodyRes != 'object') {
            console.log("not object, use JSON.parse", bodyRes);
            bodyRes = JSON.parse(bodyRes);
            console.log("after parse: ", bodyRes);

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
    if (appKey.length <=0 || appSecret <=0 ) {
        console.log("请向商务申请开发者密钥！");
        process.exit(0);
    }
    console.log(appKey, appSecret)

    let timestamp = parseInt(Date.now() / 1000) + "";
    let sign = verifySha256Sign(appKey, timestamp, appSecret);
    console.log("sign sha256 is: ", sign);
    let tts2 = await ttsDemo("满足用户高性价比需求的消费场景；通过流量集中分发，构成好品质加极致价格力的特卖场，满足极速版商城用户的购物需求，提高用户粘性，形成用户‘日销好货’的心智",
        0, appKey, timestamp, sign);
    console.log("tts2: ", tts2);

})();
