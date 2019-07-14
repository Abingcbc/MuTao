import pymysql
try:
    conn = pymysql.connect(host='47.103.21.70', user='root', passwd='3103Diao', db='mutao', port=3306, charset='utf8')
    cur = conn.cursor()
    cur.execute('select * from poem')
    data = cur.fetchall()
    for d in data:
        print("id: "+str(d[0])+' 密码：'+d[2])
    cur.close()
    conn.close()

except Exception:
    print('查询失败')
