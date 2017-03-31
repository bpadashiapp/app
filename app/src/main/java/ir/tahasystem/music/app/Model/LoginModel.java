package ir.tahasystem.music.app.Model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ir.tahasystem.music.app.Values;

public class LoginModel implements Serializable {

	static final long serialVersionUID =8996890340799609057L;

	//uid,name,family,mobile,email,loc,sex,birthd,telj,telh,addj,addh

	public String status;
	public String uid;
	public String username;
	public String password;
	public String name="";
	public String family="";
	public String mobile;
	public String email;
	public String role;
	public String sex;
	public String loc;
	public String birthd;
	public String telj;
	public String telh;
	public String addj;
	public String addh;

	public boolean hasRole;
	
	
	public String birthdate;
	public String address_home;
	public String tel_home;
	public String address_job;
	public String tel_job;

	public String avatar;
	

	public String phone;
	public String address;
	public String address2;
	public String  user;
	public String  pass;
	public boolean hasAccess;
	public boolean fullAccess;
	public int accessType;
	public boolean showPrice;

	public String getName(){
		try {
			return URLEncoder.encode(name, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getPassword(){
		try {
			return URLEncoder.encode(password, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getBirth() {
		
		if(birthdate!=null)
			birthdate.replace("-", "/");
		
		
		return null;
	}

	public boolean hasAccess() {

		if(Values.appId!=0)
			return true;

		if(hasRole)
			return  hasRole;

		return hasAccess;
	}
}
