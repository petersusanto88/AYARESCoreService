package com.apns.utility;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JSON {
  
	public JSON() {
	}
	
	public static JSONObject newStatusMessage( JSONObject data, String status, String errMsg )
	{
		 data.put( "status", status );
		 data.put( "msg", errMsg );
		 return data;
	}
	
	public static JSONObject newJSONObject( String data )
	{
		try{
			return (JSONObject) JSONValue.parse( data );
		}catch (Exception e) {
			System.out.println( "<<error>> newJSONObject() "+e.getMessage() );
		}
		return new JSONObject();
	}
	
	public static JSONArray newJSONArray( String data )
	{
		try{
			return (JSONArray) JSONValue.parse( data );
		}catch (Exception e) {
			System.out.println( "<<error>> newJSONArray() "+e.getMessage() );
		}
		return new JSONArray();
	}
	
	public static int readStatus(JSONObject json ) {
		int i = -50;
		try {
			if (json.get("status").toString().length() > 0) {
				i = (int) Integer.parseInt( json.get("status").toString() );
			}
		} catch (Exception e) {
		}
		return i;
	}
	
	public static String get(JSONObject json, String key, String strDefault) {
		String str = "";
		try {
			if (json.get(key).toString().length() > 0) {
				str = json.get(key).toString();
			} else
				str = strDefault;
		} catch (Exception e) {
			System.out.println("<<<error>>> get() " + key);
		}
		;
		if (str == null)
			str = "";
		if (str == "null")
			str = "";
		return str;
	}

	public static int getInt(JSONObject json, String key, int intDefault) {
		int intDef = intDefault;
		try {
			if (json.get(key).toString().length() > 0) {
				intDef = (int) Integer.parseInt( json.get(key).toString() );
			}
		} catch (Exception e) {
			System.out.println("<<<error>>> get() " + key);
		}
		;
		return intDef;
	}

	public static  String get(JSONObject json, String key) {
		String str = "";
		try {
			str = json.get(key).toString();
		} catch (Exception e) { }
		
		if (str == null) 	str = "";
		if (str == "null")	str = "";
		return str;
	}

	public static  int getInt(JSONObject json, String key) {
		int i = -1;
		try {
			i = (int) Integer.parseInt( json.get(key).toString() );
		} catch (Exception e) {
			System.out.println("<<<error>>> getInt() " + key);
		}
		return i;
	}
	
	public static JSONObject getJSON( String str )
	{
		try{
			JSONObject js = (JSONObject) JSONValue.parse(str);
			return js;
		}catch( Exception e ){}
		return (JSONObject) JSONValue.parse("{}");
	}
	
	public static int ERROR_JSON_FORMAT = 1001;
	public static int ERROR_JSON_STATUS_NULL = 1002;
	public static JSONObject getJsonStatusMessage( String str, String errorTitle )
	{
		int stat = -1000;
		String errMessage = "";
		JSONObject js = null;
		try{
			js = (JSONObject) JSONValue.parse(str);
		}catch( Exception e )
		{
			js = null;
			stat = ERROR_JSON_FORMAT; //format json di str bikin error
			errMessage = errorTitle+" Invalid Format JSON";
		}
		
		try{
			if( js != null )
			{
				if( js.containsKey( "status" ) )
				{
					stat = Integer.parseInt(js.get("status").toString());
					errMessage = "";
				}
				else
				{
					stat = ERROR_JSON_STATUS_NULL; //format json di str bikin error
					errMessage = errorTitle+" Status: Null";
					
					if( js.containsKey( "message" ) )
						errMessage = js.get( "message" ).toString();
					else
					if( js.containsKey( "msg" ) )
						errMessage = js.get( "msg" ).toString();
				}
			}
			else
			{
				stat = ERROR_JSON_FORMAT; //format json di str bikin error
				errMessage = errorTitle+" Invalid Format JSON";
			}
		}catch( Exception e ){
			stat = ERROR_JSON_STATUS_NULL; //format json di str bikin error
			errMessage = errorTitle+" Status: Null";
		}
		
		if( errMessage.length() > 0 )
		{
			js = new JSONObject();
			js.put( "status", stat );
			js.put( "message", errMessage );
			js.put( "msg", errMessage );
		}
		else
		{
			if( js.containsKey( "msg" ) )
			{
				js.put( "message", js.get( "msg") );
			}
		}

		return js;

	}
	
	public static String getErrorJson( String status, String message )
	{
		JSONObject j = new JSONObject();
		j.put( "status", status );
		j.put( "msg", message );
		return j.toJSONString();
	}
	
//	\r\n\t
	
	public String replaceSlash( String source )
	{
		String[] pattern = { "\r\n\r\n\t", "\r\n\t", "\r\n", "\r", "\n", "\t" };
		for( int v =0; v<pattern.length; v++ )
		{
			if( v == 0 )
				source = replaceAll( source, pattern[v], " " );
			else
				source = replaceAll( source, pattern[v], "" );
		}
		return source;
	}
	public String replaceAll(String source, String pattern, String replacement) 
	{
        if (source == null) {
            return "";
        }
       
        StringBuffer sb = new StringBuffer();
        int idx = -1;
        int patIdx = 0;

        while ((idx = source.indexOf(pattern, patIdx)) != -1) {
            sb.append(source.substring(patIdx, idx));
            sb.append(replacement);
            patIdx = idx + pattern.length();
        }
        sb.append(source.substring(patIdx));
        return sb.toString();

    }	
}