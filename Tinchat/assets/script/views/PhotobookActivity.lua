require "init"

local main={}
local ImageUploadAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.ImageUploadAdapter")
local uploadAdapter

function finish()
	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

local 	layout_header={
	FrameLayout,
	id="activity_header",
	layout_width="match_parent",
	layout_height=dimens.dx_45,
	layout_gravity="top",
	background=Color.TitleBgColor,
	layout_title_image("left|center_vertical","btn_cancle","drawable/btn_public_back_normal.png"),
	{
		TextView,
		id="tv_title_content",
		layout_width="wrap_content",
		layout_height="wrap_content",
		layout_gravity="center|center_vertical",
		textStyle="bold",
		textColor=Color.TitleTextColor,
		textSize="17sp",
		text="个人写真"
	}
}


function onLayout()
		local layout={
			LinearLayout,
			layout_width="match_parent",
			layout_height="match_parent",
			background=Color.WhiteSmoke,
			orientation="vertical",
			layout_header,
			{
					RecyclerView,
					id="recycler_photobooks",
					scrollbars="vertical",
					gravity="center_vertical",
					layout_width="match_parent",
					layout_height="match_parent"
			}
--			{
--				LinearLayout,
--				layout_width="match_parent",
--				layout_height="100dp",
--				padding="5dp",
--				
--			}
		}
		
		this.setContentView(loadlayout(layout,main))
end

local function registerOptionUpateUserInfo()
	local filter =IntentFilter()
	filter.addAction(SMGlobal.UPLOAD_IMAGE_MYPHOTO_ACTION)
	this.registerReceiver(filter)
end

function onReceive(context,intent)
	local action = intent.getAction()
	println(action)
	if(action==SMGlobal.UPLOAD_IMAGE_AVATAR_ACTION) then
		local bundle = intent.getExtras()
		local code=bundle.getInt("code")
		local path=bundle.getString("filename")
		local name=bundle.getString("name")
		UserInfo.avatar=name
		if(code==200) then
			displayCircleImage("file://"..path,main["item_avatarOfitem_midIcon"])
		end
	elseif(action==SMGlobal.UPLOAD_IMAGE_MYPHOTO_ACTION)then
		local bundle = intent.getExtras()
		local code=bundle.getInt("code")
		local path=bundle.getString("filename")
		local name=bundle.getString("name")
		local list={}
		table.insert(list,{filename="file://"..path,type=SMGlobal.Chat.Send_Image})
		local json=cjson.encode(list)
		println(name)
		println(json)
		UserInfo.addPhotobook(name)
		uploadAdapter.addArray(json)
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
	local btn_title_cancle=main.btn_cancle
	setColorButton(btn_title_cancle,'#e8e8e8','#00000000',
	function()
		finish()
	end,0)
	registerOptionUpateUserInfo()
	
	local recycler_photobooks=main["recycler_photobooks"]
	recycler_photobooks.setHasFixedSize(true);
--	local layoutManager =StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
	local layoutManager=GridLayoutManager(this,3)
	recycler_photobooks.setLayoutManager(layoutManager);
	
	uploadAdapter=ImageUploadAdapter(this,SMGlobal.UPLOAD_IMAGE_MYPHOTO_ACTION,SMGlobal.UPLOAD_IMAGE_MyPhoto)
	uploadAdapter.addArray(UserInfo.getPhotobooks())
	recycler_photobooks.setAdapter(uploadAdapter)
	uploadAdapter.notifyDataSetChanged();
end

function onCreate(savedInstanceState)
	onLayout()
	onController()
end

