package qetaa.service.location.model;

import java.util.ArrayList;
import java.util.List;


public class CountrySummary {
	private PublicCountry country;
	private Number count;
	private List<RegionSummary> regionSummaries;
	
	public CountrySummary() {
		regionSummaries = new ArrayList<>();
	}
	
	public PublicCountry getCountry() {
		return country;
	}
	public void setCountry(PublicCountry country) {
		this.country = country;
	}
	public Number getCount() {
		return count;
	}
	public void setCount(Number count) {
		this.count = count;
	}
	public List<RegionSummary> getRegionSummaries() {
		return regionSummaries;
	}
	public void setRegionSummaries(List<RegionSummary> regionSummaries) {
		this.regionSummaries = regionSummaries;
	}
	
}
