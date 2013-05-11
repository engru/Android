package com.wkey.develop.http;

import java.io.BufferedInputStream;
import java.io.File; 
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import com.wkey.develop.config.Constants;
import com.wkey.develop.utils.SSLSocketFactoryEx;

import android.util.Log;

//import com.ch_linghu.fanfoudroid.TwitterApplication;
//import com.ch_linghu.fanfoudroid.fanfou.Configuration;
//import com.ch_linghu.fanfoudroid.fanfou.RefuseError;
//import com.ch_linghu.fanfoudroid.util.DebugTimer;

/**
 * Wrap of org.apache.http.impl.client.DefaultHttpClient
 * 
 * @author lds
 * 
 */
public class HttpClient {

	private static final String TAG = "HttpClient";
	private final static boolean DEBUG = true;//Configuration.getDebug();

	/** OK: Success! */
	public static final int OK = 200;
	/** Not Modified: There was no new data to return. */
	public static final int NOT_MODIFIED = 304;
	/**
	 * Bad Request: The request was invalid. An accompanying error message will
	 * explain why. This is the status code will be returned during rate
	 * limiting.
	 */
	public static final int BAD_REQUEST = 400;
	/** Not Authorized: Authentication credentials were missing or incorrect. */
	public static final int NOT_AUTHORIZED = 401;
	/**
	 * Forbidden: The request is understood, but it has been refused. An
	 * accompanying error message will explain why.
	 */
	public static final int FORBIDDEN = 403;
	/**
	 * Not Found: The URI requested is invalid or the resource requested, such
	 * as a user, does not exists.
	 */
	public static final int NOT_FOUND = 404;
	/**
	 * Not Acceptable: Returned by the Search API when an invalid format is
	 * specified in the request.
	 */
	public static final int NOT_ACCEPTABLE = 406;
	/**
	 * Internal Server Error: Something is broken. Please post to the group so
	 * the Weibo team can investigate.
	 */
	public static final int INTERNAL_SERVER_ERROR = 500;
	/** Bad Gateway: Weibo is down or being upgraded. */
	public static final int BAD_GATEWAY = 502;
	/**
	 * Service Unavailable: The Weibo servers are up, but overloaded with
	 * requests. Try again later. The search and trend methods use this to
	 * indicate when you are being rate limited.
	 */
	public static final int SERVICE_UNAVAILABLE = 503;

	private static final int CONNECTION_TIMEOUT_MS = 30 * 1000;
	private static final int SOCKET_TIMEOUT_MS = 30 * 1000;

	public static final int RETRIEVE_LIMIT = 20;
	public static final int RETRIED_TIME = 3;

	private static final String SERVER_HOST = "api.fanfou.com";

	private DefaultHttpClient mClient;
	private AuthScope mAuthScope;
	private BasicHttpContext localcontext;

	private String mUserId;
	private String mPassword;

	private static boolean isAuthenticationEnabled = false;
	//CookieStore cookieStore;
	public HttpClient() {
		prepareHttpClient();
		//cookieStore = new BasicCookieStore();
	}

	/**
	 * @param user_id
	 *            auth user
	 * @param password
	 *            auth password
	 */
	public HttpClient(String user_id, String password) {
		prepareHttpClient();
		setCredentials(user_id, password);
	}

	/**
	 * Empty the credentials
	 */
	public void reset() {
		setCredentials("", "");
	}

	/**
	 * @return authed user id
	 */
	public String getUserId() {
		return mUserId;
	}

	/**
	 * @return authed user password
	 */
	public String getPassword() {
		return mPassword;
	}

	/**
	 * @param hostname
	 *            the hostname (IP or DNS name)
	 * @param port
	 *            the port number. -1 indicates the scheme default port.
	 * @param scheme
	 *            the name of the scheme. null indicates the default scheme
	 */
	public void setProxy(String host, int port, String scheme) {
		HttpHost proxy = new HttpHost(host, port, scheme);
		mClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}

	public void removeProxy() {
		mClient.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
	}

	private void enableDebug() {
		Log.d(TAG, "enable apache.http debug");

		java.util.logging.Logger.getLogger("org.apache.http").setLevel(
				java.util.logging.Level.FINEST);
		java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(
				java.util.logging.Level.FINER);
		java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(
				java.util.logging.Level.OFF);

		/*
		 * System.setProperty("log.tag.org.apache.http", "VERBOSE");
		 * System.setProperty("log.tag.org.apache.http.wire", "VERBOSE");
		 * System.setProperty("log.tag.org.apache.http.headers", "VERBOSE");
		 * 
		 * 鍦ㄨ繖閲屼娇鐢⊿ystem.setProperty璁剧疆涓嶄細鐢熸晥, 鍘熷洜涓嶆槑, 蹇呴』鍦ㄧ粓绔笂杈撳叆浠ヤ笅鍛戒护鏂硅兘寮�惎http璋冭瘯淇℃伅: > adb
		 * shell setprop log.tag.org.apache.http VERBOSE > adb shell setprop
		 * log.tag.org.apache.http.wire VERBOSE > adb shell setprop
		 * log.tag.org.apache.http.headers VERBOSE
		 */
	}

	/**
	 * Setup DefaultHttpClient
	 * 
	 * Use ThreadSafeClientConnManager.
	 * 
	 */
	private void prepareHttpClient() {
		if (DEBUG) {
			enableDebug();
		}
		/*
        try {  
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
            trustStore.load(null, null);  
            
            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);  
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
    
            //HttpParams params = new BasicHttpParams();  
           // HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
            //HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);  
    
            SchemeRegistry registry = new SchemeRegistry();  
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
            registry.register(new Scheme("https", sf, 443));  
    
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);  
    
            //return new DefaultHttpClient(ccm, params);  
        } catch (Exception e) {  
            //return new DefaultHttpClient();  
        }  
        */
		try{
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
	        trustStore.load(null, null);  
	        
	        SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);  
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
	        
	        
		// Create and initialize HTTP parameters
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 10);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", /*SSLSocketFactory
				.getSocketFactory()*/sf, 443));

		// Create an HttpClient with the ThreadSafeClientConnManager.
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
				params, schemeRegistry);
		mClient = new DefaultHttpClient(cm, params);

		// Setup BasicAuth
		BasicScheme basicScheme = new BasicScheme();
		mAuthScope = new AuthScope(SERVER_HOST, AuthScope.ANY_PORT);

		// mClient.setAuthSchemes(authRegistry);
		mClient.setCredentialsProvider(new BasicCredentialsProvider());

		// Generate BASIC scheme object and stick it to the local
		// execution context
		localcontext = new BasicHttpContext();
		localcontext.setAttribute("preemptive-auth", basicScheme);

		// first request interceptor
		mClient.addRequestInterceptor(preemptiveAuth, 0);
		// Support GZIP
		mClient.addResponseInterceptor(gzipResponseIntercepter);

		// TODO: need to release this connection in httpRequest()
		// cm.releaseConnection(conn, validDuration, timeUnit);
		// httpclient.getConnectionManager().shutdown();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * HttpRequestInterceptor for DefaultHttpClient
	 */
	private static HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {
		@Override
		public void process(final HttpRequest request, final HttpContext context) {
			AuthState authState = (AuthState) context
					.getAttribute(ClientContext.TARGET_AUTH_STATE);
			CredentialsProvider credsProvider = (CredentialsProvider) context
					.getAttribute(ClientContext.CREDS_PROVIDER);
			HttpHost targetHost = (HttpHost) context
					.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

			if (authState.getAuthScheme() == null) {
				AuthScope authScope = new AuthScope(targetHost.getHostName(),
						targetHost.getPort());
				Credentials creds = credsProvider.getCredentials(authScope);
				if (creds != null) {
					authState.setAuthScheme(new BasicScheme());
					authState.setCredentials(creds);
				}
			}
		}
	};

	private static HttpResponseInterceptor gzipResponseIntercepter = new HttpResponseInterceptor() {

		@Override
		public void process(HttpResponse response, HttpContext context)
				throws org.apache.http.HttpException, IOException {
			HttpEntity entity = response.getEntity();
			Header ceheader = entity.getContentEncoding();
			if (ceheader != null) {
				HeaderElement[] codecs = ceheader.getElements();
				for (int i = 0; i < codecs.length; i++) {
					if (codecs[i].getName().equalsIgnoreCase("gzip")) {
						response.setEntity(new GzipDecompressingEntity(response
								.getEntity()));
						return;
					}
				}
			}

		}
	};

	static class GzipDecompressingEntity extends HttpEntityWrapper {

		public GzipDecompressingEntity(final HttpEntity entity) {
			super(entity);
		}

		@Override
		public InputStream getContent() throws IOException,
				IllegalStateException {

			// the wrapped entity's getContent() decides about repeatability
			InputStream wrappedin = wrappedEntity.getContent();
			return new GZIPInputStream(wrappedin);
		}

		@Override
		public long getContentLength() {
			// length of ungzipped content is not known
			return -1;
		}

	}

	/**
	 * Setup Credentials for HTTP Basic Auth
	 * 
	 * @param username
	 * @param password
	 */
	public void setCredentials(String username, String password) {
		mUserId = username;
		mPassword = password;
		mClient.getCredentialsProvider().setCredentials(mAuthScope,
				new UsernamePasswordCredentials(username, password));
		isAuthenticationEnabled = true;
	}

	public Response post(String url, ArrayList<BasicNameValuePair> postParams,
			boolean authenticated) throws HttpException {
		if (null == postParams) {
			postParams = new ArrayList<BasicNameValuePair>();
		}
		return httpRequest(url, postParams, authenticated, HttpPost.METHOD_NAME);
	}

	public Response post(String url, ArrayList<BasicNameValuePair> params)
			throws HttpException {
		return httpRequest(url, params, false, HttpPost.METHOD_NAME);
	}

	public Response post(String url, boolean authenticated)
			throws HttpException {
		return httpRequest(url, null, authenticated, HttpPost.METHOD_NAME);
	}

	public Response post(String url) throws HttpException {
		return httpRequest(url, null, false, HttpPost.METHOD_NAME);
	}

	public Response post(String url, File file) throws HttpException {
		return httpRequest(url, null, file, false, HttpPost.METHOD_NAME);
	}

	/**
	 * POST涓�釜鏂囦欢
	 * 
	 * @param url
	 * @param file
	 * @param authenticate
	 * @return
	 * @throws HttpException
	 */
	public Response post(String url, File file, boolean authenticate)
			throws HttpException {
		return httpRequest(url, null, file, authenticate, HttpPost.METHOD_NAME);
	}

	public Response get(String url, ArrayList<BasicNameValuePair> params,
			boolean authenticated) throws HttpException {
		return httpRequest(url, params, authenticated, HttpGet.METHOD_NAME);
	}

	public Response get(String url, ArrayList<BasicNameValuePair> params)
			throws HttpException {
		return httpRequest(url, params, false, HttpGet.METHOD_NAME);
	}

	public Response get(String url) throws HttpException {
		return httpRequest(url, null, false, HttpGet.METHOD_NAME);
	}

	public Response get(String url, boolean authenticated) throws HttpException {
		return httpRequest(url, null, authenticated, HttpGet.METHOD_NAME);
	}

	public Response httpRequest(String url,
			ArrayList<BasicNameValuePair> postParams, boolean authenticated,
			String httpMethod) throws HttpException {
		return httpRequest(url, postParams, null, authenticated, httpMethod);
	}

	/**
	 * Execute the DefaultHttpClient
	 * 
	 * @param url
	 *            target
	 * @param postParams
	 * @param file
	 *            can be NULL
	 * @param authenticated
	 *            need or not
	 * @param httpMethod
	 *            HttpPost.METHOD_NAME HttpGet.METHOD_NAME
	 *            HttpDelete.METHOD_NAME
	 * @return Response from server
	 * @throws HttpException
	 *             姝ゅ紓甯稿寘瑁呬簡涓�郴鍒楀簳灞傚紓甯�<br />
	 * <br />
	 *             1. 搴曞眰寮傚父, 鍙娇鐢╣etCause()鏌ョ湅: <br />
	 *             <li>URISyntaxException, 鐢盽new URI` 寮曞彂鐨�</li> <li>IOException,
	 *             鐢盽createMultipartEntity` 鎴�`UrlEncodedFormEntity` 寮曞彂鐨�</li>
	 *             <li>IOException鍜孋lientProtocolException,
	 *             鐢盽HttpClient.execute` 寮曞彂鐨�</li><br />
	 * 
	 *             2. 褰撳搷搴旂爜涓嶄负200鏃舵姤鍑虹殑鍚勭瀛愮被寮傚父: <li>HttpRequestException,
	 *             閫氬父鍙戠敓鍦ㄨ姹傜殑閿欒,濡傝姹傞敊璇簡 缃戝潃瀵艰嚧404绛� 鎶涘嚭姝ゅ紓甯� 棣栧厛妫�煡request log,
	 *             纭涓嶆槸浜轰负閿欒瀵艰嚧璇锋眰澶辫触</li> <li>HttpAuthException, 閫氬父鍙戠敓鍦ˋuth澶辫触,
	 *             妫�煡鐢ㄤ簬楠岃瘉鐧诲綍鐨勭敤鎴峰悕/瀵嗙爜/KEY绛�/li> <li>HttpRefusedException,
	 *             閫氬父鍙戠敓鍦ㄦ湇鍔″櫒鎺ュ彈鍒拌姹� 浣嗘嫆缁濊姹� 鍙槸澶氱鍘熷洜, 鍏蜂綋鍘熷洜 鏈嶅姟鍣ㄤ細杩斿洖鎷掔粷鐞嗙敱,
	 *             璋冪敤HttpRefusedException#getError#getMessage鏌ョ湅</li> <li>
	 *             HttpServerException, 閫氬父鍙戠敓鍦ㄦ湇鍔″櫒鍙戠敓閿欒鏃� 妫�煡鏈嶅姟鍣ㄧ鏄惁鍦ㄦ甯告彁渚涙湇鍔�/li> <li>
	 *             HttpException, 鍏朵粬鏈煡閿欒.</li>
	 */
	public Response httpRequest(String url,
			ArrayList<BasicNameValuePair> postParams, File file,
			boolean authenticated, String httpMethod) throws HttpException {
		Log.d(TAG, "Sending " + httpMethod + " request to " + url);
		//if (TwitterApplication.DEBUG) {
		//	DebugTimer.betweenStart("HTTP");
		//}

		URI uri = createURI(url);

		HttpResponse response = null;
		Response res = null;
		HttpUriRequest method = null;

		// Create POST, GET or DELETE METHOD
		method = createMethod(httpMethod, uri, file, postParams);
		// Setup ConnectionParams, Request Headers
		SetupHTTPConnectionParams(method,httpMethod);

		if(Constants.cookieStore!=null){
			mClient.setCookieStore(Constants.cookieStore);
		}
		
		// Execute Request
		try {
			response = mClient.execute(method, localcontext);
			res = new Response(response);
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new HttpException(e.getMessage(), e);
		} catch (IOException ioe) {
			throw new HttpException(ioe.getMessage(), ioe);
		}
		//

		//mClient
		//localcontext.get
		//if(mClient.getCookieStore().getCookies()!=null)
		Constants.cookieStore = mClient.getCookieStore();

		//
		if (response != null) {
			int statusCode = response.getStatusLine().getStatusCode();
			// It will throw a weiboException while status code is not 200
			HandleResponseStatusCode(statusCode, res);
			Log.d(TAG, "response status:"+statusCode);
		} else {
			Log.e(TAG, "response is null");
		}

		//if (TwitterApplication.DEBUG) {
		//	DebugTimer.betweenEnd("HTTP");
		//}
		return res;
	}

	/**
	 * CreateURI from URL string
	 * 
	 * @param url
	 * @return request URI
	 * @throws HttpException
	 *             Cause by URISyntaxException
	 */
	private URI createURI(String url) throws HttpException {
		URI uri;

		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new HttpException("Invalid URL.");
		}

		return uri;
	}

	/**
	 * 鍒涘缓鍙甫涓�釜File鐨凪ultipartEntity
	 * 
	 * @param filename
	 *            鏂囦欢鍚�	 * @param file
	 *            鏂囦欢
	 * @param postParams
	 *            鍏朵粬POST鍙傛暟
	 * @return 甯︽枃浠跺拰鍏朵粬鍙傛暟鐨凟ntity
	 * @throws UnsupportedEncodingException
	 */
	private MultipartEntity createMultipartEntity(String filename, File file,
			ArrayList<BasicNameValuePair> postParams)
			throws UnsupportedEncodingException {
		MultipartEntity entity = new MultipartEntity();
		// Don't try this. Server does not appear to support chunking.
		// entity.addPart("media", new InputStreamBody(imageStream, "media"));

		entity.addPart(filename, new FileBody(file));
		for (BasicNameValuePair param : postParams) {
			entity.addPart(param.getName(), new StringBody(param.getValue()));
		}
		return entity;
	}

	/**
	 * Setup HTTPConncetionParams
	 * 
	 * @param method
	 */
	int i=0;
	private void SetupHTTPConnectionParams(HttpUriRequest method,String httpMethod) {
		HttpConnectionParams.setConnectionTimeout(method.getParams(),
				CONNECTION_TIMEOUT_MS);
		HttpConnectionParams
				.setSoTimeout(method.getParams(), SOCKET_TIMEOUT_MS);
		mClient.setHttpRequestRetryHandler(requestRetryHandler);
		//method.addHeader("Accept-Encoding", "gzip, deflate");
		//method.addHeader("Accept-Charset", "UTF-8,*;q=0.5");
		///
		method.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;application/json,q=0.9,*/*;q=0.8");
		method.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
		if(httpMethod.equalsIgnoreCase(HttpPost.METHOD_NAME)){
			i++;
			method.addHeader("X-Requested-With","XMLHttpRequest");
			method.addHeader("Content-Type", "application/x-www-form-urlencoded");
			method.addHeader("Referer","https://dynamic.12306.cn/otsweb/passengerAction.do?method=initUsualPassenger12306");
			if(i%2!=0)return;
			//method.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;application/json,q=0.9,*/*;q=0.8");
			method.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
			method.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
			method.addHeader("Accept-Encoding", "gzip,deflate,sdch");
			method.addHeader("Connection","keep-alive");
			//method.addHeader("Cookie", "JSESSIONID=CF87978E31A2002522DAE9D58035D512; BIGipServerotsweb=2329149706.62495.0000");
			//method.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
			///
			//method.addHeader("Cache-Control", "max-age=0");
			//method.addHeader("Content-Length", "155");
			//method.addHeader("Content-Type", "application/x-www-form-urlencoded");
			//method.addHeader("Host","dynamic.12306.cn");
			//method.addHeader("Origin","https://dynamic.12306.cn");
			//method.addHeader("Referer","https://dynamic.12306.cn/otsweb/passengerAction.do?method=initUsualPassenger12306");
		}
	}

	/**
	 * Create request method, such as POST, GET, DELETE
	 * 
	 * @param httpMethod
	 *            "GET","POST","DELETE"
	 * @param uri
	 *            璇锋眰鐨刄RI
	 * @param file
	 *            鍙负null
	 * @param postParams
	 *            POST鍙傛暟
	 * @return httpMethod Request implementations for the various HTTP methods
	 *         like GET and POST.
	 * @throws HttpException
	 *             createMultipartEntity 鎴�UrlEncodedFormEntity寮曞彂鐨処OException
	 */
	private HttpUriRequest createMethod(String httpMethod, URI uri, File file,
			ArrayList<BasicNameValuePair> postParams) throws HttpException {

		HttpUriRequest method;

		if (httpMethod.equalsIgnoreCase(HttpPost.METHOD_NAME)) {
			// POST METHOD

			HttpPost post = new HttpPost(uri);
			// See this:
			// http://groups.google.com/group/twitter-development-talk/browse_thread/thread/e178b1d3d63d8e3b
			post.getParams().setBooleanParameter(
					"http.protocol.expect-continue", false);

			try {
				HttpEntity entity = null;
				if (null != file) {
					entity = createMultipartEntity("photo", file, postParams);
					post.setEntity(entity);
				} else if (null != postParams) {
					entity = new UrlEncodedFormEntity(postParams, HTTP.UTF_8);
					/**
					System.out.println("wkey--------------------------");
					BufferedInputStream bis = null;
				
					bis = new BufferedInputStream(entity.getContent());

					int i = -1;
					byte[] b = new byte[1024];
					StringBuffer sb = new StringBuffer();
					while ((i = bis.read(b)) != -1) {
					    System.out.println(new String(b));
					}
					System.out.println("wkey--------------------------");
					*////
				}
				post.setEntity(entity);
			} catch (IOException ioe) {
				throw new HttpException(ioe.getMessage(), ioe);
			}

			method = post;
		} else if (httpMethod.equalsIgnoreCase(HttpDelete.METHOD_NAME)) {
			method = new HttpDelete(uri);
		} else {
			method = new HttpGet(uri);
		}

		return method;
	}

	/**
	 * 瑙ｆ瀽HTTP閿欒鐮�	 * 
	 * @param statusCode
	 * @return
	 */
	private static String getCause(int statusCode) {
		String cause = null;
		switch (statusCode) {
		case NOT_MODIFIED:
			break;
		case BAD_REQUEST:
			cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
			break;
		case NOT_AUTHORIZED:
			cause = "Authentication credentials were missing or incorrect.";
			break;
		case FORBIDDEN:
			cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
			break;
		case NOT_FOUND:
			cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
			break;
		case NOT_ACCEPTABLE:
			cause = "Returned by the Search API when an invalid format is specified in the request.";
			break;
		case INTERNAL_SERVER_ERROR:
			cause = "Something is broken.  Please post to the group so the Weibo team can investigate.";
			break;
		case BAD_GATEWAY:
			cause = "Weibo is down or being upgraded.";
			break;
		case SERVICE_UNAVAILABLE:
			cause = "Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
			break;
		default:
			cause = "";
		}
		return statusCode + ":" + cause;
	}

	public boolean isAuthenticationEnabled() {
		return isAuthenticationEnabled;
	}

	public static void log(String msg) {
		if (DEBUG) {
			Log.d(TAG, msg);
		}
	}

	/**
	 * Handle Status code
	 * 
	 * @param statusCode
	 *            鍝嶅簲鐨勭姸鎬佺爜
	 * @param res
	 *            鏈嶅姟鍣ㄥ搷搴�	 * @throws HttpException
	 *             褰撳搷搴旂爜涓嶄负200鏃堕兘浼氭姤鍑烘寮傚父:<br />
	 *             <li>HttpRequestException, 閫氬父鍙戠敓鍦ㄨ姹傜殑閿欒,濡傝姹傞敊璇簡 缃戝潃瀵艰嚧404绛� 鎶涘嚭姝ゅ紓甯�
	 *             棣栧厛妫�煡request log, 纭涓嶆槸浜轰负閿欒瀵艰嚧璇锋眰澶辫触</li> <li>HttpAuthException,
	 *             閫氬父鍙戠敓鍦ˋuth澶辫触, 妫�煡鐢ㄤ簬楠岃瘉鐧诲綍鐨勭敤鎴峰悕/瀵嗙爜/KEY绛�/li> <li>
	 *             HttpRefusedException, 閫氬父鍙戠敓鍦ㄦ湇鍔″櫒鎺ュ彈鍒拌姹� 浣嗘嫆缁濊姹� 鍙槸澶氱鍘熷洜, 鍏蜂綋鍘熷洜
	 *             鏈嶅姟鍣ㄤ細杩斿洖鎷掔粷鐞嗙敱, 璋冪敤HttpRefusedException#getError#getMessage鏌ョ湅</li>
	 *             <li>HttpServerException, 閫氬父鍙戠敓鍦ㄦ湇鍔″櫒鍙戠敓閿欒鏃� 妫�煡鏈嶅姟鍣ㄧ鏄惁鍦ㄦ甯告彁渚涙湇鍔�/li>
	 *             <li>HttpException, 鍏朵粬鏈煡閿欒.</li>
	 */
	private void HandleResponseStatusCode(int statusCode, Response res)
			throws HttpException {
		String msg = getCause(statusCode) + "\n";
		RefuseError error = null;

		switch (statusCode) {
		// It's OK, do nothing
		case OK:
			break;

		// Mine mistake, Check the Log
		case NOT_MODIFIED:
		case BAD_REQUEST:
		case NOT_FOUND:
		case NOT_ACCEPTABLE:
			throw new HttpException(msg + res.asString(), statusCode);

			// UserName/Password incorrect
		case NOT_AUTHORIZED:
			throw new HttpAuthException(msg + res.asString(), statusCode);

			// Server will return a error message, use
			// HttpRefusedException#getError() to see.
		case FORBIDDEN:
			throw new HttpRefusedException(msg, statusCode);

			// Something wrong with server
		case INTERNAL_SERVER_ERROR:
		case BAD_GATEWAY:
		case SERVICE_UNAVAILABLE:
			throw new HttpServerException(msg, statusCode);

			// Others
		default:
			throw new HttpException(msg + res.asString(), statusCode);
		}
	}

	public static String encode(String value) throws HttpException {
		try {
			return URLEncoder.encode(value, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e_e) {
			throw new HttpException(e_e.getMessage(), e_e);
		}
	}

	public static String encodeParameters(ArrayList<BasicNameValuePair> params)
			throws HttpException {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < params.size(); j++) {
			if (j != 0) {
				buf.append("&");
			}
			try {
				buf.append(URLEncoder.encode(params.get(j).getName(), "UTF-8"))
						.append("=")
						.append(URLEncoder.encode(params.get(j).getValue(),
								"UTF-8"));
			} catch (java.io.UnsupportedEncodingException neverHappen) {
				throw new HttpException(neverHappen.getMessage(), neverHappen);
			}
		}
		return buf.toString();
	}

	/**
	 * 寮傚父鑷姩鎭㈠澶勭悊, 浣跨敤HttpRequestRetryHandler鎺ュ彛瀹炵幇璇锋眰鐨勫紓甯告仮澶�	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		// 鑷畾涔夌殑鎭㈠绛栫暐
		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
			// 璁剧疆鎭㈠绛栫暐锛屽湪鍙戠敓寮傚父鏃跺�灏嗚嚜鍔ㄩ噸璇昇娆�			
			if (executionCount >= RETRIED_TIME) {
				// Do not retry if over max retry count
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				// Retry if the server dropped connection on us
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// Do not retry on SSL handshake exception
				return false;
			}
			HttpRequest request = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// Retry if the request is considered idempotent
				return true;
			}
			return false;
		}
	};

}
