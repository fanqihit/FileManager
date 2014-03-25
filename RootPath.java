package com.example.filemanager;

/* import相关class */
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.content.DialogInterface.OnClickListener;

public class RootPath extends ListActivity  //ListActivity类似于数组，可以存东西
{
  /* 对象声明 
     items：存放显示的名称
     paths：存放文件路径
     rootPath：起始目录
  */
  private List<String> items=null;//List<String> 泛型，存储一系列字符串(类似于字符串数组)
  private List<String> paths=null;
  private String rootPath="/";
  private TextView mPath;//在Activity上设置显示文字
  private View myView;// 定义一个布局，自定义样式
  private EditText myEditText;//在Activity上接受用户从键盘输入的内容
  private Button button2;
  
  /** Called when the activity is first created. */
  @Override
  protected void onCreate(Bundle icicle)//一个窗口生成
  {
    super.onCreate(icicle);
    setContentView(R.layout.activity_main);//以Layout 中 activity_main.xml 来布局
    button2 = (Button)findViewById(R.id.button_re_rootpath);//获取按钮"返回首页"
    button2.setTextSize(10);
    button2.setOnClickListener(        //对按钮设置监听事件
	new Button.OnClickListener()
    {
    public void onClick(View v)	
		{
			Intent intent2 = new Intent();
			intent2.setClass(RootPath.this,MainActivity.class);//返回MainActivity
			startActivity(intent2);
			RootPath.this.finish();
		}	
    }	
    );
    mPath=(TextView)findViewById(R.id.mpath); //初始化mPath，用以显示目前路径，实际为空
    getFileDir(rootPath);//调用getFileDir()方法来获取根目录下的文件信息
  }
  
  private void getFileDir(String filePath)//取得文件架构，开始时filePath为"/"
  {
    /* 设定目前所在路径 */
    mPath.setText(filePath);
    
    items=new ArrayList<String>();
    paths=new ArrayList<String>();  
    File f=new File(filePath);//以filePath "/"为路径 创建一个File实例  
    File[] files=f.listFiles();//返回f对象路径的文件列表 并存入files

    if(!filePath.equals(rootPath))
    {
      //设定回到根目录
      items.add("b1");
      paths.add(rootPath);
      //设定回到上一目录
      items.add("b2");
      paths.add(f.getParent());
    }
    
    for(int i=0;i<files.length;i++)//将所有文件加入ArrayList中 
    {
      File file=files[i];
      items.add(file.getName());
      paths.add(file.getPath());
    }
    
    /*使用自定义的MyAdapter来将数据传入ListActivity
	这个类extents ListActivity,setListAdapter是ListActivity中的一个函数
	要显示items、paths 中的东西到界面，必须借助Adapter,大概就是把数据映射到界面上*/
    setListAdapter(new MyAdapter(this,items,paths));
  }
  
  /* 设定  ListItem列表选项  被按下时要做的动作 */
  @Override
  protected void onListItemClick(ListView l,View v,int position,long id)//ListActivity 中的一个函数
  {
    File file = new File(paths.get(position));
	/*position 参数表示list中view 的位置，paths.get()根据position找到文件路径，
	  再根据路径创建一个File实例*/
    if(file.canRead())//如果文件文件可读
    {
      if (file.isDirectory())  //如果是文件夹就再进去读取
      {
        getFileDir(paths.get(position));
      }
      else // 如果是文件,调用fileHandle()函数处理
      {
        fileHandle(file);
      }
    }
    else //如果文件不可读,弹出AlertDialog对话框显示权限不足
    {
      AlertDialog.Builder Builder1 = new AlertDialog.Builder(this);
	      //暂时没有ps图标等待后期制作Builder1.setIcon(R.drawable.xxx);
          Builder1.setTitle("信息提示");
          Builder1.setMessage("权限不足!");
          Builder1.setPositiveButton("确定",
            new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface dialog,int which)
              {
			  //此处可以添加其他要执行的程序
              }
            });
		  Builder1.show();//显示对话框     
    }
  } 
  private void fileHandle(final File file)
  {
  //按下文件时的OnClickListener
  OnClickListener listener1 = new DialogInterface.OnClickListener()//对按下的对话框设置一个监听事件
   {
    /*以下是按下对话框中按钮时的执行方法，
	dialog收到单击事件的对话框，which 按下的按钮或者按下条目的位置*/
	public void onClick(DialogInterface dialog,int which)
    {
      if(which == 0)//选择的item为打开文件
      {
        openFile(file);//调用openFile()函数打开文件
      }
      else if(which == 1)//选择的item为更改文件名
      {
        LayoutInflater factory = LayoutInflater.from(RootPath.this);//重新加载一个布局
        //使用rename_alert_dialog为layout
        myView=factory.inflate(R.layout.rename_alert_dialog,null);//LayoutInflater是找res/layout下的xml布局的
        myEditText=(EditText)myView.findViewById(R.id.mEdit);//findViewById是用来找具体控件的
        /* 将原始文件名因放入EditText中 */
        myEditText.setText(file.getName());//编辑框中初始值是原始文件名，一般软件基本都是这样
          
        //新建一个更改文件名的Dialog的确定按钮的listener
        OnClickListener listener2=new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog,int which)
          {
            //取得修改后的文件路径
            String modName=myEditText.getText().toString();//取得编辑框中的内容，以string形式存储
            final String pFile=file.getParentFile().getPath()+"/";//取得文件的上层目录 + "/"
            final String newPath=pFile+modName;//pFile 再加上 modName (新的文件名)，得到新的文件路径
            
            //判断文件名是否存在，存在包括两种情况（1.并未修改 2.修改的名字已经存在） 
            if(new File(newPath).exists())//判断文件指针是否存在，如果存在
            {
              //如果并未修改，这种情况不做处理
              if(!modName.equals(file.getName()))//如果修改的文件名存在
              {
                //弹出Alert警告文件名重复，并确认是否修改
                AlertDialog.Builder Builder3 = new AlertDialog.Builder(RootPath.this);
                    Builder3.setTitle("注意!");
                    Builder3.setMessage("文件名已经存在，是否要覆盖?");
                    Builder3.setPositiveButton("确定",new DialogInterface.OnClickListener()
                    {
                      public void onClick(DialogInterface dialog,int which)
                      {          
                        //文件名重复仍然修改会覆盖掉已存的文件
                        file.renameTo(new File(newPath));
                        //重新生成文件在表的ListView,即重新载入文件列表
                        getFileDir(pFile);
                      }
                    });
                    Builder3.setNegativeButton("取消",new DialogInterface.OnClickListener()
                    {
                      public void onClick(DialogInterface dialog,int which)
                      {
                      }
                    });
					Builder3.show();
              }
            }
            else //文件名不存在，直接做修改操作 
            {
              file.renameTo(new File(newPath));//直接修改文件名，两个参数分别为目标文件和目标路径
              getFileDir(file.getParent());//重新生成文件列表的ListView,即重新载入文件列表
            }
          }
        };

        //create更改文件名时弹出的Dialog
        AlertDialog.Builder renameDialog=new AlertDialog.Builder(RootPath.this);
        renameDialog.setView(myView);//给对话框设置自定义的样式
        
        //设定 更改文件名按下 确认 后的Listener 
        renameDialog.setPositiveButton("确定",listener2);
        renameDialog.setNegativeButton("取消",new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int which)
          {
          }
        });
        renameDialog.show();
      }
      else//选择的item为删除文件
      {
        
        AlertDialog.Builder Builder4 = new AlertDialog.Builder(RootPath.this);
		Builder4.setTitle("注意!");
        Builder4.setMessage("确定要删除文件吗?");
        Builder4.setPositiveButton("确定", new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface dialog, int which)
              {          
                //删除文件 
                file.delete();
                getFileDir(file.getParent());
              }
            });
        Builder4.setNegativeButton("取消", new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface dialog, int which)
              {
              }
            });
		Builder4.show();
      }
    }
  };//设置监听事件结束
      
	//单机文件时显示对话框
	String[] menu={"打开文件","更改文件名","删除文件"};
	AlertDialog.Builder Builder2 = new AlertDialog.Builder(RootPath.this);
	Builder2.setTitle("文件菜单");
	Builder2.setItems(menu,listener1);
	Builder2.setPositiveButton("取消", new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
       {
       }
	});
     Builder2.show();
 }

/* 手机打开文件的method */
  private void openFile(File f) 
 {
  Intent intent = new Intent();
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//建立一个新的TASK，完成单独的事情
  intent.setAction(android.content.Intent.ACTION_VIEW);
  
  //调用getType()来取得文件Type
  String type = getType(f);//f为指向该文件的指针
  intent.setDataAndType(Uri.fromFile(f),type);//按type 类型打开，
  startActivity(intent); 
  }

  private String getType(File f) 
 { 
  String type="";
  String fName=f.getName();
  //截取扩展名
  String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();
	//从.后面开始截取，想起DBMS了。。。。
  if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
     end.equals("xmf")||end.equals("ogg")||end.equals("wav"))
  {
    type = "audio"; //音频文件
  }
  else if(end.equals("3gp")||end.equals("mp4")||end.equals("rmvb"))
  {
    type = "video";//视频文件
  }
  else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
          end.equals("jpeg")||end.equals("bmp"))
  {
    type = "image";//图片文件
  }
  else
  {
    type="*";//如果无法识别类型，将会弹出软件列表给用户选择 
  }
  
  type += "/*"; 
  return type; 
 }
}
