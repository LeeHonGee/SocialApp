
function OptionIncome()
	local items={"请选择","10001-2000元", "20001-3000元", "30001-5000元", "50001-8000元",
				"80001-10000元","100001-15000元","150001-20000元","200001-50000元","200001-50000元","50000以上"}
	LocalUtils.SingleWheelDailog(this,"月收入",items,"item_income")
end

function OptionMarriage()
	local items={"请选择","未婚","单身", "离异", "丧偶", "已婚"}
	LocalUtils.SingleWheelDailog(this,"婚姻状况",items,"item_marital_status")
end

function OptionEducation()
	local items={"请选择","高中及以下","中专","大专","大学本科","硕士","博士"}
	LocalUtils.SingleWheelDailog(this,"学历",items,"item_education")
end

function OptionBodily()
	local items={"请选择","一般","瘦长","运动员型","比较胖","体格魁梧","苗条","高大美丽","丰满性感","富线条美","壮实"}
	LocalUtils.SingleWheelDailog(this,"体型",items,"item_bodily")
end

function OptionBuyACar()
	local items={"请选择","未购车","已购车","单位用车","需要时购置"}
	LocalUtils.SingleWheelDailog(this,"购车情况",items,"item_hasCar")
end

function OptionHasHouse()
	local items={"请选择","保密","以后再告诉你","与父母同住","租房","已购房(有贷款)","已购房(无贷款)","住单位房","住亲朋家","需要时购置"}
	LocalUtils.SingleWheelDailog(this,"住房情况",items,"item_hasHouse")
end

function OptionProfession()
	local items={"请选择","计算机／互联网", "电子电器／通信技术", 
				"机械／仪器仪表", "销售","客服及技术支持","财务／审计／税务","证券／金融／投资／银行",
				"保险","生产／运营","质量／安全管理","工程／能源","贸易／采购","物流／仓储／运输","技工","化工／环保",
				"生物／制药／医疗器械","医院／医疗／护理","广告","市场／公关","影视／媒体","写作／出版印刷","翻译",
				"艺术／设计／创意","建筑／房地产／装饰装修","物业管理","人力资源","高级经营管理","行政／后勤","咨询／顾问",
				"律师／法务／合规","教育／培训","科研","酒店／旅游／餐饮／娱乐","美容／健身","商业零售服务","交通运输服务",
				"保安／家政服务","警察／其他","公务员","运动员","自由职业／兼职","储备干部／培训生／实习生","在校学生","退休","其他"}
	LocalUtils.SingleWheelDailog(this,"职业职务",items,"item_profession")
end

function OptionFellowTownsman()
	local items={UserInfo.province,UserInfo.city,UserInfo.district}
	LocalUtils.OptionLocation(this,items,"item_fellow_townsman")
end

function OptionStature()
	local items={}
	LocalUtils.SingleWheelDailog(this,"月收入",items,"item_income")
end

function OptionAge()
	local items={}
	LocalUtils.SingleWheelDailog(this,"月收入",items,"item_income")
end

function OptionReport(uid)
	local array={"举报"}
	LocalSheetDialog(function(which)
		if(which==1)then
			startActivity("ReportActivity",cjson.encode({uid=uid}))
		end
	end,array)
end

function OptionWheelDailogForChatting(uid,sid)
	local array={"TA的主页","清除聊天记录","举报"}
	LocalSheetDialog(function(index)
		if(index==3)then
			startActivity("ReportActivity",cjson.encode({uid=uid}))
		elseif(index==2)then
			local url=URL_User(10062,cjson.encode({senduid=sid,revuid=UserInfo.ID}))
			AsyncHttp(url,function(text)
				println(text)
			end)
		elseif(index==1)then
			startActivity("MyspaceActivity",cjson.encode({uid=uid}))
		end
	end,array)
end