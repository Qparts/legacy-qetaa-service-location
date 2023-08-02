package qetaa.service.location.helpers;

import qetaa.service.location.model.CitySummary;
import qetaa.service.location.model.CountrySummary;
import qetaa.service.location.model.OrdersSummary;
import qetaa.service.location.model.RegionSummary;

public class Helper {

	
	
	public static void preapreOrderSummary(OrdersSummary ordersSummary) {
		for(CountrySummary countrySummary : ordersSummary.getCountrySummaries()) {
			Long totalCountry = 0L;
			for(RegionSummary regionSummary : countrySummary.getRegionSummaries()) {
				Long totalRegion = 0L;
				for(CitySummary citySummary : regionSummary.getCitySummaries()) {
					if(citySummary.getCount() != null) {
						totalRegion += citySummary.getCount().longValue();
					}
				}
				regionSummary.setCount(totalRegion);
				totalCountry += totalRegion;
			}
			countrySummary.setCount(totalCountry);
		}
	}
}
