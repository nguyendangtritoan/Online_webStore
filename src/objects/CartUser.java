package objects;

import java.io.Serializable;

public class CartUser implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8785236126419127282L;

	public CartUser(int id_user, String session, String fullname, String address, String items, String amounts, String price_unit) {
		super();
		this.setId_user(id_user);
		this.setSession(session);
		this.setFullname(fullname);
		this.setAddress(address);
		this.setItems(items);
		this.setAmounts(amounts);
		this.setPrice_unit(price_unit);
	}
	
	public CartUser() {
		
	}
	
	private int id_user;
	private String fullname;
	private String address;
	private String items;
	private String amounts;
	private String price_unit;
	private String session;
	
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public int getId_user() {
		return id_user;
	}
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public String getAmounts() {
		return amounts;
	}
	public void setAmounts(String amounts) {
		this.amounts = amounts;
	}
	public String getPrice_unit() {
		return price_unit;
	}
	public void setPrice_unit(String price_unit) {
		this.price_unit = price_unit;
	}
}
