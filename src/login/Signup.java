package login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.DBHandler;

public class Signup extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        //response.sendRedirect("index.html");
    	PrintWriter out = response.getWriter();
  
    	response.setContentType("text/html");
    	
    	String errorString = request.getParameter("error");

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
    			"p {\r\n"+
    			" text-align: center;\r\n "+
    			" color: red;\r\n" +
    			"}\r\n"+
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
    			"<h1>Sign up page</h1>"+
    			"<p>" + (request.getParameter("error") == null ? "" : request.getParameter("error")) + "</p>"+
    			"	<form class=\"form-style\" action=\"sign_up\" method=\"post\">\r\n" + 
    			"		<table>\r\n" + 
    			"			<tr>\r\n" + 
    			"				<th>User name</th>\r\n" + 
    			"				<th><input type=\"text\" name=\"username\" placeholder=\"Your name\" required='required'></th>\r\n" + 
    			"			</tr>\r\n" + 
    			"			<tr>\r\n" + 
    			"				<th>Passowrd: </th>\r\n" + 
    			"				<th><input type=\"password\" name=\"password\" placeholder=\"Password\" required='required'></th>\r\n" + 
    			"			</tr>\r\n" + 
    			"			<tr>\r\n" + 
    			"				<th><input type=\"submit\" value=\"Submit\"></th>\r\n" + 
    			"			</tr>\r\n" + 
    			"		</table>\r\n" + 
    			"	</form>\r\n" +  
				"	<form class=\"form-style\" action=\"index.html\" method=\"get\">\r\n" + 
				"  		<input type=\"submit\" value=\"Sign in\">\r\n" + 
				"	</form> "+
    			"</body>\r\n" + 
    			"</html>");


    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	String isSuccess = "";
    	System.out.println(username+ " " + password);
    	try {
			DBHandler dbHandler = new DBHandler();
			isSuccess = dbHandler.createUserAccount(username, password);
			if(isSuccess.equalsIgnoreCase("exist")) {
				response.sendRedirect("sign_up?error=User exist, please choose another username");
			}
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head>"
        		+ "<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">"
        		+ "<title>Online Shop</title><style>th { text-align:left;}</style></head><body>");
        out.println("<p>"+isSuccess+"</p>");
        out.println("<p style='text-align: center;'><a href='http://localhost:8080/Example8_1/'>Login</a></p>");
        out.println("</body></html>");
        out.close();
	}
}
