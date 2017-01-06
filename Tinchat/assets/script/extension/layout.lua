
cellitem_editText_layout=function(tagname,hintText,cell_id,inputType)
	cell={
		LinearLayout,
		layout_width="match_parent",
		layout_height="wrap",
		orientation="horizontal",
		background=Color.White,
		{
			TextView,
			id="title_content",
			layout_width="70dp",
			layout_height="wrap_content",
			layout_gravity="center|center_vertical",
			gravity="right",
			textStyle="bold",
			textColor=Color.TitleTextColor,
			textSize="15sp",
			layout_marginRight="10dp",
			layout_marginLeft="10dp",
			text=tagname
		},
		{
			ClearEditText,
			id=cell_id,
			layout_width="fill_parent",
			layout_height="50dp",
			textSize="13sp",
			background=Color.White,
			text="",
			textColorHint=Color.textgrey,
			textColor=Color.Back,
			paddingRight="10dp",
			paddingLeft="15dp",
			inputType=inputType,
			hint=hintText,
			gravity="center_vertical",
			singleLine=true
		}
	}
	return cell
end

cell_editText_layout=function(hintText,cell_id)
	cell={
		LinearLayout,
		layout_width="match_parent",
		layout_height="wrap",
		orientation="vertical",
		background=Color.White,
		{
			ClearEditText,
			id=cell_id,
			layout_width="fill_parent",
			layout_height="50dp",
			textSize="13sp",
			background=Color.White,
			text="",
			textColorHint=Color.textgrey,
			textColor=Color.Back,
			paddingRight="10dp",
			paddingLeft="15dp",
			inputType="phone",
			hint=hintText,
			gravity="center_vertical",
			digits="1234567890",
			singleLine=true
		}
	}
	return cell
end

--重构ListItem 布局
cell_layout=function(cell_id,item)
		local title_name_visible=function()
			if item.right_name==nil then
				return LocalView.GONE
			else
				return LocalView.VISIBLE
			end
		end
		
		local title_avatar_visible=function()
			if item.avatar==nil then
				return LocalView.GONE
			else
				return LocalView.VISIBLE
			end
		end
		
		local drawableIcon=function()
			if item.drawableIcon==nil or item.drawableIcon==""  then
				return LocalView.GONE
			else
				return LocalView.VISIBLE
			end
		end
		
		right_name_text_background=function()
			if item.background ==nil then
				return Color.Back
			else
				return item.background
			end
		
		end
		
		cell={
			LinearLayout,
			layout_width="fill",
			layout_height="wrap_content",
			orientation="vertical",
			{
				FrameLayout,
				id=cell_id,
				layout_width="match_parent",
				layout_height="wrap_content",
				padding="15dp",
				layout_gravity="top",
				background="#ffffff",
				{
					TextView,
					id="item_left_name",
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
					LinearLayout,
					layout_width="wrap_content",
					layout_height="match_parent",
					orientation="horizontal",
					layout_gravity="right|center_vertical",
					{
						ImageView,
						id=string.format("%sOfitem_midIcon" ,cell_id),
						layout_width="80dp",
						layout_height="80dp",
						layout_marginRight="30dp",
						visibility=title_avatar_visible(),
--						border_color=Color.White,
						scaleType="centerCrop",
--						border_width="0dp",
						layout_gravity="center_vertical",
						src=item.avatar
					}
					,{
						ImageView,
						visibility=drawableIcon(),
						layout_marginRight="10dp",
						layout_width="20dp",
						layout_height="20dp",
						layout_gravity="center_vertical",
						background="drawable/ic_discover_normal.png"
					}
					,{
						TextView,
						id=string.format("%sOf%s" ,cell_id,"item_right_name"),
						layout_width="wrap_content",
						layout_height="wrap_content",
						layout_marginRight="20dp",
						layout_marginLeft="100dp",
						textColor=Color.Back,
						textSize="16sp",
						text=item.right_name,
						visibility=title_name_visible(),
						textStyle="normal",
						layout_gravity="right|center_vertical"
					}
					,{
						ImageView,
						id="item_arrow_icon_"..cell_id,
						layout_width="10dp",
						layout_height="10dp",
						gravity="center_vertical",
						layout_gravity="right|center_vertical",
						background="drawable/icon_public_arrow"
					}
				}
			},
			line
		}
		
		return cell
end

function layout_title_text(cell_layout_gravity,cell_id,name)
	local layout={
		FrameLayout,
		layout_width="70dp",
		layout_height="fill",
		layout_gravity=cell_layout_gravity,
		{
			TextView,
			layout_width="wrap_content",
			layout_height="wrap_content",
			layout_gravity="center_horizontal|center_vertical",
			textStyle="bold",
			textColor=Color.TitleTextColor,
			textSize="17sp",
			text=name
		},
		{
			ColorButton,
			alpha="0.3",
			id=cell_id,
			layout_width="match_parent",
			layout_height="match_parent"
		}
	}
	return layout
end

function layout_title_image(cell_layout_gravity,cell_id,path)
	local layout={
		FrameLayout,
		layout_width="70dp",
		layout_height="fill",
		layout_gravity=cell_layout_gravity,
		{
			ImageView,
			layout_width="25dp",
			layout_height="25dp",
			layout_marginLeft="10dp",
			layout_gravity="center_vertical|left",
			background=path
		},
		{
			ColorButton,
			alpha="0.3",
			id=cell_id,
			layout_width="match_parent",
			layout_height="match_parent"
		}
	}
	return layout
end

function layout_title_image_size(cell_layout_gravity,cell_id,path,width,height)
	local layout={
		FrameLayout,
		id="layout_"..cell_id,
		layout_width="70dp",
		layout_height="fill",
		visibility=LocalView.GONE,
		layout_gravity=cell_layout_gravity,
		{
			ImageView,
			layout_width=width,
			layout_height=height,
			layout_marginLeft="10dp",
			layout_gravity="center_vertical|center_horizontal",
			background=path
		},
		{
			ColorButton,
			alpha="0.3",
			id=cell_id,
			layout_width="match_parent",
			layout_height="match_parent"
		}
	}
	return layout
end

layout_space_item=function(cell_id,title,content)
--	VISIBLE=function()
--		if(content==nil or ""==content) then
--			return LocalView.GONE
--		end
--		return LocalView.VISIBLE
--	end
	
	layout={
		LinearLayout,
		layout_width="match_parent",
		layout_height="match_parent",
		background=Color.White,
		gravity="center_vertical",
--		visibility=VISIBLE(),
		orientation="vertical",
		{
			LinearLayout,
			layout_width="match_parent",
			layout_height="match_parent",
			gravity="center_vertical",
			orientation="horizontal",
			padding="15dp",
			{
				TextView,
				id=cell_id.."_space_item_tag",
				layout_width="90dp",
				layout_height="match_parent",
				layout_gravity="left",
				gravity="center_vertical",
				text=title,
				textColor=Color.textgrey,
				textSize="16sp",
				textStyle="normal"
			},
			{
				TextView,
				id=cell_id,
				layout_width="wrap_content",
				layout_height="match_parent",
				gravity="center_vertical",
				text=content,
				textColor=Color.Back,
				textSize="16sp",
				textStyle="normal"
			}
		}
		,line
	}
	
	return layout
end

layout_cellItem_title=function(title)
	layout={
		FrameLayout,
		layout_width="match_parent",
		layout_height="40dp",
		background="#e3e3e3",
		{
			LinearLayout,
			layout_width="match_parent",
			layout_height="match_parent",
			gravity="center_vertical",
			orientation="horizontal",
			padding="5dp",
			{
				TextView,
				gravity="center_vertical",
				text=title,
				textColor=Color.Back,
				textSize="14sp",
				textStyle="normal"
			}
		}
	}
	return layout
end

space_item_title_layout=function(drawableLeft,title,title_id)
	layout={
		FrameLayout,
		layout_width="match_parent",
		layout_height="40dp",
		background="#e3e3e3",
		{
			LinearLayout,
			layout_width="match_parent",
			layout_height="match_parent",
			gravity="center_vertical",
			orientation="horizontal",
			padding="5dp",
			{
				ImageView,
				layout_width="32dp",
				layout_height="32dp",
				layout_centerVertical="true",
				background=drawableLeft
			}
			,{
				TextView,
				id=title_id.."title",
				gravity="center_vertical",
				text=title,
				textColor=Color.Back,
				textSize="14sp",
				textStyle="normal"
			}
		}
		,{
				SlideSwitch,
				id="marriage_seeking_switch",
				layout_width="50dp",
				layout_height="24dp",
				isOpen=false,
				shape="circle",
				themeColor="#ff8733",
				layout_gravity="right|center_vertical",
				layout_marginRight="15dp",
				background="drawable/btn_public_back_normal.png"
			}
--		,{
--			TextView,
--			id="btn_editor_myInfo",
--			layout_gravity="center|right",
--			layout_marginRight="10dp",
--			gravity="center_vertical",
--			text="编辑",
--			textColor=Color.Black,
--			textSize="16sp",
--			textStyle="normal"
--		}
	}
	return layout
end

cell_layout_SlideSwitch=function(title,cell_id)
		cell={
			FrameLayout,
			id="activity_header",
			layout_width="match_parent",
			layout_height="50dp",
			layout_gravity="top",
			layout_marginTop="15dp",
			background="#ffffff",
			line,
			{
				TextView,
				id="slideSwitch_location_content",
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_gravity="left|center_vertical",
				text=title,
				layout_marginLeft="15dp",
				textColor=Color.Back,
				textSize="14sp"
			},
			{
				SlideSwitch,
				id=cell_id,
				layout_width="50dp",
				layout_height="24dp",
				isOpen=true,
				shape="circle",
				themeColor="#ff8733",
				layout_gravity="right|center_vertical",
				layout_marginRight="15dp",
				background="drawable/btn_public_back_normal.png"
			}
		}
		return cell		
end
cell_item=function(cell_id,title,drawableurl)
	cell={
		LinearLayout,id=cell_id,orientation="vertical",layout_width="fill",layout_height="wrap",background=Color.White,
		{
			LinearLayout,
			padding="12dp",
			layout_width="fill",
			layout_height="match_parent",
			orientation="horizontal",
			{
				ImageView,
				layout_marginRight="10dp",
				layout_width="30dp",
				layout_height="30dp",
				layout_gravity="center_vertical",
				background=drawableurl
			},
			{
				TextView,
				layout_width="wrap_content",
				layout_height="wrap_content",
				layout_marginRight="20dp",
				textColor="#535b5e",
				text=title,
				textSize="16sp",
				textStyle="normal",
				layout_gravity="left|center_vertical"
			}
	
		},
		line
}
	return cell
end
layout_horizontal_upload_myphoto={
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
}

spite_line={
		View,
		layout_width="100dp",
		paddingRight="5dp",
		paddingLeft="5dp",
		layout_height="1dp",
		background="#cacaca"
}