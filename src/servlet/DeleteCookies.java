//This is servlet/DeleteCookies.java file
package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utility.Helper;
public class DeleteCookies extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cookie = null;
        Cookie[] cookies = null;
        // Get an array of Cookies associated with this domain
        cookies = request.getCookies();
        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title = "Deleting Cookies";
        out.println("<!Doctype html><html><head><title>" + title + "</title></head><body>");
        if (cookies != null) {
            out.println("<h2> Cookies Name and Value</h2>");
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals(Helper.getCookieName()) || cookie.getName().equals("productCartCookies") || cookie.getName().equals("amountCartCookies")) {
                    out.println("Deleted cookie : " + cookie.getName() + "=" + cookie.getValue() + "<br/>");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                
            }
        } else {
            out.println("<h2>No cookies founds</h2>");
        }
        out.println("<hr/>");
        out.println("<p style='text-align: center;'><a href='http://localhost:8080/Example8_1/shop_servlet'>Continue Shopping</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.html");
    }
}