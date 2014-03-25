package com.example.filemanager;

/* import���class */
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

public class RootPath extends ListActivity  //ListActivity���������飬���Դ涫��
{
  /* �������� 
     items�������ʾ������
     paths������ļ�·��
     rootPath����ʼĿ¼
  */
  private List<String> items=null;//List<String> ���ͣ��洢һϵ���ַ���(�������ַ�������)
  private List<String> paths=null;
  private String rootPath="/";
  private TextView mPath;//��Activity��������ʾ����
  private View myView;// ����һ�����֣��Զ�����ʽ
  private EditText myEditText;//��Activity�Ͻ����û��Ӽ������������
  private Button button2;
  
  /** Called when the activity is first created. */
  @Override
  protected void onCreate(Bundle icicle)//һ����������
  {
    super.onCreate(icicle);
    setContentView(R.layout.activity_main);//��Layout �� activity_main.xml ������
    button2 = (Button)findViewById(R.id.button_re_rootpath);//��ȡ��ť"������ҳ"
    button2.setTextSize(10);
    button2.setOnClickListener(        //�԰�ť���ü����¼�
	new Button.OnClickListener()
    {
    public void onClick(View v)	
		{
			Intent intent2 = new Intent();
			intent2.setClass(RootPath.this,MainActivity.class);//����MainActivity
			startActivity(intent2);
			RootPath.this.finish();
		}	
    }	
    );
    mPath=(TextView)findViewById(R.id.mpath); //��ʼ��mPath��������ʾĿǰ·����ʵ��Ϊ��
    getFileDir(rootPath);//����getFileDir()��������ȡ��Ŀ¼�µ��ļ���Ϣ
  }
  
  private void getFileDir(String filePath)//ȡ���ļ��ܹ�����ʼʱfilePathΪ"/"
  {
    /* �趨Ŀǰ����·�� */
    mPath.setText(filePath);
    
    items=new ArrayList<String>();
    paths=new ArrayList<String>();  
    File f=new File(filePath);//��filePath "/"Ϊ·�� ����һ��Fileʵ��  
    File[] files=f.listFiles();//����f����·�����ļ��б� ������files

    if(!filePath.equals(rootPath))
    {
      //�趨�ص���Ŀ¼
      items.add("b1");
      paths.add(rootPath);
      //�趨�ص���һĿ¼
      items.add("b2");
      paths.add(f.getParent());
    }
    
    for(int i=0;i<files.length;i++)//�������ļ�����ArrayList�� 
    {
      File file=files[i];
      items.add(file.getName());
      paths.add(file.getPath());
    }
    
    /*ʹ���Զ����MyAdapter�������ݴ���ListActivity
	�����extents ListActivity,setListAdapter��ListActivity�е�һ������
	Ҫ��ʾitems��paths �еĶ��������棬�������Adapter,��ž��ǰ�����ӳ�䵽������*/
    setListAdapter(new MyAdapter(this,items,paths));
  }
  
  /* �趨  ListItem�б�ѡ��  ������ʱҪ���Ķ��� */
  @Override
  protected void onListItemClick(ListView l,View v,int position,long id)//ListActivity �е�һ������
  {
    File file = new File(paths.get(position));
	/*position ������ʾlist��view ��λ�ã�paths.get()����position�ҵ��ļ�·����
	  �ٸ���·������һ��Fileʵ��*/
    if(file.canRead())//����ļ��ļ��ɶ�
    {
      if (file.isDirectory())  //������ļ��о��ٽ�ȥ��ȡ
      {
        getFileDir(paths.get(position));
      }
      else // ������ļ�,����fileHandle()��������
      {
        fileHandle(file);
      }
    }
    else //����ļ����ɶ�,����AlertDialog�Ի�����ʾȨ�޲���
    {
      AlertDialog.Builder Builder1 = new AlertDialog.Builder(this);
	      //��ʱû��psͼ��ȴ���������Builder1.setIcon(R.drawable.xxx);
          Builder1.setTitle("��Ϣ��ʾ");
          Builder1.setMessage("Ȩ�޲���!");
          Builder1.setPositiveButton("ȷ��",
            new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface dialog,int which)
              {
			  //�˴������������Ҫִ�еĳ���
              }
            });
		  Builder1.show();//��ʾ�Ի���     
    }
  } 
  private void fileHandle(final File file)
  {
  //�����ļ�ʱ��OnClickListener
  OnClickListener listener1 = new DialogInterface.OnClickListener()//�԰��µĶԻ�������һ�������¼�
   {
    /*�����ǰ��¶Ի����а�ťʱ��ִ�з�����
	dialog�յ������¼��ĶԻ���which ���µİ�ť���߰�����Ŀ��λ��*/
	public void onClick(DialogInterface dialog,int which)
    {
      if(which == 0)//ѡ���itemΪ���ļ�
      {
        openFile(file);//����openFile()�������ļ�
      }
      else if(which == 1)//ѡ���itemΪ�����ļ���
      {
        LayoutInflater factory = LayoutInflater.from(RootPath.this);//���¼���һ������
        //ʹ��rename_alert_dialogΪlayout
        myView=factory.inflate(R.layout.rename_alert_dialog,null);//LayoutInflater����res/layout�µ�xml���ֵ�
        myEditText=(EditText)myView.findViewById(R.id.mEdit);//findViewById�������Ҿ���ؼ���
        /* ��ԭʼ�ļ��������EditText�� */
        myEditText.setText(file.getName());//�༭���г�ʼֵ��ԭʼ�ļ�����һ�����������������
          
        //�½�һ�������ļ�����Dialog��ȷ����ť��listener
        OnClickListener listener2=new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog,int which)
          {
            //ȡ���޸ĺ���ļ�·��
            String modName=myEditText.getText().toString();//ȡ�ñ༭���е����ݣ���string��ʽ�洢
            final String pFile=file.getParentFile().getPath()+"/";//ȡ���ļ����ϲ�Ŀ¼ + "/"
            final String newPath=pFile+modName;//pFile �ټ��� modName (�µ��ļ���)���õ��µ��ļ�·��
            
            //�ж��ļ����Ƿ���ڣ����ڰ������������1.��δ�޸� 2.�޸ĵ������Ѿ����ڣ� 
            if(new File(newPath).exists())//�ж��ļ�ָ���Ƿ���ڣ��������
            {
              //�����δ�޸ģ����������������
              if(!modName.equals(file.getName()))//����޸ĵ��ļ�������
              {
                //����Alert�����ļ����ظ�����ȷ���Ƿ��޸�
                AlertDialog.Builder Builder3 = new AlertDialog.Builder(RootPath.this);
                    Builder3.setTitle("ע��!");
                    Builder3.setMessage("�ļ����Ѿ����ڣ��Ƿ�Ҫ����?");
                    Builder3.setPositiveButton("ȷ��",new DialogInterface.OnClickListener()
                    {
                      public void onClick(DialogInterface dialog,int which)
                      {          
                        //�ļ����ظ���Ȼ�޸ĻḲ�ǵ��Ѵ���ļ�
                        file.renameTo(new File(newPath));
                        //���������ļ��ڱ��ListView,�����������ļ��б�
                        getFileDir(pFile);
                      }
                    });
                    Builder3.setNegativeButton("ȡ��",new DialogInterface.OnClickListener()
                    {
                      public void onClick(DialogInterface dialog,int which)
                      {
                      }
                    });
					Builder3.show();
              }
            }
            else //�ļ��������ڣ�ֱ�����޸Ĳ��� 
            {
              file.renameTo(new File(newPath));//ֱ���޸��ļ��������������ֱ�ΪĿ���ļ���Ŀ��·��
              getFileDir(file.getParent());//���������ļ��б��ListView,�����������ļ��б�
            }
          }
        };

        //create�����ļ���ʱ������Dialog
        AlertDialog.Builder renameDialog=new AlertDialog.Builder(RootPath.this);
        renameDialog.setView(myView);//���Ի��������Զ������ʽ
        
        //�趨 �����ļ������� ȷ�� ���Listener 
        renameDialog.setPositiveButton("ȷ��",listener2);
        renameDialog.setNegativeButton("ȡ��",new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int which)
          {
          }
        });
        renameDialog.show();
      }
      else//ѡ���itemΪɾ���ļ�
      {
        
        AlertDialog.Builder Builder4 = new AlertDialog.Builder(RootPath.this);
		Builder4.setTitle("ע��!");
        Builder4.setMessage("ȷ��Ҫɾ���ļ���?");
        Builder4.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface dialog, int which)
              {          
                //ɾ���ļ� 
                file.delete();
                getFileDir(file.getParent());
              }
            });
        Builder4.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface dialog, int which)
              {
              }
            });
		Builder4.show();
      }
    }
  };//���ü����¼�����
      
	//�����ļ�ʱ��ʾ�Ի���
	String[] menu={"���ļ�","�����ļ���","ɾ���ļ�"};
	AlertDialog.Builder Builder2 = new AlertDialog.Builder(RootPath.this);
	Builder2.setTitle("�ļ��˵�");
	Builder2.setItems(menu,listener1);
	Builder2.setPositiveButton("ȡ��", new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
       {
       }
	});
     Builder2.show();
 }

/* �ֻ����ļ���method */
  private void openFile(File f) 
 {
  Intent intent = new Intent();
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//����һ���µ�TASK����ɵ���������
  intent.setAction(android.content.Intent.ACTION_VIEW);
  
  //����getType()��ȡ���ļ�Type
  String type = getType(f);//fΪָ����ļ���ָ��
  intent.setDataAndType(Uri.fromFile(f),type);//��type ���ʹ򿪣�
  startActivity(intent); 
  }

  private String getType(File f) 
 { 
  String type="";
  String fName=f.getName();
  //��ȡ��չ��
  String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();
	//��.���濪ʼ��ȡ������DBMS�ˡ�������
  if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
     end.equals("xmf")||end.equals("ogg")||end.equals("wav"))
  {
    type = "audio"; //��Ƶ�ļ�
  }
  else if(end.equals("3gp")||end.equals("mp4")||end.equals("rmvb"))
  {
    type = "video";//��Ƶ�ļ�
  }
  else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
          end.equals("jpeg")||end.equals("bmp"))
  {
    type = "image";//ͼƬ�ļ�
  }
  else
  {
    type="*";//����޷�ʶ�����ͣ����ᵯ������б���û�ѡ�� 
  }
  
  type += "/*"; 
  return type; 
 }
}
