package com.example.topgridlibrary.topgrid.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.database.SQLException;
import android.util.Log;

import com.example.topgridlibrary.topgrid.dao.ChannelDao;
import com.example.topgridlibrary.topgrid.db.SQLHelper;

public class ChannelManage {
	public static ChannelManage channelManage;
	/**
	 * 默认的用户选择频道列表
	 * */
	public static List<ChannelItem> defaultUserChannels;
	/**
	 * 默认的其他频道列表
	 * */
	public static List<ChannelItem> defaultOtherChannels;
	private ChannelDao channelDao;
	/** 判断数据库中是否存在用户数据 */
	private boolean userExist = false;

	public static void setChannel(List<NewsItemBean.NewsBean> items){
//		defaultUserChannels = new ArrayList<ChannelItem>();
//		defaultOtherChannels = new ArrayList<ChannelItem>();
//		for (int a=0;a<items.size();a++){
//			if(a<3){
//				defaultUserChannels.add(new ChannelItem(a+1, items.get(a).getName(), a+1, 1,0));
//			}else{
//				defaultUserChannels.add(new ChannelItem(a+1, items.get(a).getName(), a+1, 0,0));
//			}
//		}
	}

	static {
		defaultUserChannels = new ArrayList<ChannelItem>();
		defaultOtherChannels = new ArrayList<ChannelItem>();
		defaultUserChannels.add(new ChannelItem(1, "全部=2509", 1, 1,0));
		defaultUserChannels.add(new ChannelItem(2, "国内=2510", 2, 1,0));
		defaultUserChannels.add(new ChannelItem(3, "国际=2511", 3, 1,0));
		defaultUserChannels.add(new ChannelItem(4, "科技=2515", 4, 1,0));
		defaultUserChannels.add(new ChannelItem(5, "股市=2517", 5, 1,0));
		defaultUserChannels.add(new ChannelItem(6, "美股=2518", 6, 1,0));
		defaultUserChannels.add(new ChannelItem(7, "财经=2516", 7, 1,0));
		defaultOtherChannels.add(new ChannelItem(8, "社会=2669", 1, 0,0));
		defaultOtherChannels.add(new ChannelItem(9, "体育=2512", 2, 0,0));
		defaultOtherChannels.add(new ChannelItem(10, "娱乐=2513", 3, 0,0));
		defaultOtherChannels.add(new ChannelItem(11, "军事=2514", 4, 0,0));
		defaultOtherChannels.add(new ChannelItem(12, "科技=2515", 5, 0,0));
	}

	private ChannelManage(SQLHelper paramDBHelper) throws SQLException {
		if (channelDao == null)
			channelDao = new ChannelDao(paramDBHelper.getContext());
		return;
	}

	/**
	 * 初始化频道管理类
	 * @param dbHelper
	 * @throws SQLException
	 */
	public static ChannelManage getManage(SQLHelper dbHelper)throws SQLException {
		if (channelManage == null)
			channelManage = new ChannelManage(dbHelper);
		return channelManage;
	}

	/**
	 * 清除所有的频道
	 */
	public void deleteAllChannel() {
		channelDao.clearFeedTable();
	}
	/**
	 * 获取其他的频道
	 * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
	 */
	public List<ChannelItem> getUserChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?",new String[] { "1" });
		if (cacheList != null && !((List) cacheList).isEmpty()) {
			userExist = true;
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			List<ChannelItem> list = new ArrayList<ChannelItem>();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate = new ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				navigate.setNewItem(Integer.valueOf(maplist.get(i).get(SQLHelper.ISNEWITEM)));
				list.add(navigate);
			}
			return list;
		}
		initDefaultChannel();
		return defaultUserChannels;
	}
	
	/**
	 * 获取其他的频道
	 * @return 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
	 */
	public List<ChannelItem> getOtherChannel() {
		Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?" ,new String[] { "0" });
		List<ChannelItem> list = new ArrayList<ChannelItem>();
		if (cacheList != null && !((List) cacheList).isEmpty()){
			List<Map<String, String>> maplist = (List) cacheList;
			int count = maplist.size();
			for (int i = 0; i < count; i++) {
				ChannelItem navigate= new ChannelItem();
				navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
				navigate.setName(maplist.get(i).get(SQLHelper.NAME));
				navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
				navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
				navigate.setNewItem(Integer.valueOf(maplist.get(i).get(SQLHelper.ISNEWITEM)));
				list.add(navigate);
			}
			return list;
		}
		if(userExist){
			return list;
		}
		cacheList = defaultOtherChannels;
		return (List<ChannelItem>) cacheList;
	}
	
	/**
	 * 保存用户频道到数据库
	 * @param userList
	 */
	public void saveUserChannel(List<ChannelItem> userList) {
		for (int i = 0; i < userList.size(); i++) {
			ChannelItem channelItem = (ChannelItem) userList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(1));
			channelDao.addCache(channelItem);
		}
	}
	
	/**
	 * 保存其他频道到数据库
	 * @param otherList
	 */
	public void saveOtherChannel(List<ChannelItem> otherList) {
		for (int i = 0; i < otherList.size(); i++) {
			ChannelItem channelItem = (ChannelItem) otherList.get(i);
			channelItem.setOrderId(i);
			channelItem.setSelected(Integer.valueOf(0));
			channelDao.addCache(channelItem);
		}
	}
	
	/**
	 * 初始化数据库内的频道数据
	 */
	private void initDefaultChannel(){
		Log.d("deleteAll", "deleteAll");
		deleteAllChannel();
		saveUserChannel(defaultUserChannels);
		saveOtherChannel(defaultOtherChannels);
	}
}
