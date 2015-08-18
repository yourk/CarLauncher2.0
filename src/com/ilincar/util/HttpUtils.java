package com.ilincar.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.ilincar.base.BaseActivity;
import com.ilincar.config.MessageConfig;
import com.ilincar.config.UrlConfig;

import android.os.Message;

//Http请求的工具类
public class HttpUtils {

	private static final int TIMEOUT_IN_MILLIONS = 5000;

	public interface CallBack {
		void onRequestComplete(String result);
	}

	/**
	 * 异步的Get请求
	 * 
	 * @param urlStr
	 * @param callBack
	 */
	public static void doGetAsyn(final String urlStr, final CallBack callBack) {
		new Thread() {
			public void run() {
				try {
					String result = doGet(urlStr);
					if (callBack != null) {
						callBack.onRequestComplete(result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	/**
	 * 异步的Post请求
	 * 
	 * @param urlStr
	 * @param params
	 *            请求参数形式 name1=value1&name2=value2
	 * @param callBack
	 * @throws Exception
	 */
	public static void doPostAsyn(final String urlStr, final String params,
			final CallBack callBack) throws Exception {
		new Thread() {
			public void run() {
				try {
					String result = doPost(urlStr, params);
					if (callBack != null) {
						callBack.onRequestComplete(result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 异步的Get请求
	 * 
	 * @param urlStr
	 *            :接口地址
	 * @param type
	 *            :Config.*
	 * */
	public static void MydoGetAsyn(final String urlStr, final int type) {
		new Thread() {
			public void run() {
				try {
					String url = UrlConfig.URL + urlStr;
					String result = doGet(url);
					JSONObject jsonObject = new JSONObject(result);
					Message message = new Message();
					message.what = type;
					message.obj = jsonObject;
					BaseActivity.sendMsg(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 异步的post请求
	 * 
	 * @param urlStr
	 *            :接口地址
	 * @param params
	 *            :请求参数 请求参数形式 name1=value1&name2=value2
	 * */
	public static void MydoPostAsyn(final String urlStr, final String params,
			final int type) {
		new Thread() {
			public void run() {
				try {
					String url = UrlConfig.URL + urlStr;
					String result = doPost(url, params);
					JSONObject jsonObject = new JSONObject(result);
					Message message = new Message();
					message.what = type;
					message.obj = jsonObject;
					BaseActivity.sendMsg(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * Get请求，获得返回数据
	 * 
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String urlStr) {
		URL url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
			conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
				baos = new ByteArrayOutputStream();
				int len = -1;
				byte[] buf = new byte[128];
				while ((len = is.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}
				baos.flush();
				return baos.toString();
			} else {
				Message message = new Message();
				message.what = MessageConfig.NETERR;
				BaseActivity.sendMsg(message);
				throw new RuntimeException(" responseCode is not 200 ... ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
			}
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
			}
			conn.disconnect();
		}

		return "";

	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 * @throws Exception
	 */
	public static String doPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setUseCaches(false);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
			conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);

			if (param != null && !param.trim().equals("")) {
				// 获取URLConnection对象对应的输出流
				out = new PrintWriter(conn.getOutputStream());
				// 发送请求参数
				out.print(param);
				// flush输出流的缓冲
				out.flush();
			}
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 另一种Get请求，获得返回数据
	 * 
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	public static String MyGet(String url) {
		try {
			URL urls = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.connect();
			if (conn.getResponseCode() == 200) {
				// 接收数据
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "utf-8"));
				String data = in.readLine();
				return data;
			} else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 另一种Post请求，获得返回数据
	 * 
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	public static String MyPost(String url, Map<String, String> param, int type)
			throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		MultipartEntity entity = new MultipartEntity();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() > 0) {
					entity.addPart(entry.getKey(),
							new StringBody(entry.getValue()));
				}
			}
		}
		post.setEntity(entity);
		HttpResponse response = httpClient.execute(post);
		int stateCode = response.getStatusLine().getStatusCode();
		if (stateCode == HttpStatus.SC_OK) {
			HttpEntity result = response.getEntity();
			if (result != null) {
				InputStream is = result.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "utf-8"));
				String data = br.readLine();
				return data;
			}
		} else {
			System.out.println("获取失败");
		}
		post.abort();
		return null;
	}

	/**
	 * @param url
	 *            :接口地址
	 * @param param
	 *            :参数
	 * @param file
	 *            图片上传
	 * @param key
	 *            图片键值
	 * */
	public static void PictureUpload(final String url,
			final Map<String, String> param, final File file, final int type,
			final String key) {
		new Thread() {
			@Override
			public void run() {

				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost post = new HttpPost(UrlConfig.URL + url);
					MultipartEntity entity = new MultipartEntity();
					if (param != null && !param.isEmpty()) {
						for (Map.Entry<String, String> entry : param.entrySet()) {
							if (entry.getValue() != null
									&& entry.getValue().trim().length() > 0) {
								entity.addPart(entry.getKey(), new StringBody(
										entry.getValue()));
							}
						}
					}
					if (file != null && file.exists()) {
						entity.addPart(key, new FileBody(file));
					}
					post.setEntity(entity);
					HttpResponse response;
					response = httpClient.execute(post);
					int stateCode = response.getStatusLine().getStatusCode();
					if (stateCode == HttpStatus.SC_OK) {
						HttpEntity result = response.getEntity();
						if (result != null) {
							InputStream is = result.getContent();
							BufferedReader br = new BufferedReader(
									new InputStreamReader(is, "utf-8"));
							String data = br.readLine();
							JSONObject body = new JSONObject(data);
							Message msg = Message.obtain();
							msg.what = type;
							msg.obj = body;
							BaseActivity.sendMsg(msg);
						}
					} else {
						System.out.println("获取失败");
					}
					post.abort();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();

	}

	/**
	 * 文件下载
	 */
	public static void fileDownload(final String url, final int type,
			final String path) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String[] jd = url.split("/");
				String string = "";
				for (int i = 1; i < jd.length-1; i++) {
					string=string+"/"+jd[i];
				}
				try {
					File file = new File(path + string);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdirs();
					}
					File file2 = new File(path + string, jd[jd.length - 1]);
					if (file2.exists()) {
					} else {
						URL url1 = new URL(UrlConfig.URL + url);
						// 创建连接
						HttpURLConnection conn = (HttpURLConnection) url1
								.openConnection();
						conn.connect();
						// 获取文件大小
						// int length = conn.getContentLength();
						// 创建输入流
						InputStream is = conn.getInputStream();
						FileOutputStream fos = new FileOutputStream(file2);
						int count = 0;
						// 缓存
						byte buf[] = new byte[1024];
						// 写入到文件中
						while ((count = is.read(buf)) != -1) {
							fos.write(buf, 0, count);
						}
						fos.close();
						is.close();
					}
					Message message = new Message();
					message.what = type;
					message.obj = file2.getPath();
					BaseActivity.sendMsg(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
