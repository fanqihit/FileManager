package com.example.filemanager;

/* import相关class */
import java.io.File;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//自定义的Adapter，继承android.widget.BaseAdapter
public class MyAdapter extends BaseAdapter
{
  /* 变量声明 
     mIcon1：返回根目录的图片
     mIcon2：返回上一目录的图片
     mIcon3：文件夹的图片
     mIcon4：文件的图片
  */
  private LayoutInflater mInflater;
  private Bitmap mIcon1;//位图文件,我的图标
  private Bitmap mIcon2;
  private Bitmap mIcon3;
  private Bitmap mIcon4;
  private List<String> items;
  private List<String> paths;
  //MyAdapter的构造符，传入三个参数
  public MyAdapter(Context context,List<String> it,List<String> pa)
  {
    //参数初始化 
    mInflater = LayoutInflater.from(context);//数据的传递 好像是
    items = it;
    paths = pa;
	//以下是加载图片资源，一参是加载的位图资源文件的对象，二是位图资源的id
    mIcon1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.back01);
    mIcon2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.back02);
    mIcon3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.folder);
    mIcon4 = BitmapFactory.decodeResource(context.getResources(),R.drawable.doc);
  }
  
  //继承BaseAdapter，需重写method 
  @Override
  public int getCount()
  {
    return items.size();
  }

  @Override
  public Object getItem(int position)
  {
    return items.get(position);
  }
  
  @Override
  public long getItemId(int position)
  {
    return position;
  }
  
  @Override
  public View getView(int position,View convertView,ViewGroup parent)
  {
    ViewHolder holder;//自己定义的一个类，在最下面
    
    if(convertView == null)
    {
      //使用告定义的file_row作为Layout 
      convertView = mInflater.inflate(R.layout.file_row, null);
      /* 初始化holder的text与icon */
      holder = new ViewHolder();//new 一个class
      holder.text = (TextView) convertView.findViewById(R.id.text);
      holder.icon = (ImageView) convertView.findViewById(R.id.icon);
      
      convertView.setTag(holder);//给view对象设置一个标签
    }
    else
    {
      holder = (ViewHolder) convertView.getTag();
    }

    File f=new File(paths.get(position).toString());//按位置得到文件的指针
    //设定[并到根目录]的文字与icon 
    if(items.get(position).toString().equals("b1"))
    {
      holder.text.setText("返回根目录");
      holder.icon.setImageBitmap(mIcon1);
    }
    //设定[并到第几层]的文字与icon
    else if(items.get(position).toString().equals("b2"))
    {
      holder.text.setText("返回");
      holder.icon.setImageBitmap(mIcon2);
    }
    //设定[文件或文件夹]的文字与icon
    else
    {
      holder.text.setText(f.getName());
      if(f.isDirectory())//是文件夹的时候
      {
        holder.icon.setImageBitmap(mIcon3);
      }
      else
      {
        holder.icon.setImageBitmap(mIcon4);
      }
    }
    return convertView;
  }
  /* class ViewHolder */
  private class ViewHolder
  {
    TextView text;
    ImageView icon;
  }
}


