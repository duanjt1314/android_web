package duanjt.life.common;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;
import org.apache.http.protocol.HTTP;

import duanjt.life.model.Users;

import android.util.Log;

public class Response {
	private Boolean success;
	private Object data;
	private String msg;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Response(Boolean success, String msg, Object data) {
		super();
		this.success = success;
		this.data = data;
		this.msg = msg;
	}

	public Response() {
	}
	
	

}
