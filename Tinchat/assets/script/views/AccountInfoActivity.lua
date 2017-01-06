require "init"
ImageUploadAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.ImageUploadAdapter")
local main={}
local uploadAdapter
local activitys={}
activitys["item_avatar"]="ImagePickerActivity"
activitys["item_nickname"]="OptionNicknameActivity"
activitys["item_telephone"]="OptionTelephoneActivity"
activitys["item_sex"]="OptionSexActivity"
activitys["item_signature"]="OptionSignatureActivity"
activitys["item_signature_video"]="OptionSignatureVideoActivity"
activitys["item_signature_voice"]="OptionSignatureVoiceActivity"
activitys["item_birthday"]="OptionBirthdayActivity"
--	activitys["item_constellation"]="OptionBirthdayActivity"
--	activitys["item_location"]="OptionLocationActivity"

local myPhotos=ArrayList()
local array_ids={
	"item_avatar",
	"item_nickname",
	"item_signature",
	"item_signature_video",
	"item_signature_voice",
	"item_sex",
	"item_birthday",
	"item_education",
	"item_income",
	"item_hasHouse",
	"item_hasCar",
	"item_fellow_townsman",
	"item_bodily",
	"item_marital_status",
	"item_profession",
	"item_marital_status"
--	"item_constellation",
--	"item_location"
}

local OptionInfo={}
OptionInfo["item_education"]=OptionEducation
OptionInfo["item_income"]=OptionIncome
OptionInfo["item_bodily"]=OptionBodily
OptionInfo["item_marital_status"]=OptionMarriage
OptionInfo["item_profession"]=OptionProfession
OptionInfo["item_hasHouse"]=OptionHasHouse
OptionInfo["item_hasCar"]=OptionBuyACar
OptionInfo["item_fellow_townsman"]=OptionFellowTownsman
	
local function registerOptionUpateUserInfo()
	local filter =IntentFilter()
	filter.addAction(SMGlobal.ACTIONO_OPTION_UPDATE_USERINFO)
	filter.addAction(SMGlobal.UPLOAD_IMAGE_AVATAR_ACTION)
	filter.addAction(SMGlobal.UPLOAD_IMAGE_MYPHOTO_ACTION)
	this.registerReceiver(filter)
end

function loadLayout()
	local layout_title_header=require "layout_title_header".new()
	layout_title_header:create({title="个人信息",
		leftListenerFunc=function() 
			finish()   
		end,
		rightListenerFunc=nil
	})
	
	local function getLocation()
		if(UserInfo.province~=nil)then
			return string.format("%s-%s",UserInfo.province,UserInfo.city)
		else
			return "定位中  ..."
		end
	end
	
	local function getEducationValue()
		if(UserInfo.marriageseeking~=nil and UserInfo.marriageseeking.education~=nil)then
			return UserInfo.marriageseeking.education
		else
			return "未填写"
		end
	end
	
	local function getIncomeValue()
		if(UserInfo.marriageseeking~=nil and UserInfo.marriageseeking.income~=nil)then
			return UserInfo.marriageseeking.income
		else
			return "未填写"
		end
	end
	
	local function getBodilyValue()
		if(UserInfo.marriageseeking~=nil and UserInfo.marriageseeking.bodily~=nil)then
			return UserInfo.marriageseeking.bodily
		else
			return "未填写"
		end
	end
	
	local function getMarital_statusValue()
		if(UserInfo.marriageseeking~=nil and UserInfo.marriageseeking.marital_status~=nil)then
			return UserInfo.marriageseeking.marital_status
		else
			return "未填写"
		end
	end
	
	local function getBodilyValue()
		if(UserInfo.marriageseeking~=nil and UserInfo.marriageseeking.bodily~=nil)then
			return UserInfo.marriageseeking.bodily
		else
			return "未填写"
		end
	end
	
	local function getprofessionValue()
		if(UserInfo.marriageseeking~=nil and UserInfo.marriageseeking.profession~=nil)then
			return UserInfo.marriageseeking.profession
		else
			return "未填写"
		end
	end
	
	local function getfellow_townsmanValue()
		if(UserInfo.marriageseeking~=nil and UserInfo.marriageseeking.fellow_townsman~=nil)then
			return UserInfo.marriageseeking.fellow_townsman
		else
			return "未填写"
		end
	end
	
	local function gethasHouseValue()
		if(UserInfo.marriageseeking~=nil and UserInfo.marriageseeking.hasHouse~=nil)then
			return UserInfo.marriageseeking.hasHouse
		else
			return "未填写"
		end
	end
	
	local function gethasCarValue()
		if(UserInfo.marriageseeking~=nil and UserInfo.marriageseeking.hasCar~=nil)then
			return UserInfo.marriageseeking.hasCar
		else
			return "未填写"
		end
	end
	
	cell_item_avatar=cell_layout('item_avatar',{left_name="头像",avatar="drawable/public_default_head.png"})
	cell_item_nickname=cell_layout('item_nickname',{left_name="昵称",right_name="请输入昵称"})
	cell_item_signature=cell_layout("item_signature",{left_name="自己介绍",right_name="你也真懒!"})
	cell_item_signature_video=cell_layout("item_signature_video",{left_name="视频介绍",right_name="尝试视频写真"})
	cell_item_signature_voice=cell_layout("item_signature_voice",{left_name="语音宣言",right_name="想说些什么呢"})
	cell_item_sex=cell_layout("item_sex",{left_name="性别",right_name="男"})
	cell_item_birthday=cell_layout("item_birthday",{left_name="出生日期",right_name="1994-03-02"})
	cell_item_constellation=cell_layout("item_constellation",{left_name="星座",right_name="射手座♐️"})
	cell_item_location=cell_layout("item_location",{left_name="居住地",right_name=getLocation()})
	cell_item_education=cell_layout("item_education",{left_name="学历",right_name=getEducationValue(),background="#fc6e27"})
	cell_item_income=cell_layout("item_income",{left_name="月收入",right_name=getIncomeValue(),background="#fc6e27"})
	cell_item_hasHouse=cell_layout("item_hasHouse",{left_name="购房情况",right_name=gethasHouseValue(),background="#fc6e27"})
	cell_item_hasCar=cell_layout("item_hasCar",{left_name="购车情况",right_name=gethasCarValue(),background="#fc6e27"})
	cell_item_fellow_townsman=cell_layout("item_fellow_townsman",{left_name="贯籍",right_name=getfellow_townsmanValue(),background="#fc6e27"})
	cell_item_bodily=cell_layout("item_bodily",{left_name="体型",right_name=getBodilyValue(),background="#fc6e27"})
	cell_item_marital_status=cell_layout("item_marital_status",{left_name="情感状态",right_name=getMarital_statusValue(),background="#fc6e27"})
	cell_item_profession=cell_layout("item_profession",{left_name="职业",right_name=getprofessionValue(),background="#fc6e27"})
--	cell_item_marital_status=cell_layout("item_marital_status",{left_name="情感状态",right_name="未填写",background="#fc6e27"})
	
	layout={
		LinearLayout,
		layout_width="fill",
		layout_height="fill",
		orientation="vertical",
		background=Color.WhiteSmoke,
		layout_title_header.layout,
		{
			LocalHoveringScrollView,
			layout_width="match_parent",
			layout_height="match_parent",
			background=Color.WhiteSmoke,
			{
			LinearLayout,
			layout_width="match_parent",
			layout_height="wrap_content",
			background=Color.WhiteSmoke,
			orientation="vertical",
			{
					LinearLayout,
					layout_width="match_parent",
					layout_height="100dp",
					padding="5dp",
					{
						RecyclerView,
						id="recycler_photobooks",
						scrollbars="vertical",
						gravity="center_vertical",
						layout_width="match_parent",
						layout_height="match_parent"
					}
			},
			line,
			layout_cellItem_title("基本资料"),line,
			cell_item_avatar,
			cell_item_nickname,
			cell_item_sex,
			cell_item_birthday,
			cell_item_constellation,
			layout_cellItem_title("详细资料"),line,
			cell_item_signature,
			cell_item_signature_video,
			cell_item_signature_voice,
			cell_item_location,
			space_item_title_layout("drawable/profile_basics_info.png","征婚资料","marriage_seeking"),
			{
				LinearLayout,
				id="marriage_seeking_layout",
				layout_width="match_parent",
				layout_height="wrap_content",
				background=Color.WhiteSmoke,
				orientation="vertical",
				cell_item_education,
				cell_item_income,
				cell_item_bodily,
				cell_item_marital_status,
				cell_item_profession,
				cell_item_hasHouse,
				cell_item_hasCar,
				cell_item_fellow_townsman
			}
		}
		}
	}
	
	this.setContentView(loadlayout(layout,main))
	layout_title_header:initComponent(main.iBtn_title_back,main.iBtn_title_right)
	local function slideSwitch(operation)
		if("open"==operation)then
			println("open")
		elseif("close"==operation) then
			println("open")
		end
	end
	
	local function updateUserInfo(data)
			local url=URL_User(10003,cjson.encode(data))
			println(url)
			http.get(url)
	end
	
	slideListener=luajava.createProxy("com.sharemob.tinchat.component.SlideSwitch$SlideListener",{
					open=function()
						println("open")
						UserInfo.marriage=1
						updateUserInfo({marriage=1,uid=UserInfo.uid})
						main["marriage_seeking_layout"].setVisibility(LocalView.VISIBLE)
					end;
					close=function()
						println("close")
						UserInfo.marriage=0
						updateUserInfo({marriage=0,uid=UserInfo.uid})
						main["marriage_seeking_layout"].setVisibility(LocalView.GONE)
					end
	})
	local marriage_seeking_switch=main["marriage_seeking_switch"]
	marriage_seeking_switch.setSlideListener(slideListener)
	
	if(UserInfo.marriage==0)then
		main["marriage_seeking_layout"].setVisibility(LocalView.GONE)
		marriage_seeking_switch.setState(false)
	elseif(UserInfo.marriage==1)then 
		main["marriage_seeking_layout"].setVisibility(LocalView.VISIBLE)
		marriage_seeking_switch.setState(true)
	end
	
	local function eventCallback(id)
		if(activitys[id]~=nil) then
			if(id=="item_avatar")then
				local parameter=cjson.encode({action=SMGlobal.UPLOAD_IMAGE_AVATAR_ACTION,upload_type=SMGlobal.UPLOAD_IMAGE_Avatar})
				OptionUpdateActivity(activitys[id],parameter)
			elseif(id=="item_sex")then
			
			else
				OptionUpdateActivity(activitys[id])
			end
		else 
			OptionInfo[id]()
		end
	end
	
	for i=1,#array_ids,1 do
		local id=array_ids[i]
		local view=main[array_ids[i]]
		SetBackgoundColor({view=view,callback=eventCallback,id=id},0xec7063,0xffffffff)
	end
	--注册更新
	registerOptionUpateUserInfo()
end

function onReceive(context,intent)
	local action = intent.getAction()
	println(action)
	if (action==SMGlobal.ACTIONO_OPTION_UPDATE_USERINFO) then
		local id=intent.getStringExtra(BUNDLE_ITEM_KEY)
		local item_id=id.."Ofitem_right_name"
		local item_value=intent.getStringExtra(BUNDLE_ITEM_VALUE)
		
		if(OptionInfo[id]~=nil) then
			local key=string.gsub(id,"item_","")
			local data={uid=UserInfo.uid}
			data[key]= URLEncoder.encode(item_value, "UTF-8")
			local url=URL_User(10049,cjson.encode(data))
			http.get(url)
			
			if(UserInfo.marriageseeking[key]==nil) then
				UserInfo[key]=item_value
			else
				UserInfo.marriageseeking[key]=item_value
			end
		end
		
		if(main[item_id]~=nil) then
			main[item_id].setText(tostring(item_value))
		end
	elseif(action==SMGlobal.UPLOAD_IMAGE_AVATAR_ACTION) then
		local bundle = intent.getExtras()
		local code=bundle.getInt("code")
		local path=bundle.getString("filename")
		local name=bundle.getString("name")
		UserInfo.avatar=name
		if(code==200) then
--			displayCircleImage("file://"..path,main["item_avatarOfitem_midIcon"])
		local account_avatar=main["item_avatarOfitem_midIcon"]
--		account_avatar.setType(RoundImageView.TYPE_ROUND);
--		account_avatar.setBorderRadius(5);
		ImageLoader.getInstance().displayImage("file://"..path,account_avatar,GlobalParams.getInstance().round_options)
		end
	elseif(action==SMGlobal.UPLOAD_IMAGE_MYPHOTO_ACTION)then
		local bundle = intent.getExtras()
		local code=bundle.getInt("code")
		local path=bundle.getString("filename")
		local name=bundle.getString("name")
		local list={}
		table.insert(list,{filename="file://"..path,type=SMGlobal.Chat.Send_Image})
		local json=cjson.encode(list)
--		println(name)
--		println(json)
		UserInfo.addPhotobook(name)
		uploadAdapter.addArray(json)
	end
end

function finish()
	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end
function onActivityResult(requestCode,resultCode,data) 
	dp =this.getResources().getDimension(R.dimen.dp);
	local browserList=CacheManager.getInstance().browserList;
	if(requestCode==SMGlobal.TAKE_PICTURE) then
		if (browserList.size() < 6 and resultCode == -1)then-- 拍照
				local bundle=data.getExtras();
				local path=bundle.getString(SMGlobal.PICTURE_PATH);
				
				local photoUri=Uri.parse(path);
				LocalUtils.startPhotoZoom(this,browserList,photoUri,SMGlobal.CUT_PHOTO_REQUEST_CODE);
		end
	elseif (requestCode==SMGlobal.RESULT_LOAD_IMAGE) then
		if (browserList.size() < 6 and resultCode == Activity.RESULT_OK and nil ~= data) then --相册返回
				local uri = data.getData();
				if (uri ~= nil) then
					LocalUtils.startPhotoZoom(this,browserList,uri,SMGlobal.CUT_PHOTO_REQUEST_CODE);
				end
		end
	elseif(requestCode==SMGlobal.CUT_PHOTO_REQUEST_CODE) then
		if (resultCode == Activity.RESULT_OK and nil ~= data) then -- 裁剪返回
				local bitmap =LocalUtils.getBitmapFromUri(this,SMGlobal.EXTRA_OUTPUT_PATH)
				local avatar= Bimp.createFramedPhoto(480, 480, bitmap,(dp * 1.0))
				main["item_avatarOfitem_midIcon"].setBackgroundDrawable(LocalUtils.BitmapToDrawable(avatar))
		end
	end
end

	
function onController()
--	loadImage("http://tupian.qqjay.com/u/2013/0227/96_13300_1.jpg",function(bitmap)
--		local user_avatar=main.item_avatarOfitem_midIcon
--		user_avatar.setBorderWidth(1)
--		user_avatar.setImageBitmap(bitmap)
--	end)
	
	
--	LocalUtils.LocalLocationMap(this,main["item_locationOfitem_right_name"])
--	println(CacheManager.getInstance().UserInfo)
	main["item_nicknameOfitem_right_name"].setText(URLDecoder.decode(UserInfo.nickname,"UTF-8"))
	main["item_sexOfitem_right_name"].setText(Gender[UserInfo.sex])
	--隐藏性别和星座左边箭头
	main["item_arrow_icon_item_sex"].setVisibility(LocalView.GONE)
	main["item_arrow_icon_item_constellation"].setVisibility(LocalView.GONE)
	
--	local constellation=LocalUtils.constellation(UserInfo.birthday)
	main["item_birthdayOfitem_right_name"].setText(UserInfo.birthday)
	main["item_constellationOfitem_right_name"].setText(UserInfo.constellation)
--	if(UserInfo.avatar~="") then
--		displayCircleImage(UserInfo.server_addr..UserInfo.avatar,main["item_avatarOfitem_midIcon"])
--	end
	if(UserInfo.signature~="") then
		main["item_signatureOfitem_right_name"].setText(URLDecoder.decode(UserInfo.signature,"UTF-8"))
	else
		main["item_signatureOfitem_right_name"].setText("你也真懒!")
	end
	
	local recycler_photobooks=main["recycler_photobooks"]
	setRecyclerViewHORIZONTAL(recycler_photobooks)
	uploadAdapter=ImageUploadAdapter(this,SMGlobal.UPLOAD_IMAGE_MYPHOTO_ACTION,SMGlobal.UPLOAD_IMAGE_MyPhoto)
	uploadAdapter.addArray(UserInfo.getPhotobooks())
	recycler_photobooks.setAdapter(uploadAdapter)
	uploadAdapter.notifyDataSetChanged();
end

function onResume()
	if(UserInfo.avatar~="") then
--		ImageUtils.loaderImageForCache(UserInfo.server_addr..UserInfo.avatar,main["item_avatarOfitem_midIcon"],false)
		displayImage(UserInfo.server_addr..UserInfo.avatar,main["item_avatarOfitem_midIcon"])
	end
--	if(UserInfo.avatar~="") then
--		loadImage(UserInfo.server_addr..UserInfo.avatar,function(bitmap)
--			main["item_avatarOfitem_midIcon"].setImageBitmap(bitmap)
--		end)
--	end
end

function onCreate()
	loadLayout()
	onController()
end
onCreate()