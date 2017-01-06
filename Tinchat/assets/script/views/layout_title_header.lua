require "init"
local TitleHeader=class("TitleHeader")

function TitleHeader:create(tHeader)
	self.tHeader=tHeader
	self.layout={
			FrameLayout,
			id="activity_header",
			layout_width="match_parent",
			layout_height=dimens.dx_45,
			layout_gravity="top",
			background=Color.TitleBgColor,
			{
				TextView,
				id="tv_title_content",
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_gravity="center",
				text=tHeader.title,
				textStyle="bold",
				textColor=Color.TitleTextColor,
				textSize="16sp"
			},
			{
				ImageView,
				id="iBtn_title_back",
				layout_width="25dp",
				layout_height="25dp",
				layout_gravity="left|center_vertical",
				layout_marginLeft="10dp",
				visibility="gone",
--				background="drawable/btn_public_back_normal.png",
				onClick=luajava.createProxy("android.view.View$OnClickListener",{onClick=function(v)
					if (tHeader.leftListenerFunc~=nil) then
						tHeader.leftListenerFunc()
					end
				end})
			},
			{
				ImageView,
				id="iBtn_title_right",
				layout_width="45dp",
				layout_height="45dp",
				layout_gravity="right|center_vertical",
				layout_marginLeft="5dp",
				visibility="gone",
--				background="drawable/ic_title_more.png",
				onClick=luajava.createProxy("android.view.View$OnClickListener",{onClick=function(v)
					if (tHeader.rightListenerFunc~=nil) then 
						tHeader.rightListenerFunc()
					end
				end})
				}
	}
end

function TitleHeader:initComponent(btn_title_back,iBtn_title_right)
	local localUtils = luajava.bindClass("com.androlua.LuaUtil")
	--左按钮
	if (self.tHeader.leftListenerFunc~=nil) then
		setDrawableButton(btn_title_back,"btn_public_back_normal","btn_public_back_pressed",nil)
	else
		btn_title_back.setVisibility(View.GONE)
	end
	
	--右按钮
	if(self.tHeader.rightListenerFunc~=nil) then
		setDrawableButton(iBtn_title_right,"ic_title_more","ic_title_more",nil)
	else
		iBtn_title_right.setVisibility(View.GONE)
	end
end

return TitleHeader