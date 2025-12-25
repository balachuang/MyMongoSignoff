## ref:
https://ithelp.ithome.com.tw/articles/10366353
https://ithelp.ithome.com.tw/articles/10365958
https://hackmd.io/@SuFrank/BJOe3Fi3c
https://medium.com/@ananimziv/%E6%A1%86%E6%9E%B6-springboot%E5%92%8Cmongodb%E7%9A%84%E5%AF%A6%E4%BD%9C-f1575e1e0a8d



## pull latest MongoDb image
docker pull mongo:latest

## run image as container
docker run --name mymongo -v D:\MyMongoDB\data:/data/db -d -p 27017:27017 --rm mongo
docker run --name mymongo -v D:\MyMongoDB\data:/data/db -d -p 27017:27017 --rm mongo mongod --auth

## stop mongodb container
docker stop mymongo

## 切換 DB, 會自動建立
## 一定要先要在admin資料庫中，建立一個管理員權限的帳號，認證才能生效，也才能夠幫其他的資料庫建立資料。 ???
use demo
use admin
db.createUser({user:"root", pwd:"pass", roles: [{role:"userAdminAnyDatabase", db:"admin" },{role:"readWriteAnyDatabase", db:"admin"}]})

## create user
## 之後就可以用 bala:pass 連進 mongodb, 實際測試有 auth 的連接方式.
db.drapUser("bala")
db.createUser({user:"bala", pwd:"pass", roles: [{role:"readWrite",db:"demo"}, {role:"userAdminAnyDatabase", db:"admin" },{role:"readWriteAnyDatabase", db:"admin"}]})
db.createUser({user:"bala", pwd:"pass", roles: [{role:"readWrite",db:"demo"}]})

db.updateUser("bala", {roles: [{role:"readWrite",db:"demo"}, {role:"readWrite",db:"test"}]})

用 MongoDB Compass 連線, 下載回來是 exe 檔, 直接執行即可.


上面的步驟都沒問題, 有成功建出 DB 和 user, 也可以用建出來的 user 連 DB 加資料.
但是不知為什麼用 spring boot 連任都失敗. 失敗原因是認証失敗.
==> 找到原因了, 原來 spring boot 4 之後, mongodb 的 config 在 spring.mongodb 下, 而不是 spring.data.mongodb

連線成功後, 開始出現找不到 item 的 error. 可以抓到有 2 筆, 但是資料都是 null.
==> 結果是大小寫問題, DB 裡是用大寫, java 裡就要用大寫, 不然就是要另外寫 mapping
==> MongoDB 的 field name 用 底線 好像不行, 之後再找解法.

db.version()
8.2.2

db.version
[Function: version] AsyncFunction {
  apiVersions: [ 0, 0 ],
  returnsPromise: true,
  serverVersions: [ '0.0.0', '999.999.999' ],
  topologies: [ 'ReplSet', 'Sharded', 'LoadBalanced', 'Standalone' ],
  returnType: { type: 'unknown', attributes: {} },
  deprecated: false,
  platforms: [ 'Compass', 'Browser', 'CLI' ],
  isDirectShellCommand: false,
  acceptsRawInput: false,
  shellCommandCompleter: undefined,
  newShellCommandCompleter: undefined,
  help: [Function (anonymous)] Help
}


