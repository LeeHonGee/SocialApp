require "init"
ImagePickerAdapter=luajava.bindClass("com.sharemob.tinchat.lib.adapter.ImagePickerAdapter")
SpaceItemDecoration=luajava.bindClass("com.sharemob.tinchat.component.SpaceItemDecoration")
LocalGridLayoutManager=luajava.bindClass("com.sharemob.tinchat.component.LocalGridLayoutManager")
LocalScrollview=luajava.bindClass("com.sharemob.tinchat.component.LocalScrollview")
local main={}
local imagePickerAdapter=nil
local list = ArrayList()
local File=luajava.bindClass("java.io.File")
local urlList = ArrayList()
local Uri=luajava.bindClass("android.net.Uri")


function finish()
	this.finish()
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

local Top_TitleBar=function()
	local layout={
			FrameLayout,
			layout_width="match_parent",
			layout_height=dimens.dx_45,
			layout_gravity="top",
			background=Color.TitleBgColor,
			{
				TextView,
				id="tv_title_content",
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_gravity="left|center_vertical",
				layout_marginLeft="60dp",
				text="所有图片",
				textColor=Color.TitleTextColor,
				textSize="16sp"
			},
			{
				FrameLayout,
				layout_width="50dp",
				layout_height="fill",
				layout_gravity="left|center_vertical",
				{
					ImageView,
					layout_width="25dp",
					layout_height="25dp",
					layout_gravity="center_vertical|center_horizontal",
					background="drawable/abc_ic_ab_back_mtrl_am_alpha.png"
				},
				{
					ColorButton,
					alpha="0.3",
					id="iBtn_title_back",
					layout_width="match_parent",
					layout_height="match_parent",
					gravity="vertical"
				}
			},
			{	
				FrameLayout,
				layout_width="wrap",
				layout_height="fill",
				layout_marginRight="10dp",
				layout_gravity="right|center_vertical",
				{
					ColorButton,
					text="发送",
					paddingTop="7dp",
					paddingBottom="7dp",
					paddingLeft="15dp",
					paddingRight="15dp",
					id="iBtn_title_right",
					layout_width="match_parent",
					layout_height="wrap",
					textColor=Color.White,
					enabled=true,
					layout_gravity="center_vertical",
					textSize="15sp"
				}
			}
	}
	return layout
end

Bottom_TitleBar=function()
	local layout={
			FrameLayout,
			layout_width="match_parent",
			layout_height=dimens.dx_45,
			layout_gravity="bottom",
			alpha="1.0",
			background="#2e3334",
			{
				ColorButton,
				text="图片",
				paddingTop="7dp",
				paddingBottom="7dp",
				paddingLeft="20dp",
				paddingRight="20dp",
				id="iBtn_titlebar_imagepath",
				layout_width="wrap",
				layout_height="fill",
				textColor=Color.White,
				layout_gravity="left|center_vertical",
				textSize="15sp"
			},
			{
				ColorButton,
				text="预览",
				layout_marginRight="10dp",
				paddingTop="7dp",
				paddingBottom="7dp",
				paddingLeft="15dp",
				paddingRight="15dp",
				id="iBtn_titlebar_preview",
				layout_width="wrap",
				layout_height="wrap",
				textColor=Color.White,
				layout_gravity="right|center_vertical",
				textSize="15sp"
			}
	}
	return layout
end

function onLayout()
	layout={
		FrameLayout,
		layout_width="fill",
		layout_height="match_parent",
		orientation="vertical",
		Top_TitleBar(),
		{
			LocalScrollview,
			layout_width="match_parent",
			layout_height="match_parent",
			layout_marginTop="45dp",
			layout_marginBottom="18dp",
			{
				RecyclerView,
				id="imagepicker_grid",
				scrollbars="vertical",
				background="#191919",
				layout_width="match_parent",
				layout_height="match_parent"
			}
		},
		Bottom_TitleBar()
	}
	this.setContentView(loadlayout(layout,main))
end

function onActivityResult(requestCode,resultCode,data) 
	dp =this.getResources().getDimension(R.dimen.dp);
	local browserList=CacheManager.getInstance().browserList;
	if(requestCode==SMGlobal.LOCAL_TAKE_PICTURE) then
		if (browserList.size() < 6 and resultCode == -1)then-- 拍照
			local file=File(SMGlobal.PICTURE_PATH);
			local uri=Uri.fromFile(file);
			LocalUtils.startPhotoZoom(this,browserList,uri,SMGlobal.LOCAL_CUT_PHOTO_REQUEST_CODE);
		end
	elseif(requestCode==SMGlobal.TAKE_PICTURE) then
		if (browserList.size() < 6 and resultCode == -1)then-- 拍照
--				local bundle=data.getExtras();
--				local path=bundle.getString(SMGlobal.PICTURE_PATH);
--				
--				local photoUri=Uri.parse(path);
--				LocalUtils.startPhotoZoom(this,browserList,photoUri,SMGlobal.CUT_PHOTO_REQUEST_CODE);
				println("--"..SMGlobal.PICTURE_PATH)
				ImagePickerAdapter.SendTakePicture(this,SMGlobal.PICTURE_PATH,imagePickerAdapter.upload_type)
				this.finish()
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
				println(SMGlobal.EXTRA_OUTPUT_PATH)
--				urlList.add(SMGlobal.EXTRA_OUTPUT_PATH)
--				LocalUtils.setGridView(this,listView, urlList);
		end
	elseif(requestCode==SMGlobal.LOCAL_RESULT_LOAD_IMAGE) then
		if (browserList.size() < 6 and resultCode == Activity.RESULT_OK and nil ~= data) then --相册返回
			local uri = data.getData();
			if (uri ~= nil) then
				LocalUtils.startPhotoZoom(this,browserList,uri,SMGlobal.LOCAL_CUT_PHOTO_REQUEST_CODE);
			end
		end
	elseif(requestCode==SMGlobal.LOCAL_CUT_PHOTO_REQUEST_CODE) then
		if (resultCode == Activity.RESULT_OK and nil ~= data) then -- 裁剪返回
		
--				local bitmap =LocalUtils.getBitmapFromUri(this,SMGlobal.EXTRA_OUTPUT_PATH)
--				local avatar= Bimp.createFramedPhoto(480, 480, bitmap,(dp * 1.0))
--				main.item_avatarOfitem_midIcon.setBackgroundDrawable(LocalUtils.BitmapToDrawable(avatar))
--				local avatar= Bimp.createFramedPhoto(480, 480, bitmap,(dp * 1.0))
--				main.user_avatar.setImageBitmap(bitmap)
--				main.layout_header_bg.setImageBitmap(avatar)
				

--				displayImage(SMGlobal.EXTRA_OUTPUT_PATH,main.user_avatar)
--				displayImage(SMGlobal.EXTRA_OUTPUT_PATH,main.layout_header_bg)
				println(SMGlobal.PICTURE_PATH)
				println(SMGlobal.EXTRA_OUTPUT_PATH)
				ImagePickerAdapter.SendTakePicture(this,SMGlobal.EXTRA_OUTPUT_PATH,imagePickerAdapter.upload_type)
				this.finish()
		end
	end
end

function onController()
	--返回按钮
	setColorButton(main.iBtn_title_back,'#e8e8e8',Color.transparent,
	function()
		println("onClickFunc")
		this.finish()
	end,0)
	--发送按钮
	local iBtn_title_right=main.iBtn_title_right
	setColorButton(iBtn_title_right,'#098708','#19ad18',
	function()
		println("onClickFunc")
		imagePickerAdapter.SendSelectedImages(UserInfo.uid)
		finish()
	end,5)
	
	--显示图片布局
	local imagepicker_grid=main.imagepicker_grid
	imagepicker_grid.setHasFixedSize(true);
	local spanCount=3
--	local layoutManager =StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
	local layoutManager =LocalGridLayoutManager(this,3)
	layoutManager.setOrientation(LocalGridLayoutManager.VERTICAL);
	layoutManager.setSmoothScrollbarEnabled(true);
	imagepicker_grid.setLayoutManager(layoutManager);
	
	imagePickerAdapter=ImagePickerAdapter(this)
	local text=this.getIntent().getStringExtra("parameters")
	println("------"..text)
	local json=cjson.decode(text)
	imagePickerAdapter.setActionName(json.action,json.upload_type)
	imagepicker_grid.setAdapter(imagePickerAdapter);
	imagePickerAdapter.notifyDataSetChanged();
	imagePickerAdapter.loadPhotoAlbum()
	--注入预览按钮监听事件
	local iBtn_titlebar_preview=main.iBtn_titlebar_preview
	setColorButton(iBtn_titlebar_preview,'#098708','#19ad18',
	function()
		println("onClickFunc")
		imagePickerAdapter.previewSelectedPictures()
	end,5)
	--选择图片路径
	local iBtn_titlebar_imagepath=main.iBtn_titlebar_imagepath
	setColorButton(iBtn_titlebar_imagepath,'#a9a9a9',Color.transparent,
	function()
		println("onClickFunc")
	end,0)
	--预览
	local iBtn_titlebar_preview=main.iBtn_titlebar_preview
	setColorButton(iBtn_titlebar_preview,'#098708',Color.transparent,
	function()
		println("onClickFunc")
		imagePickerAdapter.previewSelectedPictures()
	end,5)
	
	--适配器注入按钮事件
	imagePickerAdapter.setInjectEvent(iBtn_title_right,iBtn_titlebar_preview,iBtn_titlebar_imagepath)
end

function onCreate(savedInstanceState)
	onLayout()
	onController()
end
