require "init"

SelectedPhotoAdapter=luajava.bindClass("com.sharemob.tinchat.modules.dynamic.SelectedPhotoAdapter")
LayoutParams=luajava.bindClass("android.view.ViewGroup.LayoutParams")
local main={}

local pageChangeListener
local pager
local adapter
local bitmap=ArrayList()
local listViews

local function loadlayout()
	layout={
		RelativeLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		orientation="vertical",
		{
			ViewPager,
			id="viewpager",
			layout_width="match_parent",
			layout_height="match_parent"
		}
	}

	this.setContentView(loadlayout(layout,main))

	pageChangeListener=luajava.createProxy("android.support.v4.view.ViewPager$OnPageChangeListener",{
		onPageSelected=function(arg0)
	
		end;
		onPageScrolled=function(arg0,arg1,arg2)
		
		end;
		onPageScrollStateChanged=function(arg0)
		
		end
		
	})
	
	
	local function initListViews(mp)
		if (listViews == nil) then
			listViews =ArrayList()
		end
		local img =ImageView(this)
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		listViews.add(img)
	end
	
	pager=main.viewpager
	pager.setOnPageChangeListener(pageChangeListener);
	
	for i=0, bitmap.size(),1 do
		initListViews(bitmap.get(i))
	end
	
		adapter = SelectedPhotoAdapter(listViews);-- 构造adapter
		pager.setAdapter(adapter);-- 设置适配器
		local intent = getIntent();
		local id = intent.getIntExtra("ID", 0);
		pager.setCurrentItem(id);
end

function onCreate(savedInstanceState)
	loadlayout()

end

function finish()
	this.finish() 
	this.overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end