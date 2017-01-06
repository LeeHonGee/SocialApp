require "init"

local main_wheel_datepicker={}
local wheel_datepicker

local 	layout_wheel_datepicker={
	LinearLayout,
	layout_width="fill_parent",
	layout_height="wrap_content",
	orientation="vertical",
	background=Color.White,
	paddingBottom="10dp",
	{
		RelativeLayout,
		layout_width="fill_parent",
		{
			TextView,layout_width="match_parent",layout_height="wrap_content",layout_centerInParent="true",gravity="center_horizontal",
			padding="10dp",text="*选择出生日期，系统将会自动转换为年龄及星座",textColor="#bababa",textStyle="italic",textSize="12sp"
		}
	},
	{
		LinearLayout,layout_width="fill_parent",layout_height="fill_parent",gravity="center_vertical",orientation="horizontal" ,
		{
			WheelView,id="year",layout_width="fill_parent",layout_height="wrap_content",layout_gravity="center",layout_weight=1
		},
		{
			WheelView,id="month",layout_width="fill_parent",layout_height="wrap_content",layout_gravity="center",layout_weight=1
		},
		{
			WheelView,id="day",layout_width="fill_parent",layout_height="wrap_content",layout_gravity="center",layout_weight=1
		}
	}
}

function finish()
	this.finish() 
	this.overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
end

function onKeyDown(keyCode,event)
	if(keyCode == KeyEvent.KEYCODE_BACK) then
		finish()
	end
end

local function layout()

	local layout_title_header=require "layout_title_header".new()
	layout_title_header:create({title="设置生日",leftListenerFunc=function() finish()   end,rightListenerFunc=nil})
	
	cell_layout=function(cell_id,item)
		
		local sex_val=function(cell_id)
			local age=tostring(LocalUtils.calculateDatePoor(UserInfo.birthday))
--			local constellation=LocalUtils.getConstellation(1,1)
			local constellation=LocalUtils.constellation(UserInfo.birthday)
			if cell_id=="item_age" then
				if(UserInfo.age==nil) then
					return string.format("%d",age)
				end
				return string.format("%d",UserInfo.age)
			elseif cell_id=="item_constellation" then
				if(UserInfo.constellation==nil) then
					return constellation
				end
				return UserInfo.constellation
			end
		end
		cell={
			LinearLayout,
			layout_width="fill",
			layout_height="fill",
			orientation="vertical",
			{
				FrameLayout,
				id=cell_id,
				layout_width="match_parent",
				layout_height="wrap_content",
				padding="14dp",
				layout_gravity="top",
				background=Color.White,
				{
					TextView,
					layout_width="wrap_content",
					layout_height="wrap_content",
					layout_marginRight="20dp",
					textColor="#535b5e",
					text=item.left_name,
					textSize="16sp",
					textStyle="normal",
					layout_gravity="left|center_vertical"
				},
				{
					TextView,
					id=string.format("%sOf%s" ,cell_id,"item_left_name"),
					layout_width="wrap_content",
					layout_height="wrap_content",
					gravity="center_vertical",
					layout_marginRight="20dp",
					text=sex_val(cell_id),
					layout_gravity="right|center_vertical"
				}
			},
			line
		}
		return cell
	end

	cell_item_age=cell_layout('item_age',{left_name="年龄"})
	cell_item_constellation=cell_layout('item_constellation',{left_name="星座"})
	
	layout={
			RelativeLayout,
			layout_width="match_parent",
			layout_height="match_parent",
			background=Color.WhiteSmoke,
			{
				LinearLayout,
				layout_width="fill",
				layout_height="fill",
				orientation="vertical",
				layout_title_header.layout,
				{
					LinearLayout,
					layout_width="match_parent",
					layout_height="wrap_content",
					background=Color.WhiteSmoke,
					orientation="vertical",
					cell_item_age,
					cell_item_constellation
				}
			},
			{
				LinearLayout,
				id="layout_timepicker",
				layout_alignParentBottom="true",
				layout_width="fill_parent",
				layout_height="wrap_content",
				background=Color.WhiteSmoke,
				orientation="vertical"
			}
	}

	main={}
	this.setContentView(loadlayout(layout,main))
	layout_title_header:initComponent(main.iBtn_title_back,main.iBtn_title_right)

	
	
	wheel_datepicker=loadlayout(layout_wheel_datepicker,main_wheel_datepicker)
	local wheel_year=main_wheel_datepicker.year
	local wheel_month=main_wheel_datepicker.month
	local wheel_day=main_wheel_datepicker.day
	local  scrollListener=luajava.createProxy("com.sharemob.tinchat.component.wheeldialog.OnWheelScrollListener",{
	onScrollingStarted=function(wheelView)
	
	
	end;
	onScrollingFinished=function(wheel)
			local n_year = wheel_year.getCurrentItem() + 1950
			local n_month = wheel_month.getCurrentItem() + 1
			local n_day= wheel_day.getCurrentItem()+1
			LocalUtils.initWheelViewDay(this,wheel_day,n_year,n_month)
			
			local birthday=LocalUtils.getBirthday(wheel_year.getCurrentItem(),wheel_month.getCurrentItem(),wheel_day.getCurrentItem())
			
			local age=tostring(LocalUtils.calculateDatePoor(birthday))
			local constellation=LocalUtils.getConstellation(n_month,n_day)
			
			println(birthday)
			println(age)
--			println(constellation)
			CacheManager.getInstance().userInfo.birthday=birthday
			--更新缓存
			if(UserInfo.birthday~=nil) then
				local url=URL_User(10003,cjson.encode({uid=UserInfo.uid,birthday=birthday}))
				http.get(url)
			end
		
			
			sendBroadcastOption("item_birthday",birthday)
			sendBroadcastOption("item_age",age)
			sendBroadcastOption("item_constellation",constellation)
			main.item_ageOfitem_left_name.setText(age)
			main.item_constellationOfitem_left_name.setText(constellation)
			
			UserInfo.age=age
			UserInfo.constellation=constellation
			UserInfo.birthday=birthday
	end
	
	})
	local birthday=UserInfo.birthday
	local view_wheel_datepicker=LocalUtils.getLayoutDatePicker(this,wheel_datepicker,wheel_year,wheel_month,wheel_day,birthday,scrollListener)
	main.layout_timepicker.addView(view_wheel_datepicker)
	
--	local constellation=LocalUtils.constellation(t_info.birthday)
end


function onCreate(savedInstanceState)
	layout()

end