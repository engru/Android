/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
 * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.wkey.develop.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.wkey.develop.http.HttpException;
import com.wkey.develop.http.Response;

/**
 * 妯′豢JSONObject鐨刋ML瀹炵幇
 * 
 * @author jmx
 * 
 */
class XmlObject {
	private String str;

	public XmlObject(String s) {
		this.str = s;
	}

	// FIXME: 杩欓噷鐢ㄧ殑鏄竴涓笓鏈夌殑ugly瀹炵幇
	public String getString(String name) throws Exception {
		Pattern p = Pattern
				.compile(String.format("<%s>(.*?)</%s>", name, name));
		Matcher m = p.matcher(this.str);
		if (m.find()) {
			return m.group(1);
		} else {
			throw new Exception(String.format("<%s> value not found", name));
		}
	}

	@Override
	public String toString() {
		return this.str;
	}
}

/**
 * 鏈嶅姟鍣ㄥ搷搴旂殑閿欒淇℃伅
 */
public class RefuseError /*extends WeiboResponse */ implements java.io.Serializable {

	// TODO: get error type
	public static final int ERROR_A = 1;
	public static final int ERROR_B = 1;
	public static final int ERROR_C = 1;

	private int mErrorCode = -1;
	private String mRequestUrl = "";
	private String mResponseError = "";

	private static final long serialVersionUID = -2105422180879273058L;

	public RefuseError(Response res) throws HttpException {
		String error = res.asString();
		try {
			// 鍏堝皾璇曚綔涓簀son object杩涜澶勭悊
			JSONObject json = new JSONObject(error);
			init(json);
		} catch (Exception e1) {
			// 濡傛灉澶辫触锛屽垯浣滀负XML鍐嶈繘琛屽鐞�			
			try {
				XmlObject xml = new XmlObject(error);
				init(xml);
			} catch (Exception e2) {
				// 鍐嶅け璐ュ氨浣滀负鏅�瀛楃涓茶繘琛屽鐞嗭紝杩欎釜澶勭悊淇濊瘉涓嶄細鍑洪敊
				init(error);
			}
		}
	}

	public void init(JSONObject json) throws HttpException {
		try {
			mRequestUrl = json.getString("request");
			mResponseError = json.getString("error");
			parseError(mResponseError);
		} catch (JSONException je) {
			throw new HttpException(je.getMessage() + ":" + json.toString(), je);
		}
	}

	public void init(XmlObject xml) throws HttpException {
		try {
			mRequestUrl = xml.getString("request");
			mResponseError = xml.getString("error");
			parseError(mResponseError);
		} catch (Exception e) {
			throw new HttpException(e.getMessage() + ":" + xml.toString(), e);
		}
	}

	public void init(String error) {
		mRequestUrl = "";
		mResponseError = error;
		parseError(mResponseError);
	}

	private void parseError(String error) {
		if (error.equals("")) {
			mErrorCode = ERROR_A;
		}
	}

	public int getErrorCode() {
		return mErrorCode;
	}

	public String getRequestUrl() {
		return mRequestUrl;
	}

	public String getMessage() {
		return mResponseError;
	}
}