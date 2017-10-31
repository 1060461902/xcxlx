###提前说明：
测试用服务器ip：120.78.78.116
###数据库 表
<h4>lx_address</h4> 存储用户地址信息 openid,address_id,user_name,phone,address<br/>
address_id,openid共同构成表的主键
<h4></h4>
###调起登录
在调起登录前应检查用户登录态是否有效,确认无效后再调用<br/>
url:/wx/login.wx<br/>
request method:post<br/>
param:{code: res.code}<br/>
return:thirdsessionid unix下168位随机字符串，windows下170位随机字符串 
###用户添加地址
url:/wx/address/add.wx<br/>
request method:post<br/>
param:<br/>
{<br/>
"thirdsessionid":"test",//3rd_sessionID，如果用户登录，小程序中应该存有用户的3rd_sessionID<br/>
"user_name":"王五",//用户姓名<br/>
"phone":"11122233355",//用户电话<br/>
"address":"中国"//用户地址<br/>
}<br/>
return: "true"成功添加，"false"添加失败
###前端获取用户地址
url:/wx/address/update.wx<br/>
request method:post<br/>
param:<br/>
{<br/>
"thirdsessionid":"test"//3rd_sessionID，调用登录后，后端返回的3rd_sessionID，应该保存在小程序中<br/>
}<br/>
return: <br/>
[<br/>
    {"address":"中国",//地址<br/>
    "address_id":"6f8E1UyHtVV9454doLnm9xnsNkp4KB",//调用/add.wx接口时生成的随机字符串<br/>
    "phone":"11122233355",//用户电话<br/>
    "user_name":"王五"//姓名<br/>
    }<br/>
]<br/>
用户地址信息！！！列表！！！！
###用户更新地址
url:/wx/address/update.wx<br/>
request method:post<br/>
param:<br/>
{<br/>
"thirdsessionid":"test",//3rd_sessionID，如果用户登录，小程序中应该存有用户的3rd_sessionID<br/>
"user_name":"张三",//用户姓名<br/>
"phone":"11122233355",//用户电话<br/>
"address":"中国"//用户地址<br/>
"address_id":"NmcRiPRtS0aJIoGqa1quJW7NQZqLRs"//调用/add.wx时，后台自动生成的30位随机数，需重新传回后台<br/>
}<br/>
return: "true"成功更新，"false"更新失败
###用户删除地址
url:/wx/address/delete.wx<br/>
request method:post<br/>
param:<br/>
{<br/>
"thirdsessionid":"test",//3rd_sessionID，如果用户登录，小程序中应该存有用户的3rd_sessionID<br/>
"address_id":"NmcRiPRtS0aJIoGqa1quJW7NQZqLRs"//调用/add.wx时，后台自动生成的30位随机数，需重新传回后台<br/>
}<br/>
return: "true"成功删除，"false"删除失败