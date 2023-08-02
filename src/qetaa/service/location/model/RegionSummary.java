package qetaa.service.location.model;

import java.util.ArrayList;
import java.util.List;

public class RegionSummary {
	private PublicRegion region;
	private Number count;
	private List<CitySummary> citySummaries;
	
	
	public RegionSummary() {
		citySummaries = new ArrayList<>();
	}
	
	public PublicRegion getRegion() {
		return region;
	}
	public void setRegion(PublicRegion region) {
		this.region = region;
	}
	public Number getCount() {
		return count;
	}
	public void setCount(Number count) {
		this.count = count;
	}
	public List<CitySummary> getCitySummaries() {
		return citySummaries;
	}
	public void setCitySummaries(List<CitySummary> citySummaries) {
		this.citySummaries = citySummaries;
	}
}
