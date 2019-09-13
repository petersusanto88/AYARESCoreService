package com.webservicemaster.iirs.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class FormValidator {
	
	private boolean validateEmailFormat( String email ){
		
		String emailPattern		= "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern;
		Matcher matcher;
		
		pattern 				= Pattern.compile(emailPattern);
		matcher 				= pattern.matcher(email);
		
		return matcher.matches();
		
	}
	
	private boolean validatePasswordFormat( String password ){
		
		String passwordPattern	= "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
		Pattern pattern;
		Matcher matcher;
		
		pattern 				= Pattern.compile(passwordPattern);
		matcher 				= pattern.matcher(password);
		
		return matcher.matches();
		
	}
	
	private boolean validatePhoneNumber( String phoneNumber ){		
		
		String numberPattern	= "((?=.*\\d).{11,13})";
		Pattern pattern;
		Matcher matcher;
		
		pattern 				= Pattern.compile(numberPattern);
		matcher 				= pattern.matcher(phoneNumber);
		
		return matcher.matches();
		
	}
	
	public JSONArray createCustomerRegistrationValidation( String[] element, String[] value ){
		
		JSONArray ja			= new JSONArray();
		JSONArray jaVal			= null;
		JSONObject joData 		= null;
		
		for( int i = 0; i < element.length; i++ ){
			
			joData 					= new JSONObject();
			jaVal 					= new JSONArray();
			
			if( element[i].compareTo("email") == 0 ){
				jaVal.add("email");
			}else if( element[i].compareTo("password") == 0 ){
				jaVal.add("password");
			}else if( element[i].compareTo("phone number") == 0 ){
				jaVal.add("phone_number");
			}
			
			jaVal.add("required");
			joData.put("element",element[i]);
			joData.put("validation", jaVal);
			joData.put("value", value[i]);
			ja.add(joData);
			
		}	
		
		return ja;
		
	}
	
	public JSONObject validateElement( JSONArray jaElement ){
		
		JSONObject joResult 	= new JSONObject();
		JSONObject joElement 	= null;
		JSONArray jaValidation  = null;
		
		String element 			= "";
		String value 			= "";
		
		boolean flagValid 		= true;
		
		if( jaElement.size() > 0 ){
			
			for( int i = 0; i < jaElement.size(); i++ ){
				
				joElement 		= JSON.newJSONObject( jaElement.get(i).toString() );
				
				element 		= JSON.get(joElement, "element");
				jaValidation 	= JSON.newJSONArray( JSON.get(joElement, "validation") );
				value 			= JSON.get(joElement, "value");
				
				for( int j = 0; j < jaValidation.size(); j++ ){
					
					if( jaValidation.get(j).toString().compareTo("required") == 0 ){
						
						if( value.compareTo("") == 0 ){							
							joResult.put(element, "Please enter your " + element);
							flagValid = false;
							break;							
						}else{
							joResult.put(element, "OK");
						}
						
					}else if( jaValidation.get(j).toString().compareTo("email") == 0 ){
						
						if( !validateEmailFormat( value ) ){							
							joResult.put(element, "Please enter valid email ");
							flagValid = false;
							break;							
						}else{
							joResult.put(element, "OK");
						}
						
					}else if( jaValidation.get(j).toString().compareTo("password") == 0 ){
						
						if( !validatePasswordFormat( value ) ){							
							joResult.put(element, "Please enter valid password (Length must be 6-20, minimal 1 upper case, minimal 1 numeric) ");
							flagValid = false;
							break;							
						}else{
							joResult.put(element, "OK");
						}
						
					}else if( jaValidation.get(j).toString().compareTo("phone_number") == 0 ){
						
						if( !validatePhoneNumber( value ) ){							
							joResult.put(element, "Please enter valid phone number");
							flagValid = false;
							break;							
						}else{
							joResult.put(element, "OK");
						}
						
					}
					
				}
				
			}
			
			if( flagValid ){
				joResult.put("errCode", "00");
				joResult.put("errMsg", "OK");
			}else{
				joResult.put("errCode", "-99");
				joResult.put("errMsg", "Not Valid");
			}
			
		}else{
			
			joResult.put("errCode", "-99");
			
		}
		
		return joResult;
		
	}
	
	/*public static void main(String[] args){
		
		JSONArray ja			= new JSONArray();
		JSONArray jaVal			= null;
		JSONObject joData 		= null;
		
		joData 					= new JSONObject();
		jaVal 					= new JSONArray();
		jaVal.add("required");
		jaVal.add("");
		jaVal.add("");
		jaVal.add("");
		joData.put("element","full name");
		joData.put("validation", jaVal);
		joData.put("value", "Peter Susanto");
		ja.add(joData);
		
		joData 					= new JSONObject();
		jaVal 					= new JSONArray();
		jaVal.add("required");
		jaVal.add("email");
		jaVal.add("");
		jaVal.add("");
		joData.put("element","email");
		joData.put("validation", jaVal);
		joData.put("value", "rrpieter@gmail.com");
		ja.add(joData);
		
		joData 					= new JSONObject();
		jaVal 					= new JSONArray();
		jaVal.add("required");
		jaVal.add("password");
		joData.put("element","password");
		joData.put("validation", jaVal);
		joData.put("value", "P4ssw0rd123@@");
		ja.add(joData);
		
		System.out.println("Data : " + ja.toString());
		
		JSONObject joResult = validateElement(ja);
		
		System.out.println("Result : " + joResult.toString());
		
	}*/

}
