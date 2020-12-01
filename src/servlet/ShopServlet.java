//This is the content of servlet/ShopServlet.java file.
package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.DBHandler;
import utility.Helper;
public class ShopServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String productHtmlList;
    private String amountHtmlList;
    private static Map<String, Double> defaultPriceMap;

    public ShopServlet() {
        super();
    }
    public void init() {
        
       productHtmlList=Helper.getProductHtmlList();
   	   defaultPriceMap = Helper.initPriceMap();
        
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        Cookie cookie = null;
		Cookie[] cookies = null;
		// Get an array of Cookies associated with this domain
		cookies = request.getCookies();
		// Set response content type
		response.setContentType("text/html");
		String title = Helper.getReadCookieTitle();
		if (cookies != null) {
			out.println("<h2>" + Helper.getFoundCookiesTitle() + "</h2>");
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				out.print("Name : " + cookie.getName() + ",  ");
				out.print("Value: " + cookie.getValue() + " <br/>");
			}
		} else {
			out.println("<h2>" + Helper.getNoCookieFeedback() + "</h2>");
		}
        
        // Here we get access to the current session
        HttpSession session = request.getSession();
        System.out.println("Is user login: "+session.getAttribute("customer"));
        if(session.getAttribute("isUserLogined") != null) {
	        Object customer = null;
	        if (session != null) {
	            customer = session.getAttribute("customer");
	            System.out.println(customer);
	        }
	        if(session.getAttribute("objects.ShoppingCart") == null) {
	        	
	        	objects.ShoppingCart shoppingCart = new objects.ShoppingCart((String) session.getAttribute("customer"));
	        	String products = "";
	        	String amounts = "";
	        	String price_unit = Helper.getUnitPrice();
	        	for (int i = 0; i < cookies.length; i++) {
					cookie = cookies[i];
					if(cookie.getName().equals("productCartCookies")) {
						products = cookie.getValue();
					}
					if(cookie.getName().equals("amountCartCookies")) {
						amounts = cookie.getValue();
					}
				}
	        	shoppingCart.initShoppingCartOnCookies(products, amounts, price_unit);
	        	session.setAttribute("objects.ShoppingCart", shoppingCart);
	        }
	        out.println("<!DOCTYPE html><html><head>"
	        		+"<link rel=\"stylesheet\" href=\"mystyle.css\"><link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">"
	        		+ "<title>Online Shop</title><style>th { text-align:left;}</style></head><body><h1 style='text-align: center;'>Hello "+ (customer == null ? "" : customer.toString()) +"</h1>");
	        out.println("<p style='text-align: center;'>" + (request.getParameter("info") == null ? "" : request.getParameter("info")) + "</p>");
	        out.println("	<form style='text-align: center;' class=\"form-style\" action=\"checkout\" method=\"get\">\r\n" + 
					"  		<input type=\"submit\" name='submit' value=\"Cart Info\">\r\n" + 
					"	</form> ");
	        out.println("<form style='text-align: center;' method='post' action='handle_shopping_cart'>" +
	                "<input type='text' name='customer' value='"
	                + (customer == null ? "" : customer));
	        out.println("' required='required' readonly></td></tr>");
	        out.println("<div class=\"section\">");
	        out.println(productHtmlList);
	        out.println("</div>");
	        out.println(
	                "<p style='text-align:center'><input style='border: none; background: none; display: inline; color: blue; text-decoration: underline;' type='submit' name='submit' value='Visit cart'></p>");
	        out.println(
	                "<p style='text-align:center'><input style='border: none; background: none; display: inline; color: blue; text-decoration: underline;' type='submit' name='submit' value='Empty Cart'></p>");
	        out.println("</form>");
	        out.println("<hr/>");   
	        out.println("</form>");
	        out.println("	<form style='text-align:center' class=\"form-style\" action=\"login\" method=\"post\">\r\n" + 
					"  		<input type=\"submit\" name='submit' value=\"Log out\">\r\n" + 
					"	</form> ");
	        out.println("	<form style='text-align:center' class=\"form-style\" action=\"delete_cookies\" method=\"post\">\r\n" + 
					"  		<input type=\"submit\" name='submit' value=\"Delete Cookies\">\r\n" + 
					"	</form> ");
        }else {
        	out.println("<p style='color:red;text-align: center;'>Please login</p>");
        	out.println("<p style='text-align: center;'><a href='http://localhost:8080/Example8_1/'>Login</a></p>");
        }
        out.println("</body></html>");
        out.close();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}