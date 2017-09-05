package com.elvis.pdfreader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.TimerTask;

public class MainActivity extends Activity {

	Button openPdfBtn ;
	Button downLoadBtn;
	int result = 8;
	Dialog dialog;
	FileUtils futils;
	String fileUrl;
	String fileName;
	String subDir;
	String sdPath;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		futils= new FileUtils();
		openPdfBtn = (Button) findViewById(R.id.button1);

		subDir = "/test/";
		fileUrl = "http://59.48.96.118:7002/contract/save/20170329_1490770944027.pdf";

		fileName= getFileNameFromUrl(fileUrl);

		sdPath = futils.getSDPATH() + subDir + fileName;

		openPdfBtn.setOnClickListener(new View.OnClickListener()
		{

			@SuppressLint("SdCardPath")
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

				if(futils.isFileExist(subDir + fileName))
				{
					Intent intent = getPdfFileIntent(sdPath);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(MainActivity.this, "File not exists!,please DownLoad fist!", Toast.LENGTH_SHORT).show();
				}

			}
		});


		downLoadBtn = (Button) this.findViewById(R.id.button2);
		downLoadBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				if(futils.isFileExist(subDir + fileName))
				{
					Intent intent = getPdfFileIntent(sdPath);
					startActivity(intent);
				}
				else
				{

					showLoad("  正在打开...");
					// TODO Auto-generated method stub
					Thread t = new Thread(new Runnable()
					{

						@Override
						public void run()
						{
							// TODO Auto-generated method stub
							HttpDownloader httpDownLoader = new HttpDownloader();

							result = httpDownLoader.downfile(fileUrl, subDir, fileName);

							tt.run();
						}

					});
					t.start();

				}


			}
		});







	}

	@SuppressWarnings("null")
	public String getFileNameFromUrl(String fileUrl)
	{
		String fileName="";
		int index;
		if(fileUrl!=null || fileUrl.trim()!="")
		{
			index = fileUrl.lastIndexOf("/");
			fileName = fileUrl.substring(index+1, fileUrl.length());
		}
		return fileName;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			disMisLoad();
			MessageShow();
		}
	};

	public void MessageShow()
	{




		if(result==0)
		{
			Toast.makeText(MainActivity.this, "下载成功！", Toast.LENGTH_SHORT).show();
		}
		else if(result==1) {
			Toast.makeText(MainActivity.this, "已有文件！", Toast.LENGTH_SHORT).show();
		}
		else if(result==-1){
			Toast.makeText(MainActivity.this, "下载失败！", Toast.LENGTH_SHORT).show();
		}


		Intent intent = getPdfFileIntent(sdPath);
		startActivity(intent);

	}
	TimerTask tt = new TimerTask()
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			msg.what =1;

			handler.sendMessageDelayed(msg, 2000);
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public Intent getPdfFileIntent(String path)
	{
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
		Uri uri = Uri.fromFile(new File(path));
		i.setDataAndType(uri, "application/pdf");
		return i;
	}

	public void showLoad(String title)
	{
		dialog = new Dialog(this,R.style.MyDialog);
		//		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.load);

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.load, null);
		TextView showLoadTitle = (TextView) v.findViewById(R.id.showLoadTitle);
		showLoadTitle.setText(title);
		dialog.setContentView(v);
		dialog.show();
	}
	/**
	 * 关闭弹窗
	 */
	public void disMisLoad(){
		if(dialog!=null){
			dialog.dismiss();
		}
	}

}
