package qetaa.service.location.helpers;

public final class AppConstants {
	private static final String CUSTOMER_SERVICE = "http://localhost:8080/service-qetaa-customer/rest/";
	private static final String USER_SERVICE = "http://localhost:8080/service-qetaa-user/rest/";
	private static final String CART_REPORT_SERVICE = "http://localhost:8080/service-qetaa-cart/rest/report/";
	private static final String VENDOR_SERVICE = "http://localhost:8080/service-qetaa-vendor/rest/";
	
	public static final String CUSTOMER_MATCH_TOKEN = CUSTOMER_SERVICE + "match-token";
	public static final String USER_MATCH_TOKEN = USER_SERVICE + "match-token";
	public static final String VENDOR_MATCH_TOKEN = VENDOR_SERVICE + "match-token";
	
	public static final String GET_CITY_ORDERS_COUNT = CART_REPORT_SERVICE + "cart-cities";
	
	
	public static final String getCityOrdersCount(long from, long to, int makeId, boolean archived, boolean ordered) {
		return CART_REPORT_SERVICE + "cart-cities/from/"+from+"/to/"+to+"/make/"+makeId+"/archived/"+archived+"/ordered/"+ ordered;
	}
	
}
