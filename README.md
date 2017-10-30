###提前说明：
测试用服务器ip：120.78.78.116
###数据库 表
<h4>lx_address</h4> 存储用户地址信息 openid,user_name,phone,address<br/>
user_name,phone,address为表的候选码
<h4></h4>
###调起登录
url:/wx/login.wx<br/>
request method:post<br/>
param:{code: res.code}<br/>
return:unix下168位随机字符串，windows下170位随机字符串
###用户添加地址
url:/wx/address/add.wx<br/>
request method:post<br/>
param:
{<br/>
"thirdsessionid":"test",//3rd_sessionID，如果用户登录，小程序中应该存有用户的3rd_sessionID<br/>
"user_name":"王五",//用户姓名<br/>
"phone":"11122233355",//用户电话<br/>
"address":"中国"//用户地址<br/>
}<br/>
return: "true"成功添加，"false"添加失败
###用户更新地址
url:/wx/address/update.wx<br/>
request method:post<br/>
param:
{<br/>
"thirdsessionid":"test",//3rd_sessionID，如果用户登录，小程序中应该存有用户的3rd_sessionID<br/>
"user_name":"张三",//用户姓名<br/>
"phone":"11122233355",//用户电话<br/>
"address":"中国"//用户地址<br/>
"address_id":"NmcRiPRtS0aJIoGqa1quJW7NQZqLRs"//调用/add.wx时，后台自动生成的30位随机数，需重新传回后台<br/>
}<br/>
return: "true"成功添加，"false"添加失败
###用户更新地址
url:/wx/address/delete.wx<br/>
request method:post<br/>
param:
{<br/>
"thirdsessionid":"test",//3rd_sessionID，如果用户登录，小程序中应该存有用户的3rd_sessionID<br/>
"address_id":"NmcRiPRtS0aJIoGqa1quJW7NQZqLRs"//调用/add.wx时，后台自动生成的30位随机数，需重新传回后台<br/>
}<br/>
return: "true"成功添加，"false"添加失败