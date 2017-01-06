require "init"
local rootView
CardFragment=luajava.bindClass("com.sharemob.tinchat.modules.feeling.CardFragment")
CardDataItem=luajava.bindClass("com.sharemob.tinchat.modules.feeling.CardDataItem")
--dataList =ArrayList()
--imagePaths=ArrayList()
--
--imagePaths.add("http://img1.mm131.com/pic/2325/2.jpg")
--imagePaths.add("http://img1.mm131.com/pic/1892/3.jpg")
--imagePaths.add("http://img1.mm131.com/pic/1892/10.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
--imagePaths.add("http://img1.mm131.com/pic/2235/20.jpg")
layout_header=function(title)
	layout=	{
		FrameLayout,
		id="activity_header",
		layout_width="match_parent",
		layout_height=dimens.dx_45,
		layout_gravity="top",
		background="#fb5552",
		{
			TextView,
			id="tv_title_content",
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="center",
			text=title,
			textColor="#ffffff",
			textSize="16sp"
		}
	}
	return layout
end
--prepareDataList=function()
--			for i=0,imagePaths.size()-1,1 do
--				local dataItem =CardDataItem();
--				dataItem.userName = "leehongee";
--				dataItem.urls=imagePaths;
--				dataItem.imagePath = imagePaths.get(i);
--				dataItem.likeNum = 1;
--				dataItem.imageNum =1;
--				dataItem.signature="做爱不带套,爽死你";
--				dataItem.time="2014-08-09";
--				dataItem.location="南山区海岸城万达影院";
--				dataList.add(dataItem);
--			end
--end

initView=function(rootView,json)
	slidePanel=rootView.findViewById(R.id.image_slide_panel)
	cardSwitchListener=luajava.createProxy("com.sharemob.tinchat.modules.feeling.CardSlidePanel$CardSwitchListener",{
	onShow=function(index)
	
	end;
	onCardVanish=function( index,  type)
	
	end;
	onItemClick=function(cardView,index)
	
	end
	})
	slidePanel.setCardSwitchListener(cardSwitchListener);
	slidePanel.fillData(json);
end

function onCreate(bundle)

	rootView = LocalUtils.viewFromInflater(this, R.layout.card_slide_layout);
end

function onActivityCreated(savedInstanceState)

	local oncomplete=function(body)
		println(body)
		initView(rootView,body)
	end
	getHttp("http://172.16.4.37/api/user?cmd=10008",oncomplete)
end

function onCreateView(inflater, container,savedInstanceState)
      
      return rootView  
end
