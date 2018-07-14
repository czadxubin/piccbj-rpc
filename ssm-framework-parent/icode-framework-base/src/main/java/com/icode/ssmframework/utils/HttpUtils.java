package com.icode.ssmframework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	private static PoolingHttpClientConnectionManager poolManager = null;
	private HttpUtils() {}
	static {
		// SSL context for secure connections can be created either based on
		// system or application specific properties.
		SSLContext sslcontext = SSLContexts.createSystemDefault();
		Registry<ConnectionSocketFactory> connFactory = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(sslcontext))
				.build();
		poolManager = new PoolingHttpClientConnectionManager(connFactory );
		ConnectionConfig defaultConnectionConfig = ConnectionConfig.custom().setCharset(Charset.forName("UTF-8")).setBufferSize(1024*8).build();
		poolManager.setDefaultConnectionConfig(defaultConnectionConfig );
	}
	private static CloseableHttpClient getHttpClient() {
		// Use custom cookie store if necessary.
        CookieStore cookieStore = new BasicCookieStore();
        // Use custom credentials provider if necessary.
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		// Create global request configuration
        RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setCookieSpec(CookieSpecs.DEFAULT)
            .setExpectContinueEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
            .build();

        // Create an HttpClient with the given custom dependencies and configuration.
        return  HttpClients.custom()
            .setConnectionManager(poolManager)
            .setDefaultCookieStore(cookieStore)
            .setDefaultCredentialsProvider(credentialsProvider)
//            .setProxy(new HttpHost("myproxy", 8080))
            .setDefaultRequestConfig(defaultRequestConfig)
            .build();
	}
	/**
	 * post数据指定编码
	 * @param reqBody
	 * @param encoding
	 * @return
	 * 		返回影响数据
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String post(String requestUrl,String reqBody,String encoding) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = null;
		String resStr = null;
		Exception ex = null;
		try{
			httpClient = getHttpClient();
			HttpPost request = new HttpPost(requestUrl);
			request.setEntity(new StringEntity(reqBody, encoding));
			CloseableHttpResponse response = httpClient.execute(request);
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode()==200) {
				resStr = IOUtils.toString(response.getEntity().getContent(),encoding);
			}
		}catch (Exception e) {
			ex = e;
			throw e;
		}finally {
			if(ex==null) {
				logger.debug("[请求URL：{}]-[请求内容：{}]-[返回内容：{}]",requestUrl,reqBody,resStr);
			}else {
				logger.debug("[请求URL：{}]-[请求内容：{}]-[返回内容：{}]-[异常信息：{}]",requestUrl,reqBody,resStr,obtainExcpetionDetailMsg(ex));
			}
		}
		return resStr;
	}
	
	/**
	 *  post表单数据
	 * @param requestUrl
	 * @param params
	 * @param encoding
	 * 				返回编码
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String requestUrl,Map<String,String> params,String encoding) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = null;
		String resStr = null;
		Exception ex = null;
		try{
			httpClient = getHttpClient();
			HttpPost request = new HttpPost(requestUrl);
			if(params!=null&&params.size()>0) {
				List<NameValuePair> parameters = new ArrayList<>(params.size());
				for (Entry<String, String> entry : params.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					parameters.add(new BasicNameValuePair(key, value));
				}
				HttpEntity reqEntity = new UrlEncodedFormEntity(parameters);
				request.setEntity(reqEntity );
			}
			CloseableHttpResponse response = httpClient.execute(request);
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode()==200) {
				resStr = IOUtils.toString(response.getEntity().getContent(),encoding);
			}
		}catch (Exception e) {
			ex = e;
			throw e;
		}finally {
			if(ex==null) {
				logger.debug("[请求URL：{}]-[请求内容：{}]-[返回内容：{}]",requestUrl,JSONObject.toJSONString(params),resStr);
			}else {
				logger.debug("[请求URL：{}]-[请求内容：{}]-[返回内容：{}]-[异常信息：{}]",requestUrl,JSONObject.toJSONString(params),resStr,obtainExcpetionDetailMsg(ex));
			}
		}
		return resStr;
	}
	/**
	 * 获取异常详细信息
	 * @param ex
	 * @return
	 */
	public static String obtainExcpetionDetailMsg(Exception ex) {
		StringWriter out = new StringWriter();
		ex.printStackTrace(new PrintWriter(out));
		return out.toString();
	}
}
