/**
 * 
 */
package com.yhx.app.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yhx.app.campus_life.R;
import com.yhx.app.common.Constants;
import com.yhx.app.common.HttpHelper;
import com.yhx.app.entity.ItemList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;


public class ShopListAdapter extends BaseAdapter{
	private List<ItemList> itemList;
	public List<ItemList> getItemList() {
		return itemList;
	}
	public void setItemList(List<ItemList> itemList) {
		this.itemList = itemList;
	}

	private Context context;
	private LayoutInflater inflater;
	public ShopListAdapter(Context context){
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return itemList == null ? 0 : itemList.size();
	}

	@Override
	public Object getItem(int position) {
		return itemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(convertView == null){


			convertView  = inflater.inflate(R.layout.shop_list_item,parent,false);
			vh = new ViewHolder();
			vh.tv_list_title = (TextView) convertView.findViewById(R.id.tv_list_title);
			vh.tv_list_school = (TextView) convertView.findViewById(R.id.tv_list_school);
			vh.tv_list_court = (TextView) convertView.findViewById(R.id.tv_list_court);
			vh.tv_list_price = (TextView) convertView.findViewById(R.id.tv_list_price);
			vh.iv_list_img = (ImageView) convertView.findViewById(R.id.iv_list_img);
			vh.tv_list_put_time = (TextView) convertView.findViewById(R.id.tv_list_put_time);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		ItemList item = itemList.get(position);
		vh.tv_list_court.setText(item.getCourt());
		vh.tv_list_title.setText("【"+item.getCategory()+"】"+item.getShopname());
		vh.tv_list_price.setText(item.getPrice());
		if (item.getPrice().equals("交换")){
		    vh.tv_list_price.setTextColor(Color.parseColor("#FF8C00"));
        }else if(item.getPrice().equals("分享")){
            vh.tv_list_price.setTextColor(Color.BLUE);
        }else if(item.getPrice().equals("捐赠")){
            vh.tv_list_price.setTextColor(Color.RED);
        }
		vh.tv_list_school.setText(item.getSchool());
		vh.tv_list_put_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getPut_time()));
        String picture = item.getPicture();
        if (picture.contains(";")){
            picture = picture.substring(0, picture.indexOf(";"));
        }
        ImageLoaderConfiguration imageLoaderConfiguration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(imageLoaderConfiguration);
        String path = Constants.URL+"/Trade/images/"+picture;
        imageLoader.displayImage(path, vh.iv_list_img);
        return convertView;
	}
	private class ViewHolder {
		ImageView iv_list_img;
		TextView tv_list_title,tv_list_school,tv_list_court,tv_list_price,tv_list_put_time;
	}
}
