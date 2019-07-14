from clarifai.rest import ClarifaiApp
import json

app = ClarifaiApp(api_key="b55c8e8d48054f7bad8daeb7fa3d6221")
model = app.public_models.general_model
response = model.predict_by_filename("/Users/cbc/Desktop/homework/DataMining/hw2/dianping_avatar/2212_avatar.jpg")

labels = response['outputs'][0]['data']['concepts']


# labels = [{'id': 'ai_l8TKp2h5', 'name': '人', 'value': 0.995841383934021, 'app_id': 'main'},
#           {'id': 'ai_dxSG2s86', 'name': '男性', 'value': 0.9794287085533142, 'app_id': 'main'},
#           {'id': 'ai_MKSwLDhD', 'name': '打印', 'value': 0.978895366191864, 'app_id': 'main'},
#           {'id': 'ai_Dm5GLXnB', 'name': '插图', 'value': 0.9616398215293884, 'app_id': 'main'},
#           {'id': 'ai_djRqCRT8', 'name': '文化', 'value': 0.952273964881897, 'app_id': 'main'},
#           {'id': 'ai_JBPqff8z', 'name': '艺术', 'value': 0.9467450380325317, 'app_id': 'main'},
#           {'id': 'ai_QrcQWGwJ', 'name': '绘画', 'value': 0.9434459209442139, 'app_id': 'main'},
#           {'id': 'ai_S0rvLGPz', 'name': '老', 'value': 0.93290114402771, 'app_id': 'main'},
#           {'id': 'ai_VPmHr5bm', 'name': '成人', 'value': 0.9066742658615112, 'app_id': 'main'},
#           {'id': 'ai_QFTL1l9x', 'name': '版画', 'value': 0.9045960903167725, 'app_id': 'main'},
#           {'id': 'ai_RQccV41p', 'name': '女人', 'value': 0.9043139219284058, 'app_id': 'main'},
#           {'id': 'ai_gJm9b4ts', 'name': '复古', 'value': 0.9008330702781677, 'app_id': 'main'},
#           {'id': 'ai_0SL2mdXt', 'name': '军事', 'value': 0.9001017808914185, 'app_id': 'main'},
#           {'id': 'ai_KJZC1RDL', 'name': '古老的', 'value': 0.8961162567138672, 'app_id': 'main'},
#           {'id': 'ai_Br1sz8D7', 'name': '战争', 'value': 0.8958218097686768, 'app_id': 'main'},
#           {'id': 'ai_QdsVHRFl', 'name': '军队', 'value': 0.8834987878799438, 'app_id': 'main'},
#           {'id': 'ai_bKV6dvbZ', 'name': '战斗', 'value': 0.8657034635543823, 'app_id': 'main'},
#           {'id': 'ai_1p2zMmqb', 'name': '复古', 'value': 0.8405149579048157, 'app_id': 'main'},
#           {'id': 'ai_94cQBGlt', 'name': '两个', 'value': 0.8312219381332397, 'app_id': 'main'},
#           {'id': 'ai_wPx6P0rV', 'name': '武器', 'value': 0.8060547113418579, 'app_id': 'main'}]

print(labels)

