import hashlib
import hmac
import time
import base64

def get_signature_flytek(ts, app_id, api_key):
    tt = (app_id + ts).encode('utf-8')
    md5 = hashlib.md5()
    md5.update(tt)
    baseString = md5.hexdigest()
    baseString = bytes(baseString, encoding='utf-8')

    apiKey = api_key.encode('utf-8')
    signa = hmac.new(apiKey, baseString, hashlib.sha1).digest()
    signa = base64.b64encode(signa)
    signa = str(signa, 'utf-8')
    return signa

def get_signature_yitu(timestamp, dev_id, dev_key):
    ts = str(timestamp)
    id_ts = str(dev_id) + ts
    signature = hmac.new(str(dev_key).encode(), id_ts.encode(),
                         digestmod=hashlib.sha256).hexdigest()
    return signature