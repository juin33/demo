package com.example.demo.support;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.Map.Entry;

public class HttpClient {
	private static final RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(50000).setConnectTimeout(50000).setSocketTimeout(50000).build();
	private static final CloseableHttpClient httpclient = HttpClients.custom().setMaxConnTotal(100).setMaxConnPerRoute(100).setDefaultRequestConfig(requestConfig).build();

	public static String addParamToUrl(String url, Map<String, String> queryParams) {
		if (!url.endsWith("?") && !queryParams.isEmpty())
			url += "?";

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		for (Entry<String, String> entry : queryParams.entrySet()) {
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		String paramString = URLEncodedUtils.format(params, "UTF-8");
		url += paramString;
		return url;
	}

	public static HttpResponse get(String url) throws Exception {
		HttpGet httpget = new HttpGet(url);
		return httpclient.execute(httpget);
	}
	public static byte[] get1(String url) throws Exception {
		HttpGet httpget = new HttpGet(url);
		HttpEntity entity = httpclient.execute(httpget).getEntity();

		InputStream in = entity.getContent();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static HttpResponse get(String url,Map<String, String> queryParams) throws Exception {
		HttpGet httpget = new HttpGet(addParamToUrl(url, queryParams));
		return httpclient.execute(httpget);
	}

	public static HttpResponse post(String url) throws Exception {
		HttpPost httppost = new HttpPost(url);
		return httpclient.execute(httppost);
	}

	public static HttpResponse post(String url, Map<String, String> params) throws Exception {
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> nps = map2NameValuePairs(params);
		UrlEncodedFormEntity en = new UrlEncodedFormEntity(nps, Consts.UTF_8);
		httppost.setEntity(en);
		en.setContentEncoding("UTF-8");
		return httpclient.execute(httppost);
	}
	private static List<NameValuePair> map2NameValuePairs(final Map<String, String> params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (params == null) {
			return nameValuePairs;
		}
		for (Entry<String, String> entry : params.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return nameValuePairs;
	}


	public static HttpResponse postJson(String url, String body) throws Exception {
		System.out.println("发送的body:" + body);
		HttpPost method = new HttpPost(url);
		StringEntity stringEntity = new StringEntity(body, Consts.UTF_8);
		stringEntity.setContentEncoding("UTF-8");
		stringEntity.setContentType("application/json");
		method.setEntity(stringEntity);
		return httpclient.execute(method);
	}

	public static HttpResponse postJson(String url, String body, Map<String, String> headerParams) throws Exception {
		System.out.println("发送的body:" + body);
		HttpPost method = new HttpPost(url);

		if(headerParams==null){
			headerParams = new HashMap<>();
		}
		for (Entry<String, String> entry : headerParams.entrySet()) {
			method.addHeader(entry.getKey(), entry.getValue());
		}

		StringEntity stringEntity = new StringEntity(body, Consts.UTF_8);
		stringEntity.setContentEncoding("UTF-8");
		stringEntity.setContentType("application/json");
		method.setEntity(stringEntity);
		return httpclient.execute(method);
	}


	public static String getSignOrderStr(Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();
		List<Entry> keyList = new ArrayList<>(params.entrySet());
		Collections.sort(keyList, (a, b) -> a.getKey().toString().compareTo(b.getKey().toString()));
		for (Iterator iterator = keyList.iterator(); iterator.hasNext(); ) {
			Entry entry = (Entry) iterator.next();
			if ("sign".equals(entry.getKey()))
				continue;

			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			sb.append("&");
		}

		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	public static JSONObject orderByKeyMapToJson(Map<String,Object> params){
		if(params == null || params.isEmpty()){
			return new JSONObject();
		}

		Set<String> keys = new TreeSet<String>(new Comparator<String>(){
			public int compare(String o1, String o2)   {
				return  o1.compareTo(o2);
			}
		});
		keys.addAll(params.keySet());

		JSONObject obj = new JSONObject(true); //按照入key的顺序固定
		for(String key : keys){
			if("sign".equalsIgnoreCase(key)){
				continue;
			}
			obj.put(key,params.get(key));
		}
		return obj;
	}

//	public static void main(String[] args){
////		ThreadTest tt = null;
////		for (int i=0;i<10;i++){
////			tt = new ThreadTest();
////			tt.start();
////		}
//		try {
//			HttpResponse response = HttpClient.post("http://creditease-echo-interactive.test.91gfd.cn/api/inner/count/stat?token=dashu&type=1&date=20181108");
//			System.out.println(response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

}
