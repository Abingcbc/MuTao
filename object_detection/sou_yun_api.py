import requests
import json

url = "https://api.sou-yun.cn/api/Poem"

with open("poem.json",'r+') as file:
    for i in range(1,4):
        r = requests.get(url+"?key="+str(i)+"&jsontype=true")
        print(r.json())
        result = json.dumps(r.json())
        
        file.write(result+"\n")
