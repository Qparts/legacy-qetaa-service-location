package qetaa.service.location.restful;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import qetaa.service.location.dao.DAO;
import qetaa.service.location.filters.Secured;
import qetaa.service.location.filters.SecuredCustomer;
import qetaa.service.location.filters.SecuredUser;
import qetaa.service.location.filters.ValidApp;
import qetaa.service.location.helpers.AppConstants;
import qetaa.service.location.helpers.Helper;
import qetaa.service.location.model.CitySummary;
import qetaa.service.location.model.CountrySummary;
import qetaa.service.location.model.OrdersSummary;
import qetaa.service.location.model.PublicCity;
import qetaa.service.location.model.PublicCountry;
import qetaa.service.location.model.PublicRegion;
import qetaa.service.location.model.RegionSummary;

@Path("/api/v1/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ApiV1 {

	@EJB
	private DAO dao;

	@GET
	@Path("regions/country-id/{param}")
	@Secured
	public Response getRegionsFromCountryId(@PathParam(value = "param") int countryId) {
		try {
			List<PublicRegion> regions = dao.getCondition(PublicRegion.class, "countryId", countryId);
			for (PublicRegion region : regions) {
				List<PublicCity> cities = dao.getTwoConditions(PublicCity.class, "customerStatus", "regionId", 'A',
						region.getId());
				region.setCities(cities);
			}
			return Response.status(200).entity(regions).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@GET
	@ValidApp
	@Path("countries-only")
	public Response getActiveCountriesOnly() {
		try {
			List<PublicCountry> list = dao.getConditionOrderBy(PublicCountry.class, "customerStatus", 'A', "id");
			return Response.status(200).entity(list).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	private void prepareRegionsCities(List<PublicRegion> regions) {
		for (PublicRegion region : regions) {
			List<PublicCity> cities = dao.getTwoConditions(PublicCity.class, "customerStatus", "regionId", 'A',
					region.getId());
			region.setCities(cities);
		}
	}

	private void prepareCountryRegions(List<PublicCountry> countries) {
		for (PublicCountry country : countries) {
			List<PublicRegion> regions = dao.getCondition(PublicRegion.class, "countryId", country.getId());
			prepareRegionsCities(regions);
			country.setRegions(regions);
		}
	}

	@GET
	@ValidApp
	@Path("countries")
	public Response getActiveCountriesFull() {
		try {
			List<PublicCountry> countries = dao.getConditionOrderBy(PublicCountry.class, "customerStatus", 'A', "id");
			prepareCountryRegions(countries);
			return Response.status(200).entity(countries).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@GET
	@Path("carts-count/from/{from}/to/{to}/make/{makeId}/archived/{archived}/ordered/{ordered}")
	public Response getCartsCountFiltered(@HeaderParam("Authorization") String authHeader, @PathParam(value="from") long from, @PathParam(value="to") long to, @PathParam(value="makeId") int makeId, @PathParam(value="archived") boolean archived, @PathParam(value="ordered") boolean ordered) {
		try {
			OrdersSummary orderSummary = new OrdersSummary();
			Response r = this.getSecuredRequest(AppConstants.getCityOrdersCount(from, to, makeId, archived, ordered), authHeader);
			if (r.getStatus() == 200) {
				List<Map<String, Number>> cityOrders = r.readEntity(new GenericType<List<Map<String, Number>>>() {
				});
				List<PublicCountry> countries = dao.get(PublicCountry.class);
				for (PublicCountry country : countries) {
					// set country regions
					List<PublicRegion> regions = dao.getCondition(PublicRegion.class, "countryId", country.getId());
					country.setRegions(regions);

					CountrySummary ctm = new CountrySummary();
					ctm.setCountry(country);
					for (PublicRegion region : regions) {
						List<PublicCity> cities = dao.getCondition(PublicCity.class, "regionId", region.getId());
						region.setCities(cities);

						RegionSummary rm = new RegionSummary();
						rm.setRegion(region);
						List<CitySummary> citySummaries = new ArrayList<>();
						for (PublicCity city : cities) {
							CitySummary cm = new CitySummary();
							cm.setCity(city);
							for (Map<String, Number> map : cityOrders) {
								if (map.get("cityId").intValue() == city.getId()) {
									cm.setCount(map.get("count"));
									break;
								}
							}
							citySummaries.add(cm);
						}
						rm.setCitySummaries(citySummaries);
						ctm.getRegionSummaries().add(rm);
					}
					orderSummary.getCountrySummaries().add(ctm);
				}
			}
			Helper.preapreOrderSummary(orderSummary);
			return Response.status(200).entity(orderSummary).build();
			// set values
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
		//
	}

	@GET
	@Path("carts-count")
	public Response getCartsCount(@HeaderParam("Authorization") String authHeader) {
		try {
			OrdersSummary orderSummary = new OrdersSummary();
			Response r = this.getSecuredRequest(AppConstants.GET_CITY_ORDERS_COUNT, authHeader);
			if (r.getStatus() == 200) {
				List<Map<String, Number>> cityOrders = r.readEntity(new GenericType<List<Map<String, Number>>>() {
				});
				List<PublicCountry> countries = dao.get(PublicCountry.class);
				for (PublicCountry country : countries) {
					// set country regions
					List<PublicRegion> regions = dao.getCondition(PublicRegion.class, "countryId", country.getId());
					country.setRegions(regions);

					CountrySummary ctm = new CountrySummary();
					ctm.setCountry(country);
					for (PublicRegion region : regions) {
						List<PublicCity> cities = dao.getCondition(PublicCity.class, "regionId", region.getId());
						region.setCities(cities);

						RegionSummary rm = new RegionSummary();
						rm.setRegion(region);
						List<CitySummary> citySummaries = new ArrayList<>();
						for (PublicCity city : cities) {
							CitySummary cm = new CitySummary();
							cm.setCity(city);
							for (Map<String, Number> map : cityOrders) {
								if (map.get("cityId").intValue() == city.getId()) {
									cm.setCount(map.get("count"));
									break;
								}
							}
							citySummaries.add(cm);
						}
						rm.setCitySummaries(citySummaries);
						ctm.getRegionSummaries().add(rm);
					}
					orderSummary.getCountrySummaries().add(ctm);
				}
			}
			Helper.preapreOrderSummary(orderSummary);
			return Response.status(200).entity(orderSummary).build();
			// set values
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}

	@GET
	@Path("find-city/name/{param}/country/{param2}")
	@SecuredCustomer
	public Response findCountryCityByName(@PathParam(value = "param2") int countryId,
			@PathParam(value = "param") String name) {
		try {
			//remove (al) in arabic and english, 
			//add another search condition
			//for example : Khobar, Al-Khobar, Alkhobar
			String sql = "select d from PublicCity d where lower(d.name) like :value0 "
					+ " or lower(d.nameAr) like :value0 and d.countryId = :value1 and d.customerStatus = :value2";
			List<PublicCity> cities = dao.getJPQLParams(PublicCity.class, sql, "%" + name.trim().toLowerCase() + "%",
					countryId, 'A');
			if (cities.isEmpty()) {
				return Response.status(404).build();
			}
			return Response.status(200).entity(cities).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}

	public Response getSecuredRequest(String link, String authHeader) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, authHeader);
		Response r = b.get();
		return r;
	}

}
