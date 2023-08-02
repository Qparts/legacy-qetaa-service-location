package qetaa.service.location.model;

import java.util.ArrayList;
import java.util.List;

public class OrdersSummary {
	private List<CountrySummary> countrySummaries;
	
	public OrdersSummary() {
		countrySummaries = new ArrayList<>();
	}
	
	public List<CountrySummary> getCountrySummaries() {
		return countrySummaries;
	}

	public void setCountrySummaries(List<CountrySummary> countrySummaries) {
		this.countrySummaries = countrySummaries;
	}
	

}
