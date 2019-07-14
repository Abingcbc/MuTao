from clarifai.rest import ClarifaiApp
import os
from flask import Flask, request, redirect, url_for, send_from_directory
from werkzeug import secure_filename
import requests
import json

CURRENT_PATH = os.getcwd()

UPLOAD_FOLDER = 'static/Uploads'  # 文件下载路径
ALLOWED_EXTENSIONS = set(['jpeg', 'png', 'jpg'])  # 文件允许上传格式

# ALLOWED_EXTENSIONS = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])

app = Flask(__name__)
application = ClarifaiApp(api_key='c63b5fc84f444eb5b853cddf68ec257b')
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER  # 设置文件下载路径


# def allowed_file(filename):  # 通过将文件名分段的方式查询文件格式是否在允许上传格式范围之内
    # return '.' in filename and filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS



@app.route('/', methods=['POST'])
def controller():
    file = request.files['file']#将上传的文件赋予file
    # if file and allowed_file(file.filename):#当确认有上传文件并且格式合法
    filename = secure_filename(file.filename)#使用secure_filename()让文件名变得安全
    temp_path=os.path.join(app.config['UPLOAD_FOLDER'], filename)
    if not os.path.exists(app.config['UPLOAD_FOLDER']):
        os.makedirs(app.config['UPLOAD_FOLDER'])

    file.save(temp_path)
    model = application.public_models.general_model
    response_data = model.predict_by_filename(temp_path)
    tag_urls = []
    for concept in response_data['outputs'][0]['data']['concepts']:
        tag_urls.append(concept['name'])

    tag_string = " ".join(str(i) for i in tag_urls)
    print(tag_string)
    j = {"query": {"match": {"content": tag_string}}}
    raw = json.dumps(j)
    r = requests.post('http://127.0.0.1:9200/poems/poem/_search', data=raw)

    result = json.loads(r.text)
    poems = []
    for poem in result['hits']['hits']:
        poems.append({'body': poem['_source']['body'],
                      'title': poem['_source']['title'],
                      'dynasty': poem['_source']['dynasty'],
                      'author': poem['_source']['author']})



    os.remove(temp_path)
    print(result)
    return json.dumps(poems)


if __name__ == '__main__':
    app.run(debug=True, host='127.0.0.1')
