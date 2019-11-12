from clarifai.rest import ClarifaiApp
import os
from flask import Flask, request, redirect, url_for, send_from_directory, jsonify, abort, make_response, render_template
from werkzeug import secure_filename
import requests
import json
import pymysql
import math
import random
import time
import datetime
import functools


CURRENT_PATH = os.getcwd()

UPLOAD_FOLDER = 'static/Uploads' 
ALLOWED_EXTENSIONS = set(['jpeg', 'png', 'jpg']) 

jiuge_website = "http://118.190.162.99:8080/"
user_id = None

# ALLOWED_EXTENSIONS = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])

app = Flask(__name__)
application = ClarifaiApp(api_key='c63b5fc84f444eb5b853cddf68ec257b')
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER  

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
    conn=pymysql.connect(host="127.0.0.1",port=3306,user="root",password="friday",db="mutao",charset="utf8")
    cursor=conn.cursor()
    ret=cursor.execute("select * from user where username=%s;",(username,))
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
    file = request.files['file']
    # if file and allowed_file(file.filename):
    filename = secure_filename(file.filename)
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
    j = {"query": {"match": {"content": tag_string}}, "size": 10}
    raw = json.dumps(j)
    r = requests.post('http://127.0.0.1:9200/poems/poem/_search', data=raw)

    result = json.loads(r.text)
    poems = []
    for poem in result['hits']['hits']:
        if poem['_score'] < 35:
            break
        poems.append({'body': poem['_source']['body'],
                      'title': poem['_source']['title'],
                      'dynasty': poem['_source']['dynasty'],
                      'author': poem['_source']['author'],
                      'isAI':0})
    
    if 0 < len(poems) < random.randrange(5,8):
        for i in range(random.randrange(3,5)):
            # 诗
            if random.random() > 0.5:
                yan = random.choice([5, 7])
                keyword = random.choice(tag_urls)
                poem_context = send_poem('JJ', yan, keyword)
            # 词
            else:
                yan = random.choice(list(range(1,20)))
                keyword = ' '.join(random.sample(tag_urls, 2))
                poem_context = send_poem('SC', yan, keyword)
            
            if poem_context is not None:
                formatted_poem = functools.reduce(lambda x, y: x + '\n' + y, poem_context)
                formatted_poem = formatted_poem.strip('\n')
                poems.append({'body': formatted_poem,
                            'title': '',
                            'dynasty': '',
                            'author': 'AI',
                            'isAI':1})
    if len(poems) == 0:
        poems.append({'body': "恕桃子愚钝，想不出来诗，换个关键词吧",
                    'title': '',
                    'dynasty': '',
                    'author': '',
                    'isAI':1})
    os.remove(temp_path)
    return json.dumps(poems)

def random_string(length):
    length = length or 32
    chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678"
    max_pos = len(chars)
    pwd = ''
    for i in range(0, length):
        pwd += chars[math.floor(random.randint(0, max_pos - 1))]
    return pwd


def get_poem(data):
    res = None
    timeout = 0
    while not res or res.json()["code"] != "1":
        timeout += 1
        if timeout > 100:
            return None
        time.sleep(1)
        res = requests.post(jiuge_website + 'getPoem', data)
        print(res.text)
    return res.json()["content"]


def send_poem(typ, yan, keyword):
    global user_id
    if not user_id:
        user_id = random_string(30)
    data = {"type": typ, "yan": yan, "keyword": keyword, "user_id": user_id}
    res = requests.post(jiuge_website + 'sendPoem', data)
    print(res.content)
    if res.status_code != 200 and res.json()["code"] != "0000":
        return None
    else:
        poem = get_poem(data)
        print(poem)
        return poem

# /getRecJJ param: keyword|string, words|number
# 注：目前只能有一个keyword
# words 选项: 7|七言, 5|五言
# def simulate_requestjj():
#     keyword = request.form['keyword']
#     words = request.form['words']
#     poem = simulate_post_poem('JJ', keyword, words)
#     if poem is not None:
#         formatted_poem = functools.reduce(lambda x, y: x + '\n' + y, poem)
#         formatted_poem = formatted_poem.strip('\n')
#     else:
#         formatted_poem = None
#     return jsonify({"ai": formatted_poem}), 200 if poem is not None else 400

# /getRecSC param: keyword|string(separated by spaces), type|number
# type选择有：归字谣, 如梦令, 梧桐影, 渔歌子, 捣练子, 忆江南, 忆王孙, 河满子,
# 思帝乡, 醉吟商, 卜算子, 点绛唇, 踏莎行, 画堂春, 浣溪沙, 武陵春, 采桑子, 海棠春,
# 苏幕遮, 蝶恋花  (type的编号从1开始)
# keyword: 每个为1-2个字，用空格分开多个关键词
# def simulate_requestsc():
#     keyword = request.form['keyword']
#     typ = request.form['type']
#     poem = simulate_post_poem('SC', keyword, typ)
#     if poem is not None:
#         formatted_poem = functools.reduce(lambda x, y: x + '\n' + y, poem)
#         formatted_poem = formatted_poem.strip('\n')
#     else:
#         formatted_poem = "恕桃子愚钝，想不出来诗，换个关键词吧"
#     return jsonify({"ai": formatted_poem})

if __name__ == '__main__':
    app.run(host='127.0.0.1',port=5000)
