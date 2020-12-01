//This is the content of objects.ShoppingCart.java file
package objects;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServlet;
public class ShoppingCart extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Map<String, String> map;
    private Map<String, String> priceMap;
    private String customer;
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ShoppingCart(String customer) {
        if (customer == null)
            this.customer = "Anonymous";
        else
            this.customer = customer;
        map = Collections.synchronizedMap(new HashMap());
        priceMap = Collections.synchronizedMap(new HashMap());
    }
    public String getCustomer() {
        return this.customer;
    }
    
    public void put(String key, String value) {
        if (key == null || value == null)
            return;
        String currentValue;
        if ((currentValue = map.get(key)) != null) {
            Integer currentAmount = Integer.parseInt(currentValue) + Integer.parseInt(value);
            map.put(key, currentAmount + "");
        } else {
            map.put(key, value);
        }
    }
    
    public void updatePrice(String key, String addPrice) {
        if (key == null || addPrice == null)
            return;
        String currentAmount;
        if ((currentAmount = map.get(key)) != null) {
            Double totalPrice = Double.parseDouble(currentAmount)*Double.parseDouble(addPrice);
            priceMap.put(key, totalPrice+"");
        } else {
        	priceMap.put(key, addPrice);
        }
    }
    
    
    public double totalPayment() {
    	double total = 0.0;
    	synchronized (priceMap) {
    		Iterator<Entry<String, String>> iterator =priceMap.entrySet().iterator();
    		while (iterator.hasNext()) {
                Map.Entry<String, String> me = (Map.Entry<String, String>) iterator.next();
                total += Double.parseDouble(priceMap.get(me.getKey()));
    		}
		}
    	return total;
    }
    
    public String getStringOfItems() {
    	StringBuilder result = new StringBuilder();
    	synchronized (map) {
    		Iterator<Entry<String, String>> iterator =map.entrySet().iterator();
    		while (iterator.hasNext()) {
                Map.Entry<String, String> me = (Map.Entry<String, String>) iterator.next();
                result.append(me.getKey()+";");
    		}
		}
    	return result.toString();
    }
    
    public String getStringOfAmounts() {
    	StringBuilder result = new StringBuilder();
    	synchronized (map) {
    		Iterator<Entry<String, String>> iterator =map.entrySet().iterator();
    		while (iterator.hasNext()) {
                Map.Entry<String, String> me = (Map.Entry<String, String>) iterator.next();
                result.append(me.getValue()+";");
    		}
		}
    	return result.toString();
    }
    
    public void initShoppingCartOnCookies(String products, String amounts, String price_unit) {
    	String[] productArrayStrings = products.split("-");
    	String[] amountArrayStrings = amounts.split("-");
    	String[] priceStrings = price_unit.split(";");
    	
    	for (int i = 0; i < amountArrayStrings.length; i++) {
    		if(productArrayStrings[i].equals("")) break;
			map.put(productArrayStrings[i], amountArrayStrings[i]);
			priceMap.put(productArrayStrings[i], Double.parseDouble(priceStrings[i]) * Double.parseDouble(amountArrayStrings[i]) + "");
		}
    	
    	

    }
    
    public void remove(String key) {
        if (key == null)
            return;
        if (map.containsKey(key)) {
            map.remove(key);
            priceMap.remove(key);
        }
    }
    public void removeAll() {
        if (map != null)
            map.clear();
        	priceMap.clear();
    }
    public Map<String, String> getMap() {
        return map;
    }
    public String getValues() {
        StringBuffer content = new StringBuffer("<tr><th>Product</th><th>Amount</th><th>Update</th><th>Total price</th></tr>");
// Here we explicitly synchronise since we use an iterator
// This makes the the map thread safe so that only one thread a time
// can access the map
        synchronized (map) {
// Iterator i=keys.iterator();
            Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> me = (Map.Entry<String, String>) iterator.next();
                content.append(
                        "<tr><form method='post' action='handle_shopping_cart'><td><input type='text' name='product' value='"+ me.getKey() + "' readonly>"
                        		+ "</td><td><input type='text' name='amount' value='"+ me.getValue()+ "'>"
                                + "</td><td><input type='submit' name='submit' value='Update'>"
                        		+ "</td><td><input type='text' name='price' value='"+ priceMap.get(me.getKey())+ "'  readonly>"
                                + "</td><td><input type='submit' name='submit' value='Delete'></form></td></tr>");
            }
        } // end of synchronised
        return content.toString();
    }
    public int getSize() {
        return map.size();
    }
    @Override
    public String toString() {
        return getClass().getName() + "[" + map + "]";
    }
}