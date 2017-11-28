###提前说明：
测试用服务器ip：120.78.78.116<br/>
订单状态：0——正在付款，1——付款成功，2——商家已处理，3——退款，4——用户付款失败
用户地址格式必须是：普通省：XX省XX市(市必须是地级市)<br/>
                    自治区：XX自治区XX市(市必须是地级市)<br/>
                    直辖市：xxx市<br/>
录入运费计价规则的时候 如果流向地区是空或者是其他 一律填写“其他”<br/>
如果在录入运费计价规则碰到直辖市 省和地区都填直辖市名<br/>
录入货物重量时 单位 千克 小数点一位<br/>
获取调用获取运费接口时，返回的数值以分为单位<br/>
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
"address":"中国"//用户地址 格式：普通省：XX省XX市(市必须是地级市) 
                                 自治区：XX自治区XX市(市必须是地级市) 
                                 直辖市：xxx市
                                 不超过100个字符<br/>
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
goods_weight:货物重量 单位kg 保留小数点一位<br/>
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
"goods_weight":1.2//货物重量 单位 千克 小数点一位<br/>
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
      {"continue_weight":13.0,//续重 单位 元/千克 保留小数点一位<br/>
      "first_weight":23.0,//初重 单位 元/千克 保留小数点一位<br/>
      "flow_place":"杭州,湖州,嘉兴,宁波,绍兴,台州,温州,衢州,丽水,金华,舟山",//流向地区<br/>
      "freight_id":"123",//计价规则ID 商家填写<br/>
      "province":"浙江",//省份<br/>
      "express_company_id":"SF"},//快递公司简写 由lx_express_company表维护<br/>
      {"continue_weight":20.0,"express_company_id":"SF","first_weight":25.0,"flow_place":"呼伦贝尔,兴安盟","freight_id":"12asd","province":"内蒙古"},<br/>
      {"continue_weight":20.0,"express_company_id":"SF","first_weight":25.0,"flow_place":"","freight_id":"1892371982","province":"黑龙江"}<br/>
  ]<br/>
return: 字符串："true"成功，"false"失败
###前端获取所有运费计价规则
url:/wx/freight/getAll.wx<br/>
request method:post<br/>
param:无<br/>
return:json格式字符串<br/>
       [
             {"continue_weight":13.0,//续重 单位 元/千克 保留小数点一位<br/>
             "first_weight":23.0,//初重 单位 元/千克 保留小数点一位<br/>
             "flow_place":"杭州,湖州,嘉兴,宁波,绍兴,台州,温州,衢州,丽水,金华,舟山",//流向地区<br/>
             "freight_id":"123",//计价规则ID 商家填写<br/>
             "province":"浙江",//省份<br/>
             ,"express_company_id":"SF"},//快递公司简写 由lx_express_company表维护<br/>
             {"continue_weight":20.0,"express_company_id":"SF"，"first_weight":25.0,"flow_place":"呼伦贝尔,兴安盟","freight_id":"12asd","province":"内蒙古"},<br/>
             {"continue_weight":20.0,"express_company_id":"SF"，"first_weight":25.0,"flow_place":"","freight_id":"1892371982","province":"黑龙江"}<br/>
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
"province":"山东",<br/>
"express_company_id":"SF"<br/>
}<br/>
return: 字符串："true"成功，"false"失败
###单个添加计价规则
url:/wx/freight/insert.wx<br/>
request method:post<br/>
param:json字符串：<br/>
{"continue_weight":20.0,<br/>
"first_weight":25.0,<br/>
"flow_place":"",<br/>
"freight_id":"122213",<br/>
"province":"山东"，<br/>
"express_company_id":"SF"}<br/>
return: 字符串："true"成功，"false"失败
###获取运费并显示给客户
url:/wx/wxpay/freight.wx<br/>
request method:post<br/>
param:json字符串：<br/>
{<br/>
  "address":"浙江省杭州市江干区",<br/>
  "express_company_id":"SF",<br/>
  "goods_array":[<br/>
    {<br/>
      "goods_id":"1231asdasdasd",<br/>
      "goods_number":1<br/>
    }<br/>
  ]<br/>
}<br/>
return:字符串："unknown":获取不到运费 "4250"（数字）:运费价格，单位 分
###获取某人某状态的订单
url:/wx/wxpay/getonepersonthestatusall.wx<br/>
request method:post<br/>
param:json字符串：<br/>
{<br/>
	"thirdsessionid":"test",<br/>
	"order_status":0 //订单状态<br/>
}<br/>
return:字符串："fail"重置失败 "lose"3rd_sessionID失效 
   正常：json格式字符串<br/>
   {"order_status":1,<br/>
   "address":"涓浗",<br/>
   "phone":"11122233355",<br/>
   "user_name":"鐜嬩簲",<br/>
   "freight":1200,<br/>
   "order_array":<br/>
   [<br/>
   {"goods_name":"榫欒櫨",<br/>
   "goods_number":2,<br/>
   "goods_price":1,<br/>
   "goods_id":"1231asdasdasd",<br/>
   "goods_img":"192.168.145.1/wx/images/timg.jpg"}<br/>
   ],<br/>
   "express_company_id":"SF",<br/>
   "order_time":1510987445000<br/>
   ,"user_add_message":"鍟婂疄鎵撳疄",<br/>
   "order_id":"akslud","order_wx_id":"12312"<br/>
   }<br/>
###商家获取某状态的订单
url:/wx/wxpay/getthestatusall.wx<br/>
request method:post<br/>
param:json字符串：<br/>
{<br/>
	"order_status":0 //订单状态<br/>
}<br/>
return：json格式字符串：<br/>
[<br/>
   {"address":"中国",//地址<br/>
   "express_company_id":"SF",//快递公司ID<br/>
   "freight":1200,//运费价格<br/>
   "goods_id":"1231asdasdasd",//货物ID<br/>
   "goods_img":"E:\\ideawk\\xcxlx\\out\\artifacts\\xcxlx_war_exploded\\images/1.jpg",<br/>
   "goods_name":"沙发上",//货物名<br/>
   "goods_number":2,//购买数量<br/>
   "goods_price":1200,//货物价格 单位 分<br/>
   "order_id":"akslud",//订单编号 后台生成<br/>
   "order_status":0,//订单状态 0未支付成功 1支付成功 2订单完成<br/>
   "order_time":1510987445000,//订单时间戳<br/>
   "order_wx_id":"12312",//微信订单号<br/>
   "phone":"11122233355",//用户电话号码<br/>
   "user_add_message":"啊实打实",//用户备注<br/>
   "user_name":"王五"}//用户姓名<br/>
   ]
###商家更新某个订单的状态
url:<br/>
request method:post<br/>
param:json字符串：<br/>
{<br/>
	"order_id":"akslud",<br/>
	"order_status":1<br/>
}<br/>
return: 字符串："true"成功，"false"失败
###获取某人的所有订单
url:/wx/wxpay/getonepersonall.wx<br/>
request method:post<br/>
param:json字符串：<br/>
{<br/>
	"thirdsessionid":"test",<br/>
}<br/>
return:字符串："fail"重置失败 "lose"3rd_sessionID失效 
   正常：json格式字符串<br/>
   [<br/>
   {"address":"中国",//地址<br/>
   "express_company_id":"SF",//快递公司ID<br/>
   "freight":1200,//运费价格<br/>
   "goods_id":"1231asdasdasd",//货物ID<br/>
   "goods_img":"E:\\ideawk\\xcxlx\\out\\artifacts\\xcxlx_war_exploded\\images/1.jpg",<br/>
   "goods_name":"沙发上",//货物名<br/>
   "goods_number":2,//购买数量<br/>
   "goods_price":1200,//货物价格 单位 分<br/>
   "order_id":"akslud",//订单编号 后台生成<br/>
   "order_status":0,//订单状态 0未支付成功 1支付成功 2订单完成<br/>
   "order_time":1510987445000,//订单时间戳<br/>
   "order_wx_id":"12312",//微信订单号<br/>
   "phone":"11122233355",//用户电话号码<br/>
   "user_add_message":"啊实打实",//用户备注<br/>
   "user_name":"王五"}//用户姓名<br/>
   ]
###商家获取所有订单
url:/wx/order/get.wx<br/>
request method:post<br/>
param:无<br/>
return:json格式字符串<br/>
   [<br/>
   {"address":"中国",//地址<br/>
   "express_company_id":"SF",//快递公司ID<br/>
   "freight":1200,//运费价格<br/>
   "goods_id":"1231asdasdasd",//货物ID<br/>
   "goods_img":"E:\\ideawk\\xcxlx\\out\\artifacts\\xcxlx_war_exploded\\images/1.jpg",<br/>
   "goods_name":"沙发上",//货物名<br/>
   "goods_number":2,//购买数量<br/>
   "goods_price":1200,//货物价格 单位 分<br/>
   "order_id":"akslud",//订单编号 后台生成<br/>
   "order_status":0,//订单状态 0未支付成功 1支付成功 2订单完成<br/>
   "order_time":1510987445000,//订单时间戳<br/>
   "order_wx_id":"12312",//微信订单号<br/>
   "phone":"11122233355",//用户电话号码<br/>
   "user_add_message":"啊实打实",//用户备注<br/>
   "user_name":"王五"}//用户姓名<br/>
   ]
###用户和商户删除订单
url:/wx/order/delete.wx<br/>
request method:post<br/>
param:json字符串：<br/>
{<br/>
	"order_id":"akslud",//订单号<br/>
}<br/>
return: 字符串："true"成功，"false"失败
###微信支付(未测试)
url:/wx/wxpay/pay.wx<br/>
request method:post<br/>
param:json字符串：<br/>
{<br/>
    "thirdsessionid":"",<br/>
    "goods_id":",<br/>
    "goods_number":2,<br/>
    "user_add_message":",<br/>
    "address_id":"",<br/>
    "express_company_id":"SF"<br/>
}<br/>
{<br/>
    "thirdsessionid":"test",<br/>
    "express_company_id":"SF",<br/>
    "address_id":"asdasd",<br/>
    "user_add_message":"asdasdasd",<br/>
    "goods_array":[<br/>
        {<br/>
            "goods_id":"1231asdasdasd",<br/>
            "goods_number":1<br/>
        }<br/>
    ]<br/>
}<br/>
return:正常：json字符串：<br/>
{<br/>
    "nonceStr":"123asdjashdj123",<br/>
    "prepay_id":"wx1239128371823",<br/>
    "timeStamp":"1283761273",<br/>
    "paySign":"asdasasd"<br/>
}<br/>

* 签名使用MD5！！！

不正常：<br/>
|值 | 含义 |
|---------------------|----------------------|
|{"error":"lake","goods_id":""}|缺货 缺货的货品ID|
|{"error":"lose"}|3rd_sessionid失效|
|其他|第三方服务器与微信服务器交互错误|

