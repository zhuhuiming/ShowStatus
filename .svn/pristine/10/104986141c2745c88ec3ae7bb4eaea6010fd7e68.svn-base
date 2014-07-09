package com.status.showstatus;

import com.status.utils.CommonUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class AboutDialog extends AlertDialog implements OnClickListener {

	// 状态提示信息控件
	TextView statustextview;
	// 状态信息控件
	TextView statusinfoedittext;
	// 发送
	LinearLayout surebutton;
	// 取消
	LinearLayout cancelbutton;
	// 操作类型,1表示发状态,2表示查找
	static int nOperaType = 0;
	// 编辑框中输入字符个数的最大值
	static final int nMaxChars = 3;

	// 登录用户名
	EditText personnameedittext;
	// 登录控件
	LinearLayout loginlinearlayout;

	PriorityListener mlistener = null;

	/**
	 * 自定义Dialog监听器
	 */
	public interface PriorityListener {
		/**
		 * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
		 */
		public void refreshPriorityUI(String str1, String str2, String str3,
				String str4);
	}

	public AboutDialog(final Context context, int nType,
			PriorityListener listener) {
		super(context);
		mlistener = listener;
		final View cfg_view;
		switch (nType) {
		case 0:
			cfg_view = getLayoutInflater().inflate(R.layout.state_edit, null);
			setView(cfg_view);

			statustextview = (TextView) cfg_view
					.findViewById(R.id.stateedit_prompttextview);
			statusinfoedittext = (TextView) cfg_view
					.findViewById(R.id.stateedit_statusedittext);
			surebutton = (LinearLayout) cfg_view
					.findViewById(R.id.stateedit_sendlinearlayout);
			cancelbutton = (LinearLayout) cfg_view
					.findViewById(R.id.stateedit_cancellinearlayout);

			if (1 == nOperaType) {
				statustextview.setText("我现在正");
			} else if (2 == nOperaType) {
				statustextview.setText("我现在正查看");
			}

			statusinfoedittext.addTextChangedListener(new TextWatcher() {

				// 输入中字符值
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}

				// 输入后字符值
				@Override
				public void afterTextChanged(Editable s) {
					if (s.length() > nMaxChars) {
						CommonUtils.ShowToastCenter(context, "不能超过三个字哦", Toast.LENGTH_LONG);
						int pos = s.length() - 1;
						s.delete(pos, pos + 1);
					}
				}

				// 输入前字符值
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

				}

			});
			
			surebutton.setOnClickListener(new LinearLayout.OnClickListener(){

				@Override
				public void onClick(View v) {
					String strContent = statusinfoedittext.getText().toString();
					if(strContent.equals("")){
						CommonUtils.ShowToastCenter(context, "请输入内容", Toast.LENGTH_LONG);
					}else{
						mlistener.refreshPriorityUI("1", strContent, "", "");
						AboutDialog.this.dismiss();	
					}
				}
				
			});
			
			cancelbutton.setOnClickListener(new LinearLayout.OnClickListener(){

				@Override
				public void onClick(View v) {
					mlistener.refreshPriorityUI("2", "", "", "");
					AboutDialog.this.dismiss();
				}
				
			});

			break;
		case 1:
			cfg_view = getLayoutInflater().inflate(R.layout.log, null);
			setView(cfg_view);
			personnameedittext = (EditText) cfg_view
					.findViewById(R.id.log_personnameedittext);
			loginlinearlayout = (LinearLayout) cfg_view
					.findViewById(R.id.log_logintextviewlinearlayout);
			
			loginlinearlayout.setOnClickListener(new LinearLayout.OnClickListener(){

				@Override
				public void onClick(View v) {
					String strContent = personnameedittext.getText().toString();
					if(strContent.equals("")){
						CommonUtils.ShowToastCenter(context, "用户名不能为空", Toast.LENGTH_LONG);
					}else{
						mlistener.refreshPriorityUI(strContent, "", "", "");
						AboutDialog.this.dismiss();	
					}
				}
				
			});
			break;
		}
	}

	@Override
	public void onClick(View v) {
	}

	public static void SetOperaType(int nType) {
		nOperaType = nType;
	}
}