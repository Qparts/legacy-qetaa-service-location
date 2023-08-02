package qetaa.service.location.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name="loc_country")
@Entity
public class PublicCountry implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id")
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="name_ar")
	private String nameAr;
	@Column(name="latitude")
	private double latitude;
	@Column(name="longitude")	
	private double longitude;
	@Column(name="map_zoom")
	private int mapZoom;
	@Column(name="country_code")
	private String countryCode;
	@Column(name="mobile_regex")
	private String mobileRegex;
	@Transient
	private List<PublicRegion> regions;
	@JsonIgnore
	@Column(name="customer_status")
	private char customerStatus;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getMapZoom() {
		return mapZoom;
	}
	public void setMapZoom(int mapZoom) {
		this.mapZoom = mapZoom;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getMobileRegex() {
		return mobileRegex;
	}
	public void setMobileRegex(String mobileRegex) {
		this.mobileRegex = mobileRegex;
	}
	public List<PublicRegion> getRegions() {
		return regions;
	}
	public void setRegions(List<PublicRegion> regions) {
		this.regions = regions;
	}
	public String getNameAr() {
		return nameAr;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}
	public char getCustomerStatus() {
		return customerStatus;
	}
	public void setCustomerStatus(char customerStatus) {
		this.customerStatus = customerStatus;
	}
	
	
	
	
}
