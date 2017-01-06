local format=string.format


ACTIONO_OPTION_UPDATE_USERINFO="sm.action.update.userinfo"
ACTIONO_DELETE_PHTOTO="sm.action.delete.photo"
BUNDLE_ITEM_KEY="item_id"
BUNDLE_ITEM_VALUE="item_value"


ScaleType={
	MATRIX=0,
	FIT_XY=1,
	FIT_START=2,
	FIT_CENTER=3,
	FIT_END=4,
	CENTER=5,
	CENTER_CROP=6,
	CENTER_INSIDE=7
}

RegChannel={
	WeiXin=1,Phone=2
}


Me=0
Other=1
Color={ 
	Back="#000000",
	Black="#000000",
	White="#ffffff",
	textgrey="#535b5e",
	slategray="#535b5e",
	grey="#808080",
	slategrey="#535b5e",
	WhiteSmoke="#EBEBEB",
--	TitleBgColor="#fb5552",
	TitleBgColor="#ffdc1a",
	TitleTextColor="#000000",
	transparent="#00000000",
	ChatingBG="#f0f0f4"
}

Gender={MALE=1,FEMALE=0}
Gender[1]="男"
Gender[0]="女"
Gender["男"]=1
Gender["女"]=0


LocalView={
	VISIBLE=0x00000000,INVISIBLE = 0x00000004,GONE = 0x00000008
}

dimens ={ 
	dx_45="45dp",
	dx_46="46dp",
	dx_47="47dp",
	dx_48="48dp",
	dx_49="49dp",
	dx_50="50dp",
	dx_51="51dp"
}


account= {
	id="dc8a4e75f874779b3bb4ddca185d18c2",
	sex=1,
	age=21,
	constellation="巨蟹座♋"
}

address=CacheManager.IP
--address="192.168.0.104"

ActivityName="AppActivity"

--URL_Login=function(phone,password) 
--	return format("http://%s/api/user?cmd=%d&account_phone=%s&account_pwd=%s",address,10004,phone,password) 
--end

URL_User=function(cmd,data)
	return format("http://%s/api/user?cmd=%d&data=%s",address,cmd,data) 
end

URL_APP=function(cmd,data)
	return format("http://%s/api/app?cmd=%d&data=%s",address,cmd,data) 
end

--URL_Feedback=function(data)
--	return format("http://%s/api/app?cmd=%d&data=%s",address,2003,data)
--end

--URL_Updater=function(version)
--	return format("http://%s/api/app?cmd=%d&version=%s",address,2002,version)
--end

--URL_Latest_Login=function()
--	return format("http://%s/api/user?cmd=%d&account=%s",address,10019,"asdasdasj29299asdh")
--end

--URL_Update_UserInfo=function(userInfo)
--	return format("http://%s/api/user?cmd=%d&userInfo=%s",address,10003,userInfo)
--end

URL_Member_BannerAD=function()
	return format("http://%s/api/app?cmd=%d",address,2006) 
end

--URL_Change_Phone=function(prameter)
--	return format("http://%s/api/user?cmd=%d&myPhone=%s&meID=%s&otherID=%s",address,10018,prameter.phone,prameter.meID,prameter.otherID) 
--end

--URL_Send_Sayhi=function(prameter)
--	return format("http://%s/api/user?cmd=%d&meID=%s&otherID=%s",address,10016,prameter.meID,prameter.otherID) 
--end

--URL_Send_Attention=function(prameter)
--	return format("http://%s/api/user?cmd=%d&meID=%s&otherID=%s",address,10045,prameter.meID,prameter.otherID) 
--end

--URL_Send_Loving=function(prameter)
--	return format("http://%s/api/user?cmd=%d&accountID=%s",address,10012,prameter.accountID) 
--end

--URL_Load_UserInfo=function(prameter)
--	return format("http://%s/api/user?cmd=%d&accountID=%s",address,10012,prameter.accountID) 
--end

--URL_Message_One=function()
--	return format("http://%s/api/user?cmd=%d",address,10047) 
--end

--URL_Register=function(data)
--	return format("http://%s/api/user?cmd=%d&data=%s",address,10002,data) 
--end

--URL_verifyTelephoneExist=function(data)
--	return format("http://%s/api/user?cmd=%d&data=%s",address,10048,data) 
--end


URL_Location_regeo=function(latitude,longitude)
	return format("http://restapi.amap.com/v3/geocode/regeo?output=json&location=%s,%s&key=ee95e52bf08006f63fd29bcfbcf21df0&radius=200&extensions=all",latitude,longitude)
end

function split(str, reps)
    local resultStrsList = {};
    string.gsub(str, '[^' .. reps ..']+', function(w) table.insert(resultStrsList, w) end );
    return resultStrsList;
end

function CurrentDateTime()
    local tab=os.date("*t");
    local srcDateTime=string.format ("%d-%02d-%d %d:%02d:%02d",tab.year, tab.month, tab.day, tab.hour, tab.min, tab.sec)
    return  srcDateTime
end

function UUID()
    local template ="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
    local d = io.open("/dev/urandom", "r"):read(4)
    math.randomseed(os.time() + d:byte(1) + (d:byte(2) * 256) + (d:byte(3) * 65536) + (d:byte(4) * 4294967296))

    local uuid= string.gsub(template, "x", function (c)
        local v = (c == "x") and math.random(0, 0xf) or math.random(8, 0xb)
        return string.format("%x", v)
    end)
    return uuid
end

info={}

ShareMob_AppKey="a8a336e62ff2";
ShareMob_AppSecret="05a4f7e86010f0ed5adf7b7802251d1b";