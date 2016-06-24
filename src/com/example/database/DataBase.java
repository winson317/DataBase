package com.example.database;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class DataBase extends Activity {
	
	SQLiteDatabase db;
	Button button = null;
	ListView  listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         //打开火创建数据库，（此处需要使用绝对路径）
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/my.db3", null);
        listView = (ListView)findViewById(R.id.show);
        button = (Button)findViewById(R.id.ok);
        
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View source) {
				// TODO Auto-generated method stub
				//获取用户输入
				String title = ((EditText)findViewById(R.id.title)).getText().toString();
				String content = ((EditText)findViewById(R.id.content)).getText().toString();
				
				try 
				{
					insertData(db, title, content);
					Cursor cursor = db.rawQuery("select * from news_inf", null);
					inflateList(cursor);
				} 
				catch (SQLiteException e)
				{
					//执行DDL创建数据表
					db.execSQL("create table news_inf(_id integer" + "primary key autoincrement," 
				     + "news_title varchar(50)" + "news_content varchar(255))");
					insertData(db, title, content); //执行insert语句插入数据
					Cursor cursor = db.rawQuery("select * from news_inf", null); //执行查询
					inflateList(cursor);
				}
			}
		});
    }
    
    private void insertData(SQLiteDatabase db, String title, String content) {
		// TODO Auto-generated method stub
    	db.execSQL("insert into news_inf values(null, ?, ?)", new String[] {title, content}); //执行插入语句
	}
    
    private void inflateList(Cursor cursor) {
		// TODO Auto-generated method stub
    	//填充SimpleCursorAdapter
    	SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.line, cursor, 
    			new String[] {"news_title", "news_content"}, new int[] {R.id.my_title, R.id.my_content},
    			CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    	listView.setAdapter(adapter);  //显示数据
	}
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	//退出程序时关闭SQLiteDatabase
    	if (db != null && db.isOpen())
    	{
    		db.close();
    	}
    }

}
