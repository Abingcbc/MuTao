from clarifai.rest import ClarifaiApp
import os
from flask import Flask, request, redirect, url_for, send_from_directory
from werkzeug import secure_filename
import requests
import json
import pymysql

CURRENT_PATH = os.getcwd()

UPLOAD_FOLDER = 'static/Uploads'  # 文件下载路径
ALLOWED_EXTENSIONS = set(['jpeg', 'png', 'jpg'])  # 文件允许上传格式

# ALLOWED_EXTENSIONS = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])

app = Flask(__name__)
application = ClarifaiApp(api_key='c63b5fc84f444eb5b853cddf68ec257b')
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER  # 设置文件下载路径

synonymKey='1c119e1ba1b9ef7a'
requestPara='https://api.jisuapi.com/jinyifanyi/word?appkey='+synonymKey+'&word='

# def allowed_file(filename):  # 通过将文件名分段的方式查询文件格式是否在允许上传格式范围之内
    # return '.' in filename and filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS


@app.route('/register',methods=['POST'])
def register():
    # 在数据库里寻找是否已经注册
    # 若是没有注册
    # 自行更改数据库密码
    username = request.form['username']
    password = request.form['password']
    conn = pymysql.connect(host="127.0.0.1", port=3306, user="root", passwd="friday", db="mutao", charset="utf8")
    cursor = conn.cursor()
    count = cursor.execute("select * from user where username = %s", (username,))
    if count:
        cursor.close()
        conn.close()
        return json.dumps({"is_success":-1})
    else:
        sql=cursor.execute('insert into user values(%s,%s)',(username,password))
        if sql:
            conn.commit()
            cursor.close()
            conn.close()
            return json.dumps({"is_success":1})
        else:
            cursor.close()
            conn.close()
            return json.dumps({"is_success":-1})

@app.route('/login',methods=['POST'])
def login():
    #在数据库里寻找是否存在
    username = request.form['username']
    password = request.form['password']
    conn=pymysql.connect(host="127.0.0.1",port=3306,user="root",password="friday",database="mutao",harset="utf8")
    cursor=conn.cursor
    ret=cursor.execute("select * from user where username=%s;",(userName,))
    if ret:
        conn.commit()
        cursor.close()
        conn.close()
        return json.dumps({"is_success":1})
        #可以加一个全局变量来保证不重复登录
    else:
        cursor.close()
        conn.close()
        return json.dumps({"is_success":-1})

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

    print(tag_urls)#打表
    synResult=[]
    synPara=[]
    for index in range(len(tag_urls)):
        synPara.append(requestPara+tag_urls[index])
        print(synPara[-1])
        r=requests.get(synPara[-1])
        result=json.loads(r.text)
        print(result)#打表
        if result["status"]!=0 or result["result"]["jin"][0]=="":
            synResult.append(tag_urls[index])
        else:
            synResult.extend(result["result"]["jin"])
    print(synResult)

    ##tag_urls被我修改为请求的完整url了，所有的结果存在synResult（同义词查询结果）
    tag_string = " ".join(str(i) for i in synResult)
    print(tag_string)
    j = {"query": {"match": {"content": tag_string}}}
    raw = json.dumps(j)
    r = requests.post('http://127.0.0.1:9200/poems/poem/search', data=raw)

    result = json.loads(r.text)
    poems = []
    for poem in result['hits']['hits']:
        poems.append({'body': poem['_source']['body'],
                      'title': poem['_source']['title'],
                      'dynasty': poem['_source']['dynasty'],
                      'author': poem['_source']['author']})

    os.remove(temp_path)
    print(result)
    return json.dumps(tag_string)


if __name__ == '__main__':
    app.run(debug=True, host='127.0.0.1')
