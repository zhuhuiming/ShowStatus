package com.status.serviceimpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.status.service.Service;

public class ServiceImpl implements Service {

	private static final String strBasePath = "http://www.meiliangshare.cn:8000/StatusServer/status";

	@Override
	public int LogIn(String strPersonName, String strPhoneNum, String strMac) {
		int nRet = 0;// 1表示成功,2表示账号重名,其他表示失败
		// 创建请求的url
		String url = strBasePath + "/login.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		paramas.add(new BasicNameValuePair("strPhoneNum", strPhoneNum));
		paramas.add(new BasicNameValuePair("strMac", strMac));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRet = parseLogIn(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	// 解析json的单个对象
	private int parseLogIn(String json) {
		int nRet = 0;// 1表示成功,2表示账号重名,其他表示失败
		try {
			nRet = new JSONObject(json).getInt("loginret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int InsertStatus(String strPersonName, String strContent,
			double dLongitude, double dLatitude) {
		int nRet = 0;// 0表示失败,1表示成功
		// 创建请求的url
		String url = strBasePath + "/insertstatus.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		paramas.add(new BasicNameValuePair("strContent", strContent));
		paramas.add(new BasicNameValuePair("dLongitude", dLongitude + ""));
		paramas.add(new BasicNameValuePair("dLatitude", dLatitude + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRet = parseInsertStatus(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	// 解析json的单个对象
	private int parseInsertStatus(String json) {
		int nRet = 0;// 0表示失败,1表示成功
		try {
			nRet = new JSONObject(json).getInt("insertstatusret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int InsertSearchRecord(String strPersonName, String strContent,
			double dLongitude, double dLatitude) {
		int nRet = 0;// 0表示失败,1表示成功
		// 创建请求的url
		String url = strBasePath + "/insertsearchrecord.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		paramas.add(new BasicNameValuePair("strContent", strContent));
		paramas.add(new BasicNameValuePair("dLongitude", dLongitude + ""));
		paramas.add(new BasicNameValuePair("dLatitude", dLatitude + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRet = parseInsertSearchRecord(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	// 解析json的单个对象
	private int parseInsertSearchRecord(String json) {
		int nRet = 0;// 0表示失败,1表示成功
		try {
			nRet = new JSONObject(json).getInt("insertsearchrecordret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public long GetSameStatusPersonNum(String strPersonName, String strContent,
			long lSeconds, double dLongitude, double dLatitude, int nType) {
		int nRet = 0;
		// 创建请求的url
		String url = strBasePath + "/getsamestatuspersonnum.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		paramas.add(new BasicNameValuePair("strContent", strContent));
		paramas.add(new BasicNameValuePair("lSeconds", lSeconds + ""));
		paramas.add(new BasicNameValuePair("dLongitude", dLongitude + ""));
		paramas.add(new BasicNameValuePair("dLatitude", dLatitude + ""));
		paramas.add(new BasicNameValuePair("nType", nType + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRet = parseGetSameStatusPersonNum(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	// 解析json的单个对象
	private int parseGetSameStatusPersonNum(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("samestatuspersonnum");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public long GetIsSearchingPersonNum(String strPersonName,
			String strContent, long lSeconds, double dLongitude,
			double dLatitude, int nType) {
		int nRet = 0;
		// 创建请求的url
		String url = strBasePath + "/getissearchingpersonnum.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		paramas.add(new BasicNameValuePair("strContent", strContent));
		paramas.add(new BasicNameValuePair("lSeconds", lSeconds + ""));
		paramas.add(new BasicNameValuePair("dLongitude", dLongitude + ""));
		paramas.add(new BasicNameValuePair("dLatitude", dLatitude + ""));
		paramas.add(new BasicNameValuePair("nType", nType + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRet = parseGetIsSearchingPersonNum(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	// 解析json的单个对象
	private int parseGetIsSearchingPersonNum(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("issearchingpersonnum");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public String GetServiceTime() {
		String strTime = "";
		// 创建请求的url
		String url = strBasePath + "/getservicetime.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			// httpPost.setEntity(new UrlEncodedFormEntity(paramas,
			// HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					strTime = parseGetServiceTime(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strTime;
	}

	// 解析json的单个对象
	private String parseGetServiceTime(String json) {
		String strTime = "";
		try {
			strTime = new JSONObject(json).getString("servicetime");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strTime;
	}

	@Override
	public String GetAccountName(String strMac) {
		String strAccountName = "";
		// 创建请求的url
		String url = strBasePath + "/getaccountname.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strMac", strMac));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					strAccountName = parseGetAccountName(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strAccountName;
	}

	// 解析json的单个对象
	private String parseGetAccountName(String json) {
		String strAccountName = "";
		try {
			strAccountName = new JSONObject(json).getString("straccountname");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strAccountName;
	}

	@Override
	public int GetUsingPersonNum() {
		int nRet = 0;
		// 创建请求的url
		String url = strBasePath + "/getusingpersonnum.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRet = parseGetUsingPersonNum(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	// 解析json的单个对象
	private int parseGetUsingPersonNum(String json) {
		int nRet = 0;// 0表示失败,1表示成功
		try {
			nRet = new JSONObject(json).getInt("usingnum");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}
}
