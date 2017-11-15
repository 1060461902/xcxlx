###提前说明：
测试用服务器ip：120.78.78.116
###数据库 表
<h4>lx_address</h4> 存储用户地址信息 openid,address_id,user_name,phone,address<br/>
address_id,openid共同构成表的主键
<h4>lx_goodsinfo</h4>存储货物信息 goods_id 由后台系统自动生成的随机字符串,goods_name,goods_price 单位为分,goods_img 货品图片在后台的保存路径,goods_num<br/>
goods_id为表的主键
###调起登录
在调起登录前应检查用户登录态是否有效,确认无效后再调用<br/>
url:/wx/login.wx<br/>
request method:post<br/>
param:json格式字符串：{code: res.code}<br/>
return:字符串：thirdsessionid unix下168位随机字符串，windows下170位随机字符串 
###用户添加地址
url:/wx/address/add.wx<br/>
request method:post<br/>
param:json格式字符串：<br/>
{<br/>
"thirdsessionid":"test",//3rd_sessionID，如果用户登录，小程序中应该存有用户的3rd_sessionID<br/>
"user_name":"王五",//用户姓名<br/>
"phone":"11122233355",//用户电话<br/>
"address":"中国"//用户地址 不超过100个字符<br/>
}<br/>
return: 字符串："true"成功添加，"false"添加失败,"lose"后台3rd_sessionID失效，需要重新调起登录
###前端获取用户地址
url:/wx/address/get.wx<br/>
request method:post<br/>
param:json格式字符串：<br/>
{<br/>
"thirdsessionid":"test"//3rd_sessionID，调用登录后，后端返回的3rd_sessionID，应该保存在小程序中<br/>
}<br/>
return:json格式字符串 <br/>
[<br/>
    {"address":"中国",//地址<br/>
    "address_id":"6f8E1UyHtVV9454doLnm9xnsNkp4KB",//调用/add.wx接口时生成的随机字符串<br/>
    "phone":"11122233355",//用户电话<br/>
    "user_name":"王五"//姓名<br/>
    }<br/>
]<br/>
用户地址信息！！！列表！！！！<br/>
字符串：""(空字符串)获取失败<br/>
字符串："lose"后台3rd_sessionID失效，需要重新调起登录
###用户更新地址
url:/wx/address/update.wx<br/>
request method:post<br/>
param:json格式字符串：<br/>
{<br/>
"thirdsessionid":"test",//3rd_sessionID，如果用户登录，小程序中应该存有用户的3rd_sessionID<br/>
"user_name":"张三",//用户姓名<br/>
"phone":"11122233355",//用户电话<br/>
"address":"中国"//用户地址 不超过100个字符<br/>
"address_id":"NmcRiPRtS0aJIoGqa1quJW7NQZqLRs"//调用/add.wx时，后台自动生成的30位随机数，需重新传回后台<br/>
}<br/>
return: 字符串："true"成功更新，"false"更新失败,"lose"后台3rd_sessionID失效，需要重新调起登录
###用户删除地址
url:/wx/address/delete.wx<br/>
request method:post<br/>
param:json格式字符串：<br/>
{<br/>
"thirdsessionid":"test",//3rd_sessionID，如果用户登录，小程序中应该存有用户的3rd_sessionID<br/>
"address_id":"NmcRiPRtS0aJIoGqa1quJW7NQZqLRs"//调用/add.wx时，后台自动生成的30位随机数，需重新传回后台<br/>
}<br/>
return: 字符串："true"成功删除，"false"删除失败,"lose"后台3rd_sessionID失效，需要重新调起登录
###管理员添加，更新商品信息 单个
url:/wx/goods/addorupdate.wx<br/>
request method:post<br/>
param:表单格式：<br/>
goods_id:货物ID 不超过50个字符 商家输入<br/>
goods_name:货物名称 不超过50个字符<br/>
goods_price:货物价格 单位 分<br/>
goods_num:货物数量<br/>
goods_weight:货物重量 单位kg 保留小数点一位 严格按照计价规则填写<br/>
file:商品图片<br/>
return: 字符串："true"操作成功，"false"操作失败
###管理员删除货品信息（单个）
url:/wx/goods/delete.wx<br/>
request method:post<br/>
param:json格式字符串：<br/>
{"goods_id":"12983h13hasd"} //商品ID<br/>
return: 字符串："true"成功删除，"false"删除失败
###前端获取所有货物信息
url:/wx/goods/get.wx<br/>
request method:post<br/>
param:无
return:json格式字符串：<br/>
[{<br/>
"goods_id":"12983h13hasd",//货物ID<br/>
"goods_img":null,//货物照片在服务器中的地址<br/>
"goods_name":"1233123safasd",//货物名称<br/>
"goods_num":131,//货物数量<br/>
"goods_price":9000//货物价格 单位 分<br/>
"goods_weight":1.2//货物重量 严格按照计价规则输入，并非实际重量 单位 千克 小数点一位<br/>
},<br/>
{"goods_id":"13jffs3hasd",<br/>
"goods_img":"123123saddvz1323/e123123",<br/>
"goods_name":"123312312dasdd",<br/>
"goods_num":0,<br/>
"goods_price":9900<br/>
"goods_weight":1.2//货物重量 严格按照计价规则输入，并非实际重量 单位 千克 小数点一位<br/>}
]
###批量添加或修改运费计价规则
url:/wx/freight/addORUpdate.wx<br/>
request method:post<br/>
param:json格式字符串<br/>
[
      {"continue_weight":13.0,//续重 单位 千克 保留小数点一位<br/>
      "first_weight":23.0,//初重 单位 千克 保留小数点一位<br/>
      "flow_place":"杭州,湖州,嘉兴,宁波,绍兴,台州,温州,衢州,丽水,金华,舟山",//流向地区<br/>
      "freight_id":"123",//计价规则ID 商家填写<br/>
      "province":"浙江"},//省份<br/>
      {"continue_weight":20.0,"first_weight":25.0,"flow_place":"呼伦贝尔,兴安盟","freight_id":"12asd","province":"内蒙古"},<br/>
      {"continue_weight":20.0,"first_weight":25.0,"flow_place":"","freight_id":"1892371982","province":"黑龙江"}<br/>
  ]<br/>
return: 字符串："true"成功，"false"失败
###前端获取所有运费计价规则
url:/wx/freight/getAll.wx<br/>
request method:post<br/>
param:无<br/>
return:json格式字符串<br/>
       [
             {"continue_weight":13.0,//续重 单位 千克 保留小数点一位<br/>
             "first_weight":23.0,//初重 单位 千克 保留小数点一位<br/>
             "flow_place":"杭州,湖州,嘉兴,宁波,绍兴,台州,温州,衢州,丽水,金华,舟山",//流向地区<br/>
             "freight_id":"123",//计价规则ID 商家填写<br/>
             "province":"浙江"},//省份<br/>
             {"continue_weight":20.0,"first_weight":25.0,"flow_place":"呼伦贝尔,兴安盟","freight_id":"12asd","province":"内蒙古"},<br/>
             {"continue_weight":20.0,"first_weight":25.0,"flow_place":"","freight_id":"1892371982","province":"黑龙江"}<br/>
         ]<br/>
###删除运费计价规则
url:/wx/freight/delete.wx<br/>
request method:post<br/>
param:json字符串：{"freight_id":"122"}<br/>
return: 字符串："true"成功删除，"false"删除失败
###单个更新计价规则
url:/wx/freight/update.wx<br/>
request method:post<br/>
param:json字符串：<br/>
{"continue_weight":20.0,<br/>
"first_weight":25.0,<br/>
"flow_place":"",<br/>
"freight_id":"122",<br/>
"province":"山东"}<br/>
return: 字符串："true"成功，"false"失败
###单个添加计价规则
url:/wx/freight/insert.wx<br/>
request method:post<br/>
param:json字符串：<br/>
{"continue_weight":20.0,<br/>
"first_weight":25.0,<br/>
"flow_place":"",<br/>
"freight_id":"122213",<br/>
"province":"山东"}<br/>
return: 字符串："true"成功，"false"失败
###微信支付以及订单接口未完成
service.pay
controller.pay
未获取微信支付相关材料，weixin.properties未填补完整
