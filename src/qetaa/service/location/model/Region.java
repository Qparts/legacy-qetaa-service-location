package qetaa.service.location.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="loc_region")
public class Region implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "loc_region_id_seq_gen", sequenceName = "loc_region_id_seq", initialValue=1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loc_region_id_seq_gen")
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
	@JoinColumn(name="country_id")
	@ManyToOne
	private Country country;
	
	@Transient
	private List<City> cities;
	
	
	@Column(name="json_code")
	private String jsonCode;
	/*
	 ['sa-4293', 0],
    ['sa-tb', 1],
    ['sa-jz', 2],
    ['sa-nj', 3],
    ['sa-ri', 4],
    ['sa-md', 5],
    ['sa-ha', 6],
    ['sa-qs', 7],
    ['sa-hs', 8],
    ['sa-jf', 9],
    ['sa-sh', 10],
    ['sa-ba', 11],
    ['sa-as', 12],
    ['sa-mk', 13]
	
	
	*/
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
	public String getNameAr() {
		return nameAr;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
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
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public String getJsonCode() {
		return jsonCode;
	}
	public void setJsonCode(String jsonCode) {
		this.jsonCode = jsonCode;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	
}


