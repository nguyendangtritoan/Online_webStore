//This is the content of logic.HandleShoppingCart.java file
package logic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.DBHandler;
import objects.CartUser;
import utility.Helper;
public class Checkout extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static Map<String, Double> defaultPriceMap;
    @Override
    public void init() {
    	defaultPriceMap = Helper.initPriceMap();
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	HttpSession session = request.getSession();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String customer;
        customer = (String) session.getAttribute("customer");
        out.println("<!DOCTYPE html><html><head>"
        		+"<link rel=\"stylesheet\" href=\"mystyle.css\"><link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">"
        		+ "<title>Online Shop</title><style>th { text-align:left;}</style></head><body><h1 style='text-align: center;'>Hello "+ (customer == null ? "" : customer.toString()) +"</h1>");
        
        if(session.getAttribute("isUserLogined") != null) {
    	DBHandler dbHandler;
		try {
			dbHandler = new DBHandler();
			int id = Integer.parseInt((String) session.getAttribute("iduser"));
			ArrayList<CartUser> cartUsers = new ArrayList<CartUser>();
			cartUsers = dbHandler.getUserCartData(id);
			out.println("<p style='text-align: center;'><a href='http://localhost:8080/Example8_1/shop_servlet'>Continue Shopping</a></p>");
			if(cartUsers.size() == 0) {
				out.println("<p style='color:blue;text-align: center;'>Sorry you haven't bought anything</p>");
			}else {
				
				out.println("<div>Session ID: "+ session.getAttribute("sessionID") +"</div>");
				out.println("<div>Creation time: "+ session.getAttribute("sessionCreateTionime") +"</div>");
				out.println("<div class='all'>");
				for (CartUser cartUser : cartUsers) {
					out.println("<div class='section'>Full name: "+ cartUser.getFullname());
					out.println("<div>Address: "+ cartUser.getAddress() +"</div>");
					
					String[] items = cartUser.getItems().split(";");
					String[] amounts = cartUser.getAmounts().split(";");
					String[] prices = cartUser.getPrice_unit().split(";");
					
					for (int i = 0; i < items.length; i++) {
						out.println("<div class='bill'>");
						out.println("<div class='combine'>Item: "+ items[i] +"</div>");
						out.println("<div class='combine'>Amount: "+ amounts[i] +"</div>");
						out.println("<div class='combine'>Price: "+ prices[i] +"</div>");
						out.println("</div>");

					}
					out.println("</div>");
				}
				out.println("</div>");
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
        else {
        	out.println("<p style='color:red;text-align: center;'>Please login</p>");
        	out.println("<p style='text-align: center;'><a href='http://localhost:8080/Example8_1/'>Login</a></p>");
        }
        out.println("</body></html>");

    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Here we get access to the current session
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
       	Object customer = null;
       	
        if (session != null) {
            customer = session.getAttribute("customer");
            System.out.println(customer);
        }
        
        if(session.getAttribute("isUserLogined") != null) {
	        String action = request.getParameter("submit");
	        if (action == null)
	            response.sendRedirect("index.html");
	        out.println("<!DOCTYPE html><html><head>"
	        		+"<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">"
	        		+ "<title>Online Shop</title><style>th { text-align:left;}</style></head><body><h1>Hello "+ (customer == null ? "" : customer.toString()) +"</h1>");
	        // Here we create a local variable which holds the shoppinCart
	        objects.ShoppingCart shoppingCart = null;
	        // Here we get access to the shopping cart
	        if (session != null)
	            shoppingCart = (objects.ShoppingCart) session.getAttribute("objects.ShoppingCart");
	        if (action.equalsIgnoreCase("Checkout")) {
	        	double payment = Double.parseDouble(request.getParameter("payment"));
	        	if(payment < 1.0) {
	        		response.sendRedirect("shop_servlet?info=Please buy something");
	        	}else {
	            out.println("<form method='post' action=\"checkout\">\r\n" + 
	            		"  <label for=\"fname\">Full name:</label>\r\n" + 
	            		"  <input type=\"text\" id=\"fname\" name=\"fname\"><br><br>\r\n" + 
	            		"  <label for=\"address\">Address:</label>  \r\n" + 
	            		"  <input type=\"text\" id=\"address\" name=\"address\"><br><br>\r\n" +
	            		"  <label for=\"total\">Total:</label>  \r\n" + 
	            		"  <input type=\"text\" id=\"total\" name='total' value='"+ request.getParameter("payment")+"' readonly><br><br>\r\n" +
	            		"  <input type=\"submit\" name=\"submit\" value=\"Submit\">\r\n" + 
	            		"</form>");
	        	}
	        }
	        if (action.equalsIgnoreCase("Submit")) {
	        	
	            out.println("Thank you for choosing our store");
	            out.println("	<form class=\"form-style\" action=\"checkout\" method=\"get\">\r\n" + 
						"  		<input type=\"submit\" name='submit' value=\"Cart Info\">\r\n" + 
						"	</form> ");
	            String fullname = request.getParameter("fname");
	            String address = request.getParameter("address");
	            double payment = Double.parseDouble(request.getParameter("total"));
	            
	            System.out.println("THIS IS PAYMENT: "+request.getParameter("total"));
	            try {
	            	shoppingCart = (objects.ShoppingCart) session.getAttribute("objects.ShoppingCart");
	            	int id = Integer.parseInt((String) session.getAttribute("iduser"));
	            	String items = shoppingCart.getStringOfItems();
	            	String amounts = shoppingCart.getStringOfAmounts();
	            	String price_unit = Helper.getUnitPrice();
	            	String sessString = "";
	            	
	            	Enumeration e = (Enumeration) (session.getAttributeNames());
	            	while(e.hasMoreElements()) {
	            		Object atb;
	            		if((atb = e.nextElement())!=null)
	            			sessString += atb+";";
	            		System.out.println(sessString);
	            	}
	            	
	            	CartUser cartUser = 
	            			new CartUser(id, sessString, fullname, address, items, amounts, price_unit);
					DBHandler dbHandler = new DBHandler();
					System.out.println(dbHandler.saveCheckOutUserData(cartUser));
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            Cookie productCartCookies = new Cookie("productCartCookies", "");
	            response.addCookie(productCartCookies);
	            
	            Cookie amountCartCookies = new Cookie("amountCartCookies", "");
	            response.addCookie(amountCartCookies);
	            
	            session.setAttribute("objects.ShoppingCart", null);
	        }
	       
	       
	        out.println("<p style='text-align: center;'><a href='http://localhost:8080/Example8_1/shop_servlet'>Continue Shopping</a></p>");
        }else {
        	out.println("<p style='color:red;text-align: center;'>Please login</p>");
        	out.println("<p style='text-align: center;'><a href='http://localhost:8080/Example8_1/'>Login</a></p>");
        }
        out.println("</body></html>");
        out.close();
    }
}