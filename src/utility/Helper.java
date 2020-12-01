 //This is the content of utility.Helper.java file:

package utility;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
public class Helper {
    
    
    private static ResourceBundle resourceBundle;
    private static String[] productList, amountList, priceList;
    private static String productHtmlList="";
    private static String amountHtmlList="";
    //private static Map<String, Double> defaultPriceMap;
    
    public static Map<String, Double> initPriceMap() {
    	Map<String, Double> defaultPriceMap = new HashMap<String, Double>();
    	resourceBundle = ResourceBundle.getBundle("utility.settings", new Locale(""));
        priceList=resourceBundle.getString("price_list").split(";");
        resourceBundle = ResourceBundle.getBundle("utility.settings", new Locale(""));
        productList=resourceBundle.getString("product_list").split(";");
        
        for(int i=0; i<priceList.length;i++) {
        	defaultPriceMap.put(productList[i],Double.parseDouble(priceList[i]));
        }
        
        return defaultPriceMap;
    }
    
    public static String getUnitPrice() {
    	resourceBundle = ResourceBundle.getBundle("utility.settings", new Locale(""));

        
        return resourceBundle.getString("price_list");
    }
    
    public static String getProductHtmlList() {
        
         resourceBundle = ResourceBundle.getBundle("utility.settings", new Locale(""));
        
        //In the following we build HTML lists for products
        productList=resourceBundle.getString("product_list").split(";");
    
        
        for(String product : productList) {
            productHtmlList += "<div class=\"product\">";
            productHtmlList +=  "<input type='text' name='product' value='"+product+"' readonly>";
            productHtmlList += getAmounttHtmlList(product);
            productHtmlList += "<input type='submit' name='submit' value='Add "+ product +"' class=\"element\">";
            productHtmlList += "</div><br>";
        }
          
        
          return productHtmlList;
    }
    
    
    public static String getAmounttHtmlList(String product) {
        
          resourceBundle = ResourceBundle.getBundle("utility.settings", new Locale(""));
          
           //In the following we build HTML lists for amounts
          amountList=resourceBundle.getString("amount_list").split(";");
          amountHtmlList="<select name='"+product+"amount' class=\"element\">";
          for(String amount : amountList)
                amountHtmlList += "<option value='" + amount + "'>" + amount;
          amountHtmlList +="</select>";
          
          return amountHtmlList;
    }
    
    public static SimpleDateFormat getDateFormat() {
         resourceBundle = ResourceBundle.getBundle("utility.settings", new Locale(""));
         String dateTimePattern=resourceBundle.getString("date_time_pattern").trim();
    
         return new SimpleDateFormat(dateTimePattern);
    }
    
    //Cookies helper
    
    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy hh.mm.ss");
    // Here we define invalid cookie data pattern: "[\\s\\[\\]()=\\\",/?@:;\\\\ ]";
    private final static String invalidCookieDataPattern = "[\\s\\[\\]()=\\\",/?@:;\\\\]";
    // Here we define invalid data pattern: "(\\s)|([nN][uU][lL][lL])";
    private final static String invalidDataPattern ="(\\s)|([nN][uU][lL][lL])";
    // Here we define cookie age of 8 hours
    private static final int cookieAge = 8 * 60 * 60;
    private static final String cookieName = "user_info";
    private static final String feedback = "Empty fields!";
    private static final String noCookieFoundFeedback="No cookies found!";
    private static String foundCookiesTitle="Found Cookies Name and Value";
    private static String readCookieTitle="Reading Cookies";
    public static String stripCookieValue(String data) {
        return (data == null ? "" : data.replaceAll(invalidCookieDataPattern, ""));
    }
    public static String stripData(String data) {
        return (data == null ? "" : data.replaceAll(invalidDataPattern, ""));
    }
    public static SimpleDateFormat getDateFormatV2() {
        return simpleDateFormat;
    }
    public static String getCookieName() {
        return cookieName;
    }
    
    public static String getFeedback() {
        return feedback;
    }
    public static int getCookieAge() {
        return cookieAge;
    }
    public static String getNoCookieFeedback() {
        return noCookieFoundFeedback;
    }
    public static String getFoundCookiesTitle() {
        return foundCookiesTitle;
    }
    public static String getReadCookieTitle() {
         return readCookieTitle;
    }
    
}
