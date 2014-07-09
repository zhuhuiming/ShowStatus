package com.status.utils;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class ScreenShot {
	
	public static String rootpath = "showstatus/";// ��װĿ¼
	
	private Context mcontext = null;

	public ScreenShot(Context context) {
		mcontext = context;
	}

	// ��ȡ��ĻͼƬ
	public Bitmap GetCurrenScreenBitmap() {
		Bitmap bmp = null;
		Activity activity = (Activity) mcontext;
		if (activity != null) {
			View view = activity.getWindow().getDecorView();
			view.setDrawingCacheEnabled(true);
			view.buildDrawingCache();
			Bitmap bitmap = view.getDrawingCache();
			Rect rect = new Rect();
			activity.getWindow().getDecorView()
					.getWindowVisibleDisplayFrame(rect);
			int statusBarHeight = rect.top;

			int width = activity.getWindowManager().getDefaultDisplay()
					.getWidth();
			int height = activity.getWindowManager().getDefaultDisplay()
					.getHeight();

			bmp = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height
					- statusBarHeight);
			view.destroyDrawingCache();
		}
		return bmp;
	}

	// ��ȡ����ͼƬ��·��
	public String GetImageFilePath(String name) {

		String strPath = "";
		// �ж��Ƿ����sd��
		boolean sdExist = android.os.Environment.MEDIA_MOUNTED
				.equals(android.os.Environment.getExternalStorageState());
		if (!sdExist) {// ���������,
			Log.e("SD������", "SD�������ڣ������SD��");
		} else {// �������
				// ��ȡsd��·��
			strPath = android.os.Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			strPath += "/";
			strPath += rootpath;// ���ݿ�����Ŀ¼
			String dbPath = strPath + name + ".png";// ���ݿ�·��
			// �ж�Ŀ¼�Ƿ���ڣ��������򴴽���Ŀ¼
			File dirFile = new File(strPath);
			if (!dirFile.exists())
				dirFile.mkdirs();
			// �ж��ļ��Ƿ���ڣ��������򴴽����ļ�
			File dbFile = new File(dbPath);
			if (!dbFile.exists()) {
				try {
					dbFile.createNewFile();// �����ļ�
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				// isFileCreateSuccess = true;
				strPath = dbPath;
			}
		}
		return strPath;
	}
}
