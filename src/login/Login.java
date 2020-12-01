package login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.DBHandler;
import servlet.ShopServlet;
import utility.Helper;

public class Login extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5777030616914939743L;
	/**
	 * 
	 */
	private String cookieValue;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        //response.sendRedirect("index.html");
    	PrintWriter out = response.getWriter();
  
    	response.setContentType("text/html");
    	
    	//Invalidate session when user go to this page
    	HttpSession session = request.getSession();
   		if(!session.isNew()) {
   			session.invalidate();
   		}

    	out.print("<!-- This is the content of index.html file -->\r\n" + 
    			"<!Doctype html>\r\n" + 
    			"<html>\r\n" + 
    			"<head>\r\n" + 
    			"<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">"+
    			"<style type=\"text/css\">\r\n" + 
    			"th, td {\r\n" + 
    			"	background-color:  #afcdcd;\r\n" + 
    			"	color: #d97077;\r\n" + 
    			"	border: 2px solid white;\r\n" + 
    			"	text-align: left;\r\n" + 
    			"}\r\n" + 
    			"h1 {\r\n"+
    			" text-align: center;\r\n "+
    			"}\r\n"+
    			"p {\r\n"+
    			" text-align: center;\r\n "+
    			" color: red;\r\n" +
    			"}\r\n"+
    			".form-style {\r\n" + 
    			"	font-weight: bold;\r\n" + 
    			"	font-style: italic;\r\n" + 
    			"	border-bottom: 2px solid #ddd;\r\n" + 
    			"	margin-bottom: 20px;\r\n" + 
    			"	font-size: 15px;\r\n" + 
    			"	padding-bottom: 3px;\r\n" + 
    			"	display: flex;\r\n" + 
    			"  	flex-direction: column;\r\n" + 
    			"  	align-items: center;\r\n" + 
    			"  	text-align: center;\r\n" + 
    			"}\r\n" + 
    			".form-style-2 input[type=submit],\r\n" + 
    			".form-style-2 input[type=button]{\r\n" + 
    			"	border: none;\r\n" + 
    			"	padding: 8px 15px 8px 15px;\r\n" + 
    			"	background: #FF8500;\r\n" + 
    			"	color: #fff;\r\n" + 
    			"	box-shadow: 1px 1px 4px #DADADA;\r\n" + 
    			"	-moz-box-shadow: 1px 1px 4px #DADADA;\r\n" + 
    			"	-webkit-box-shadow: 1px 1px 4px #DADADA;\r\n" + 
    			"	border-radius: 3px;\r\n" + 
    			"	-webkit-border-radius: 3px;\r\n" + 
    			"	-moz-border-radius: 3px;\r\n" + 
    			"}\r\n" + 
    			".form-style input[type=submit]:hover,\r\n" + 
    			".form-style input[type=button]:hover{\r\n" + 
    			"	background: #EA7B00;\r\n" + 
    			"	color: #fff;\r\n" + 
    			"}\r\n" + 
    			"</style>\r\n" + 
    			"</head>\r\n" + 
    			"<body>\r\n" + 
    			"<h1>Sign in page</h1>\r\n"+
    			"<p>" + (request.getParameter("error") == null ? "" : request.getParameter("error")) + "</p>"+
    			"	<form class=\"form-style\" action=\"login\" method=\"post\"> \r\n" + 
    			"		<table>\r\n" + 
    			"			<tr>\r\n" + 
    			"				<th>User name</th>\r\n" + 
    			"				<th><input type=\"text\" name=\"username\" placeholder=\"Your name\" required='required'></th>\r\n" + 
    			"			</tr>\r\n" + 
    			"			<tr>\r\n" + 
    			"				<th>Passowrd: </th>\r\n" + 
    			"				<th><input type=\"password\" name='password' placeholder=\"Password\" required='required'></th>\r\n" + 
    			"			</tr>\r\n" + 
    			"			<tr>\r\n" + 
    			"				<th><input type=\"submit\" name='submit' value=\"Sign in\"></th>\r\n" + 
    			"			</tr>\r\n" + 
    			"		</table>\r\n" + 
    			"	</form>\r\n" +  
				"	<form class=\"form-style\" action=\"sign_up\" method=\"get\">\r\n" + 
				"  		<input type=\"submit\" value=\"Sign up\">\r\n" + 
				"	</form> "+
    			"</body>\r\n" + 
    			"</html>");


    }
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		String actionString = request.getParameter("submit");
		if(actionString.equalsIgnoreCase("Sign in")) {
			String name = request.getParameter("username");
			String password = request.getParameter("password");
	    	System.out.println("Login: "+name+ " "+password);
	    	String isSuccess = "";
	    	try {
				DBHandler dbHandler = new DBHandler();
				isSuccess = dbHandler.userLogin(name, password);
	    	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				isSuccess = "Something wrong with connection";
			}
	    	if(isSuccess.contains("ID")) {
	    		
	    		//Save data as session
	       		HttpSession session = request.getSession(true);
				System.out.println("set attribute");
	            session.setAttribute("sessionID", session.getId());
	            session.setAttribute("sessionCreateTionime", Helper.getDateFormat().format(new Date(session.getCreationTime())));
	            // session.setAttribute("sessionLastAccessTime", new
	            // Date(session.getLastAccessedTime()));
	            session.setAttribute("isUserLogined", true);
	            session.setAttribute("customer", name);
	            session.setAttribute("iduser", isSuccess.substring(2));
	            //session.setAttribute("objects.ShoppingCart", new objects.ShoppingCart(name));
	    		
	            //Save data as COOKIES
	    		String usernameStrip = Helper.stripData(name);
	            // Here we make sure that the length of passed parameters are not zero
	            Cookie[] cookies = null;
	            Cookie userCookie = null;
	            // Get an array of Cookies associated with this domain
	            cookies = request.getCookies();
	            boolean cookieFound = false;
	            if (cookies != null) {
	                for (Cookie c : cookies) {
	                    if (c.getName().equals(Helper.getCookieName())) {
	                    	System.out.println("Name: "+c.getName());
	                    	System.out.println("Value: "+c.getValue());
	                    	if(!c.getValue().equals(usernameStrip+ "_" + isSuccess)) {
	                    		break;
	                    	}	                    		
	                        cookieFound = true;
	                        break;
	                    }
	                }
	            }
	            
	            
	            if (!cookieFound) {
	            	System.out.println("No cookies found");
	                cookieValue = usernameStrip + "_" + isSuccess;
	                userCookie = new Cookie(Helper.getCookieName(), Helper.stripCookieValue(cookieValue));
	                // Here we set the expiry date for the cookie.
	                userCookie.setMaxAge(Helper.getCookieAge());
	                // Add both the cookies in the response header.
	                response.addCookie(userCookie);
	                
	                Cookie productCartCookies = new Cookie("productCartCookies", "");
	                productCartCookies.setMaxAge(Helper.getCookieAge());
		            response.addCookie(productCartCookies);
		            
		            Cookie amountCartCookies = new Cookie("amountCartCookies", "");
	                amountCartCookies.setMaxAge(Helper.getCookieAge());
		            response.addCookie(amountCartCookies);
	            }//THE END
	            
	    		response.sendRedirect("shop_servlet");

	    	}else {
	    		response.sendRedirect("login?error="+isSuccess);
	    	}
    	}else { // this is for log out
    		HttpSession session = request.getSession();
    		session.setAttribute("isUserLogined", null); // make sure this attribute is null
    		session.invalidate(); // then invalidate 
    		response.sendRedirect("login?error=you have been logged out"); // redirect to login page
    	}
    }
}
