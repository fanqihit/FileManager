package com.example.filemanager;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import com.example.filemanager.RootPath;
public class MainActivity extends Activity {

	private Button button1;
	private TextView textview ; 
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybutton);
        button1 = (Button)findViewById(R.id.button_rootpath);//获取按钮
        textview = (TextView)findViewById(R.id.text2);
        button1.setOnClickListener(
        new Button.OnClickListener()
        {
         public void onClick(View v)
         { 
        	 //new 一个Intent对象，并制定要启动的class
        	 Intent intent1 = new Intent();
        	 intent1.setClass(MainActivity.this,RootPath.class);
        	 
        	 //调用一个新的acvitity
        	 startActivity(intent1);
        	 MainActivity.this.finish();
         }
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
