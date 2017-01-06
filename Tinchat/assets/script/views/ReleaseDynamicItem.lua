require "init"

ImageUploadAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.ImageUploadAdapter")
CatLoadingView=luajava.bindClass("com.sharemob.tinchat.lib.catloading.CatLoadingView")
--ImageUploadAdapter.Limit=9

local progressLoadingDialog 
local imageUploadAdapter
local locationAddress="来自火星#唐人街"
local main={}

function finish()
	this.finish()
end

--local function registerOptionUpateUserInfo()
--	local filter =IntentFilter()
--	filter.addAction(SMGlobal.CHATING_IMAGE_ACTION)
--	filter.addAction(SMGlobal.UPLOAD_IMAGE_DYNAMIC_ACTION)
--	this.registerReceiver(filter)
--end

local uploadCount=0
local uploadImages={}
function onReceive(context,intent)
	local action = intent.getAction()
	if(action==SMGlobal.UPLOAD_IMAGE_DYNAMIC_ACTION)then
		local bundle = intent.getExtras()
		local array=bundle.getString(SMGlobal.IMAGE_KEY)
		local code=bundle.getInt("code")
		if(array~=nil) then
			imageUploadAdapter.addArray(array)
		elseif(code==200) then
			uploadCount=uploadCount+1
			local size=imageUploadAdapter.getMyPhotosLocalPath().size()
			local filename=bundle.getString("name")
			table.insert(uploadImages,filename)
			println(filename)
			if(uploadCount==size)then
				local content=tostring(main["ev_content"].getText())
				println(UserInfo.uid)
				println(content)
				local images=cjson.encode(uploadImages)
				local location=tostring(main["slideSwitch_location_content"].getText())
				println(images)
				local data=cjson.encode({
				uid=UserInfo.uid,
				content=URLEncoder.encode(content, "UTF-8"),
				images=uploadImages,
				location=location})
				println(data)
				
				local url=URL_User(10020,data)
				println(url)
				local body,cookie,code,headers=http.get(url)
				println(body)
				progressLoadingDialog.dismiss()
				finish()
			end
		end
	end
end
function onResume()
	println("onResume")
end

local layout_horizontal_upload_myphoto={
		LinearLayout,
		layout_width="match_parent",
		layout_height="100dp",
		background=Color.White,
		padding="5dp",
		{
			RecyclerView,
			id="recycler_myphotos",
			scrollbars="vertical",
			gravity="center_vertical",
			layout_width="match_parent",
			layout_height="match_parent"
		}
}

local layout_header={
	FrameLayout,
	id="activity_header",
	layout_width="match_parent",
	layout_height=dimens.dx_45,
	layout_marginBottom="0dp",
	layout_gravity="top",
	background=Color.TitleBgColor,
	layout_title_image("left|center_vertical","btn_cancle","drawable/btn_public_back_normal.png"),
	{
		TextView,
		layout_width="wrap_content",
		layout_height="wrap_content",
		layout_gravity="center|center_vertical",
		textColor=Color.Back,
		textSize="17sp",
		text="分享我的生活"
	},
	layout_title_text("right|center_vertical","btn_title_right_submit","发布")
}

local function loadUploadImage()
		imageUploadAdapter=ImageUploadAdapter(this,SMGlobal.UPLOAD_IMAGE_DYNAMIC_ACTION,SMGlobal.UPLOAD_IMAGE_Dynamic)
		
		local layoutManager =StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
		
		local recycler_myphotos=main.recycler_myphotos
		recycler_myphotos.setHasFixedSize(true);
		recycler_myphotos.setLayoutManager(layoutManager);
		recycler_myphotos.setAdapter(imageUploadAdapter);
		imageUploadAdapter.notifyDataSetChanged();
end
	
local function onLoadLayout()
	layout={
		LinearLayout,
		layout_width="fill",
		layout_height="fill",
		orientation="vertical",
		background=Color.WhiteSmoke,
		layout_header,
		{
			ClearEditText,
			id="ev_content",
			layout_width="fill_parent",
			layout_height="150dp",
			textSize="14sp",
			background=Color.White,
			textColorHint=Color.textgrey,
			textColor=Color.Back,
			padding="15dp",
			hint='此时此地,想和大家分享些什么呢?',
			text="",
			gravity="fill_horizontal",
			singleLine=false
		},
		layout_horizontal_upload_myphoto,
		line,
		cell_layout_SlideSwitch("正在搜索地理位置...","slideSwitch_location"),
		line
	}

	this.setContentView(loadlayout(layout,main))
	local slideListener=luajava.createProxy("com.sharemob.tinchat.component.SlideSwitch$SlideListener",{
					open=function()
						LocalUtils.LocalLocationMap(this,main["slideSwitch_location_content"])
					end;
					close=function()
						main["slideSwitch_location_content"].setText("来自火星#唐人街")
					end
	})
	local slideSwitch_open_location=main["slideSwitch_location"]
	slideSwitch_open_location.setSlideListener(slideListener)
	slideSwitch_open_location.setState(true)
	LocalUtils.LocalLocationMap(this,main["slideSwitch_location_content"])
	--加载上传图片框
	loadUploadImage()
end


function onController()
	--返回
	local btn_title_cancle=main.btn_cancle
	setColorButton(btn_title_cancle,'#e8e8e8','#00000000',
	function()
--		finish()
		LocalUtils.LocalAlertDialog(this,"不发了","再看看","提示","你还没发布动态，确定不发布吗?")
	end,0)

	--发布
	local btn_title_right_submit=main.btn_title_right_submit
	setColorButton(btn_title_right_submit,'#e8e8e8','#00000000',
	function()
		progressLoadingDialog=CatLoadingView(this)
		progressLoadingDialog.show()
		uploadCount=0
		uploadImages=nil
		uploadImages={}
		local paths=imageUploadAdapter.getMyPhotosLocalPath()
		LocalUtils.UploadMultipartFile(this,paths,UserInfo.uid,SMGlobal.UPLOAD_IMAGE_Dynamic,SMGlobal.UPLOAD_IMAGE_DYNAMIC_ACTION);
	end,0)

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
--				local bitmap =LocalUtils.getBitmapFromUri(this,SMGlobal.EXTRA_OUTPUT_PATH)
--				local avatar= Bimp.createFramedPhoto(480, 480, bitmap,(dp * 1.0))
				
				local list={}
				table.insert(list,{filename="file://"..SMGlobal.EXTRA_OUTPUT_PATH,type=SMGlobal.Chat.Send_Image})
				local json=cjson.encode(list)
				println(json)
				imageUploadAdapter.addArray(json)
--				main["item_avatarOfitem_midIcon"].setBackgroundDrawable(LocalUtils.BitmapToDrawable(avatar))
		end
	end
end
function onCreate(savedInstanceState)
	onLoadLayout()
	onController()
--	registerOptionUpateUserInfo()
end