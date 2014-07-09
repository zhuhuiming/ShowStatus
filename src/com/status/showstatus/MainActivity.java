package com.status.showstatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.SaveListener;

import com.status.bombobject.BmobObject1;
import com.status.bombobject.StatusObject;
import com.status.showstatus.AboutDialog.PriorityListener;
import com.status.utils.CommonUtils;
import com.status.utils.ScreenShot;
import com.status.versionupdate.FileUtil;
import com.status.versionupdate.UpdateInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// 查找图片控件
	TextView searchitextview;
	// 当前用户名控件
	TextView curpersonnametextview;
	// 编辑状态控件
	TextView edittextview;
	// 当前时间控件
	TextView timetextview;
	// 当前用户状态控件
	TextView curstatustextview;
	// 提示信息控件
	TextView prompttextview;
	// 相同状态人数控件
	TextView samestatustextview;
	// 正在查询某状态提示信息控件
	TextView researchprompttextview;
	// 正在查询某一状态人数提示信息控件
	TextView sameresearchstatustextview;
	// 分享控件
	ImageView shareimageview;
	// 更多操作控件
	// ImageView moreoperaimageview;
	// 更多操作linearlayout
	LinearLayout moreoperalinearlayout;
	// 用来获取服务器上数据的对象
	// private Service goodService = new ServiceImpl();

	MyHandler myhandler = null;
	// 用来存储用户信息
	SharedPreferences msettings = null;
	// 当前状态人数显示控件
	LinearLayout state_linearlayout;
	// 当前查询人数显示控件
	LinearLayout search_linearlayout;

	public static int localVersion = 1;// 本地安装版本
	/* 下载更新有关的变量 (start) */
	private NotificationManager notificationManager;
	private Notification notification;
	private Intent updateIntent;
	private PendingIntent pendingIntent;
	private int notification_id = 1;
	RemoteViews contentView;
	private static final int DOWN_OK = 1;
	private static final int DOWN_ERROR = 0;
	private static final String down_url = "http://www.meiliangshare.cn:8000/ShowStatusApk/download?fileName=ShowStatus";
	private static final int TIMEOUT = 10 * 1000;// 超时59.60.9.202:8000
	// 判断是否已经提示更新版本号
	private boolean bIsReminderUpdateApk = false;
	public UpdateInfo mupdateinfo = null;
	private static final String strUpdateXmlPath = "http://www.meiliangshare.cn:8000/ShowStatusApk/download?fileName=update.xml";
	/* (end) */
	// 状态持续时间
	private static final long lTimeSeconds = 8 * 3600;
	// 定时更新任务数据的线程对象
	private static Thread mthre = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		msettings = getSharedPreferences("statusdata", 0);
		myhandler = new MyHandler();
		bIsReminderUpdateApk = false;
		// 如果此时线程对象还没有创建,那么就创建(用来检测新版本)
		if (null == mthre) {
			mthre = new Thread(checkrun);
			mthre.start();
		}
		// 初始化 Bmob SDK
		Bmob.initialize(this, "d469396104254d9cd69ea9f511eb4401");

		InitActivity();
		// 到服务器中下载update.xml文件,获取里面的信息
		checkVersion();
		// 获取用户的登录信息
		DealAccountInfo();

		// // 获取版本号
		PackageInfo packageInfo = null;
		try {
			packageInfo = getApplicationContext().getPackageManager()
					.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageInfo != null) {
			localVersion = packageInfo.versionCode;
		}
	}

	// 处理正在做某事的人数,nType为1表示只查询,2表示查询和插入
	private void DealStatusCount(final String strContent, final int nType) {

		Bmob.getServerTime(MainActivity.this, new GetServerTimeListener() {
			String strServiceTime = "";

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onSuccess(long time) {
				// 获取系统当前时间
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				strServiceTime = formatter.format(new Date(time * 1000L));
				long diff = time * 1000L - lTimeSeconds * 1000;// 这样得到的差值是毫秒级别
				// 根据diff换算成yyyy年MM月dd日HH:mm:ss格式
				String strStartTime = SecondsToDate(diff);
				BmobQuery<StatusObject> query = new BmobQuery<StatusObject>();
				query.addWhereEqualTo("StatusContent", strContent);
				query.addWhereGreaterThanOrEqualTo("AnncounceTime",
						strStartTime);
				query.addWhereLessThanOrEqualTo("AnncounceTime",
						strServiceTime);

				query.count(MainActivity.this, StatusObject.class,
						new CountListener() {
							@Override
							public void onFailure(int code, String msg) {
								CommonUtils.ShowToastCenter(
										getApplicationContext(), msg,
										Toast.LENGTH_LONG);
							}

							@Override
							public void onSuccess(int count) {
								if (2 == nType) {
									// 将这条查询记录插入到表中
									StatusObject gameScore = new StatusObject();
									gameScore
											.setStatusContent(strContent);
									gameScore.setAnncounceTime(strServiceTime);
									BmobGeoPoint geopoint = new BmobGeoPoint(0,0);
									gameScore.setgeopoint(geopoint);
									gameScore.save(MainActivity.this,
											new SaveListener() {

												@Override
												public void onSuccess() {
												}

												@Override
												public void onFailure(int code,
														String arg0) {
												}
											});
								}
								Message msg = myhandler.obtainMessage();
								Bundle b = new Bundle();// 存放数据
								b.putInt("nPersonNum", count);
								b.putString("strContent", strContent);
								msg.setData(b);
								msg.what = 3;
								myhandler.sendMessage(msg);
							}

						});
			}

			@Override
			public void onFailure(int code, String msg) {
			}
		});
	}

	// 处理正在查询某事的人数,nType为1表示只查询,2表示查询和插入
	private void DealResearchingCount(final String strContent, final int nType) {
		Bmob.getServerTime(MainActivity.this, new GetServerTimeListener() {
			String strServiceTime = "";

			@Override
			public void onSuccess(long time) {
				// 获取系统当前时间
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				strServiceTime = formatter.format(new Date(time * 1000L));
				long diff = time * 1000L - lTimeSeconds * 1000;// 这样得到的差值是毫秒级别
				// 根据diff换算成yyyy年MM月dd日HH:mm:ss格式
				String strStartTime = SecondsToDate(diff);
				BmobQuery<BmobObject1> query = new BmobQuery<BmobObject1>();
				query.addWhereEqualTo("StatusContent", strContent);
				query.addWhereGreaterThanOrEqualTo("AnncounceTime",
						strStartTime);
				query.addWhereLessThanOrEqualTo("AnncounceTime",
						strServiceTime);

				query.count(MainActivity.this, BmobObject1.class,
						new CountListener() {

							@Override
							public void onFailure(int code, String msg) {
								CommonUtils.ShowToastCenter(
										getApplicationContext(), msg,
										Toast.LENGTH_LONG);
							}

							@Override
							public void onSuccess(int count) {
								if (2 == nType) {
									// 将这条查询记录插入到表中
									BmobObject1 gameScore = new BmobObject1();
									gameScore
											.setStatusContent(strContent);
									gameScore.setAnncounceTime(strServiceTime);
									BmobGeoPoint geopoint = new BmobGeoPoint(0,0);
									gameScore.setgeopoint(geopoint);
									gameScore.save(MainActivity.this,
											new SaveListener() {

												@Override
												public void onSuccess() {
												}

												@Override
												public void onFailure(int code,
														String arg0) {
												}
											});
								}
								Message msg = myhandler.obtainMessage();
								Bundle b = new Bundle();// 存放数据
								b.putString("strContent", strContent);
								b.putInt("lPlayPersonNum", count);
								msg.setData(b);
								msg.what = 4;
								myhandler.sendMessage(msg);
							}

						});
			}

			@Override
			public void onFailure(int code, String msg) {
			}
		});
	}

	// 获取系统时间
	private void ProduceServiceTime() {
		Bmob.getServerTime(MainActivity.this, new GetServerTimeListener() {

			@Override
			public void onFailure(int arg0, String arg1) {

			}

			@Override
			public void onSuccess(long time) {
				// 获取系统当前时间
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String strServiceTime = formatter
						.format(new Date(time * 1000L));
				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// 存放数据
				b.putString("servicetime", strServiceTime);
				msg.setData(b);
				msg.what = 5;
				myhandler.sendMessage(msg);
			}

		});
	}

	// 获取用户名称
	private void ProducePersonName() {

		BmobUser bmobUser = BmobUser.getCurrentUser(this);
		if (bmobUser != null) {
			Message msg = myhandler.obtainMessage();
			Bundle b = new Bundle();// 存放数据
			b.putString("personname", bmobUser.getUsername());
			msg.setData(b);
			msg.what = 0;
			myhandler.sendMessage(msg);
		} else {
			// 获取手机mac
			WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

			WifiInfo info = wifi.getConnectionInfo();

			String strMac = info.getMacAddress();
			if (null == strMac) {
				strMac = "";
			}
			// 对密码Mac进行加密
			String strPassWord = CommonUtils.MD5(strMac);
			BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
			query.addWhereEqualTo("password", strPassWord);
			query.findObjects(MainActivity.this, new FindListener<BmobUser>() {
				@Override
				public void onSuccess(List<BmobUser> object) {
					if (object.size() > 0) {
						Message msg = myhandler.obtainMessage();
						Bundle b = new Bundle();// 存放数据
						b.putString("personname", object.get(0).getUsername());
						msg.setData(b);
						msg.what = 0;
						myhandler.sendMessage(msg);
					}else{
						Message msg = myhandler.obtainMessage();
						Bundle b = new Bundle();// 存放数据
						b.putString("personname", "");
						msg.setData(b);
						msg.what = 0;
						myhandler.sendMessage(msg);
					}
				}

				@Override
				public void onError(int code, String Errormsg) {
					
				}
			});
		}
	}

	private void DealAccountInfo() {
		DealStatusCount("玩三点", 1);
		DealResearchingCount("玩三点", 1);
		// 获取系统时间
		ProduceServiceTime();
		// 获取用户名
		ProducePersonName();
	}

	private void InitActivity() {
		searchitextview = (TextView) findViewById(R.id.main_searchtextview);
		curpersonnametextview = (TextView) findViewById(R.id.main_currentpersonname);
		edittextview = (TextView) findViewById(R.id.main_edittextview);
		timetextview = (TextView) findViewById(R.id.main_curtimetextview);
		curstatustextview = (TextView) findViewById(R.id.main_mystatustextview);
		prompttextview = (TextView) findViewById(R.id.main_prompttextview);
		samestatustextview = (TextView) findViewById(R.id.main_samestatuspersonnum);
		researchprompttextview = (TextView) findViewById(R.id.main_researchprompttextview);
		sameresearchstatustextview = (TextView) findViewById(R.id.main_researchpersonnumtextview);
		shareimageview = (ImageView) findViewById(R.id.main_shareimageview);
		moreoperalinearlayout = (LinearLayout) findViewById(R.id.main_moreoperalinearlayout);
		moreoperalinearlayout.setVisibility(View.GONE);
		state_linearlayout = (LinearLayout) findViewById(R.id.main_statelinearlayout);
		search_linearlayout = (LinearLayout) findViewById(R.id.main_searchlinearlayout);

		// 查找
		search_linearlayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AboutDialog.SetOperaType(2);
				AboutDialog aboutDialog = new AboutDialog(MainActivity.this, 0,
						new PriorityListener() {
							@Override
							public void refreshPriorityUI(String str1,
									String str2, String str3, String str4) {
								// 如果按了确定键
								if (str1.equals("1")) {
									DealStatusCount(str2, 1);
									DealResearchingCount(str2, 2);
									ProduceServiceTime();
								}
							}

						});
				BmobUser bmobUser = BmobUser.getCurrentUser(MainActivity.this);
				if (null == bmobUser) {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"你还没有登录哦!", Toast.LENGTH_LONG);
				}else{
					aboutDialog.show();	
				}
			}
		});

		// 发状态
		state_linearlayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AboutDialog.SetOperaType(1);
				AboutDialog aboutDialog = new AboutDialog(MainActivity.this, 0,
						new PriorityListener() {
							@Override
							public void refreshPriorityUI(String str1,
									String str2, String str3, String str4) {
								// 如果按了确定键
								if (str1.equals("1")) {
									DealStatusCount(str2, 2);
									DealResearchingCount(str2, 1);
									ProduceServiceTime();
								}
							}

						});
				BmobUser bmobUser = BmobUser.getCurrentUser(MainActivity.this);
				if (null == bmobUser) {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"你还没有登录哦!", Toast.LENGTH_LONG);
				}else{
					aboutDialog.show();	
				}
			}
		});

		// 分享
		shareimageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ScreenShot screen = new ScreenShot(MainActivity.this);

				String strFilePath = "";
				// 截取当前屏幕
				Bitmap bmp = screen.GetCurrenScreenBitmap();
				if (bmp != null) {
					// 获取分享图片的路径
					strFilePath = screen.GetImageFilePath("screenshot");
					// 将图片保存到该文件下
					CommonUtils util = new CommonUtils(MainActivity.this);
					try {
						util.saveBitmapToFile(bmp, strFilePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"截图失败", Toast.LENGTH_LONG);
				}
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
				intent.putExtra(Intent.EXTRA_STREAM,
						Uri.fromFile(new File(strFilePath)));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent, getTitle()));
			}
		});

		curpersonnametextview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AboutDialog aboutDialog = new AboutDialog(MainActivity.this, 1,
						new PriorityListener() {
							@Override
							public void refreshPriorityUI(final String str1,
									String str2, String str3, String str4) {
								// 获取手机mac
								WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

								WifiInfo info = wifi.getConnectionInfo();

								String strMac = info.getMacAddress();
								if (null == strMac) {
									strMac = "";
								}
								// 对密码Mac进行加密
								String strPassWord = CommonUtils.MD5(strMac);
								BmobUser bu2 = new BmobUser();
								bu2.setUsername(str1);
								bu2.setPassword(strPassWord);
								bu2.signUp(MainActivity.this,
										new SaveListener() {
											@Override
											public void onSuccess() {
												Message msg = myhandler
														.obtainMessage();
												Bundle b = new Bundle();// 存放数据
												b.putString("personname", str1);
												msg.setData(b);
												msg.what = 0;
												myhandler.sendMessage(msg);
												CommonUtils
														.ShowToastCenter(
																getApplicationContext(),
																"登录成功",
																Toast.LENGTH_LONG);
											}

											@Override
											public void onFailure(int code,
													String msg) {
												if (202 == code) {
													// 此时表示用户名已经存在
													CommonUtils
															.ShowToastCenter(
																	getApplicationContext(),
																	"用户名已经存在",
																	Toast.LENGTH_LONG);
												} else {
													CommonUtils
															.ShowToastCenter(
																	getApplicationContext(),
																	"登录失败",
																	Toast.LENGTH_LONG);
												}
											}
										});
							}

						});
				BmobUser bmobUser = BmobUser.getCurrentUser(MainActivity.this);
				if (null == bmobUser) {
					curpersonnametextview.setText("登录");
					aboutDialog.show();
				}
			}

		});

	}

	Runnable checkrun = new Runnable() {
		public void run() {
			while (!bIsReminderUpdateApk) {
				Message msg = myhandler.obtainMessage();
				msg.what = 10;
				myhandler.sendMessage(msg);
				// 每隔3秒刷新一次界面
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	public boolean isConnectInternet() {

		ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null) { // 这个判断一定要加上，要不然会出错
			return networkInfo.isAvailable();
		}
		return false;
	}

	class MyHandler extends Handler {
		// 子类必须重写此方法,接受数据
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
				Bundle b = msg.getData();
				String strPersonName = b.getString("personname");
				if(!strPersonName.equals("")){
					curpersonnametextview.setText(strPersonName);
				}else{
					curpersonnametextview.setText("登录");
				}				
				break;
			case 1:
				b = msg.getData();
				long lRet = b.getLong("lRet");
				long lRet1 = b.getLong("lRet1");
				String strText;
			case 2:
				b = msg.getData();
				lRet = b.getLong("lRet");
				strPersonName = b.getString("strTextTemp");
				if (1 == lRet) {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"登录成功", Toast.LENGTH_LONG);
					curpersonnametextview.setText(strPersonName);
					SharedPreferences.Editor editor = msettings.edit();
					editor.putString("curpersonname", strPersonName);
					editor.commit();
				} else if (2 == lRet) {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"已经存在该用户", Toast.LENGTH_LONG);
				} else {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"登录失败", Toast.LENGTH_LONG);
				}
				break;
			case 3:
				b = msg.getData();
				String strContent = b.getString("strContent");
				int nPersonNum = b.getInt("nPersonNum");
				// 如果联网了
				if (isConnectInternet()) {
					long lvalue1 = (long) (Math.random() * 50);
					nPersonNum += lvalue1;
				}

				strText = "我现在正\"";
				strText += strContent;
				strText += "\"";
				curstatustextview.setText(strText);
				samestatustextview.setText("" + nPersonNum);

				strText = "现在正 \"";
				strText += strContent;
				strText += "\" 的人数为";
				prompttextview.setText(strText);
				break;
			case 4:
				b = msg.getData();
				strContent = b.getString("strContent");
				int lPlayPersonNum = b.getInt("lPlayPersonNum");
				// 如果联网了
				if (isConnectInternet()) {
					long lvalue2 = (long) (Math.random() * 80);
					lPlayPersonNum += lvalue2;
				}
				strText = "我现在正\"";
				strText += strContent;
				strText += "\"";
				curstatustextview.setText(strText);
				sameresearchstatustextview.setText("" + lPlayPersonNum);

				strText = "现在正查找 \"";
				strText += strContent;
				strText += "\" 的人数为";
				researchprompttextview.setText(strText);
				break;
			case 5:
				b = msg.getData();
				String strTime = b.getString("servicetime");
				String strTimeTemp = strTime.substring(0,
						strTime.length() - 3);
				if (strTime.equals("")) {
					timetextview.setText("网络连接错误");
				} else {
					timetextview.setText(strTimeTemp);
				}
				break;
			case 10:
				// 新版本更新提示
				if (!bIsReminderUpdateApk) {
					if (mupdateinfo != null) {
						int nNewVersion = Integer.parseInt(mupdateinfo
								.getVersion());
						String strDescript = mupdateinfo.getDescription();
						// 如果有新版本,那么就提示更新最新版本
						if (localVersion < nNewVersion) {
							// 发现新版本，提示用户更新
							AlertDialog.Builder alert = new AlertDialog.Builder(
									MainActivity.this);
							alert.setTitle("有新版本可用")
									.setMessage(strDescript)
									.setPositiveButton(
											"现在升级",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													// 创建文件
													FileUtil.createFile(getResources()
															.getString(
																	R.string.app_name));
													createNotification();
													createThread();
												}
											})
									.setNegativeButton(
											"以后再说",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											});
							alert.create().show();
							bIsReminderUpdateApk = true;
						}
					}
				}
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		bIsReminderUpdateApk = true;
		mthre = null;
		finish();
	}

	/***
	 * 检查是否更新版本
	 */
	public void checkVersion() {
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					downloadUpdateXMLFile(strUpdateXmlPath);
				} catch (Exception e) {
				}

			}
		}).start();
	}

	/***
	 * 下载文件
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public long downloadUpdateXMLFile(String down_url) throws Exception {
		int totalSize;// 文件总大小
		InputStream inputStream;
		int downloadCount = 0;// 已经下载好的大小
		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
		// 获取下载文件的size
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
		}
		inputStream = httpURLConnection.getInputStream();
		mupdateinfo = getUpdataInfo(inputStream);
		if (!mupdateinfo.getVersion().equals("")) {
			downloadCount = 1;
		}

		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		inputStream.close();

		return downloadCount;
	}

	/*
	 * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
	 */
	public static UpdateInfo getUpdataInfo(InputStream is) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");// 设置解析的数据源
		int type = parser.getEventType();
		UpdateInfo info = new UpdateInfo();// 实体
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("version".equals(parser.getName())) {
					info.setVersion(parser.nextText()); // 获取版本号
				} else if ("url".equals(parser.getName())) {
					info.setUrl(parser.nextText()); // 获取要升级的APK文件
				} else if ("description".equals(parser.getName())) {
					String strTemp = parser.nextText().replace("\\n", "\n");
					info.setDescription(strTemp); // 获取该文件的信息
				}
				break;
			}
			type = parser.next();
		}
		return info;
	}

	public void createNotification() {

		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		updateIntent = new Intent(this, MainActivity.class);
		updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
		notification = new Notification();
		// 设置通知在状态栏显示的图标
		notification.icon = R.drawable.point100;

		// 通知时在状态栏显示的内容
		notification.tickerText = "开始下载";
		// 通知被点击后，自动消失
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		/***
		 * 在这里我们用自定的view来显示Notification
		 */
		contentView = new RemoteViews(getPackageName(),
				R.layout.notification_item);
		contentView.setTextViewText(R.id.notificationTitle, "正在下载");
		contentView.setTextViewText(R.id.notificationPercent, "0%");
		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

		notification.contentView = contentView;

		notification.contentIntent = pendingIntent;

		notificationManager.notify(notification_id, notification);

	}

	/***
	 * 开线程下载
	 */
	public void createThread() {
		/***
		 * 更新UI
		 */
		final Handler handlerDownFile = new Handler() {
			@SuppressWarnings("deprecation")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWN_OK:

					// 下载完成，点击安装
					Uri uri = Uri.fromFile(FileUtil.updateFile);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri,
							"application/vnd.android.package-archive");

					MainActivity.this.startActivity(intent);
					if (notificationManager != null) {
						notificationManager.cancel(notification_id);
					}
					MainActivity.this.finish();
					break;
				case DOWN_ERROR:
					notification.setLatestEventInfo(MainActivity.this,
							getResources().getString(R.string.app_name),
							"下载失败", pendingIntent);
					break;

				default:
					stopService(updateIntent);
					break;
				}

			}

		};

		final Message message = new Message();

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					long downloadSize = downloadUpdateFile(down_url,
							FileUtil.updateFile.toString());
					if (downloadSize > 0) {
						// 下载成功
						message.what = DOWN_OK;
						handlerDownFile.sendMessage(message);
					}

				} catch (Exception e) {
					e.printStackTrace();
					message.what = DOWN_ERROR;
					handlerDownFile.sendMessage(message);
				}

			}
		}).start();
	}

	/***
	 * 下载文件
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public long downloadUpdateFile(String down_url, String file)
			throws Exception {
		int down_step = 5;// 提示step
		int totalSize;// 文件总大小
		int downloadCount = 0;// 已经下载好的大小
		int updateCount = 0;// 已经上传的文件大小
		InputStream inputStream;
		OutputStream outputStream;

		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
		// 获取下载文件的size
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
		}
		int nCode = httpURLConnection.getHeaderFieldInt("versioncode", -1);
		inputStream = httpURLConnection.getInputStream();
		outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
		byte buffer[] = new byte[1024];
		int readsize = 0;
		while ((readsize = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readsize);
			downloadCount += readsize;// 时时获取下载到的大小
			/**
			 * 每次增张5%
			 */
			if (updateCount == 0
					|| (downloadCount * 100 / totalSize - down_step) >= updateCount) {
				updateCount += down_step;
				contentView.setTextViewText(R.id.notificationPercent,
						updateCount + "%");
				contentView.setProgressBar(R.id.notificationProgress, 100,
						updateCount, false);
				// show_view
				notificationManager.notify(notification_id, notification);

			}

		}
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		inputStream.close();
		outputStream.close();

		return downloadCount;
	}

	private String SecondsToDate(long lSeconds) {
		Date dateOld = new Date(lSeconds); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, "yyyy-MM-dd HH:mm:ss"); // 把date类型的时间转换为string
		return sDateTime;
	}

	public String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}
}
