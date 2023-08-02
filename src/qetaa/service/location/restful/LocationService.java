package qetaa.service.location.restful;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import qetaa.service.location.dao.DAO;
import qetaa.service.location.filters.Secured;
import qetaa.service.location.filters.SecuredCustomer;
import qetaa.service.location.filters.SecuredUser;
import qetaa.service.location.filters.ValidApp;
import qetaa.service.location.model.City;
import qetaa.service.location.model.Country;
import qetaa.service.location.model.Region;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LocationService {
	@EJB
	private DAO dao;
	
	@Secured
	@ValidApp
	@GET 
	@SecuredCustomer
	public List<Region> test() {
		List<Region> regions = dao.getCondition(Region.class, "country.id", 1);
		for(Region region : regions) {
			List<City> cities = dao.getCondition(City.class, "region", region);
			region.setCities(cities);
		}
		return regions;
	}
	
	@GET
	@Path("active-countries-internal")
	@SecuredUser//only user can see this
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveCountriesInternal(){
		try{
			List<Country> list = dao.getCondition(Country.class, "internalStatus", "A");
			for(Country c : list){
				List<City> cities = dao.getTwoConditions(City.class, "country", "internalStatus", c, 'A');
				c.setCities(cities);
				List<Region> regions = dao.getCondition(Region.class, "country", c);
				for(Region region : regions) {
					List<City> cities2 = dao.getTwoConditions(City.class, "region", "internalStatus", region, 'A');
					region.setCities(cities2);
				}
				c.setRegions(regions);
			}
			return Response.status(200).entity(list).build();
		}
		catch (Exception ex){
			return Response.status(500).build();
		}
	}
	
	

	
	
	
	@GET
	@Path("all-regions")
	@SecuredUser
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllRegions() {
		try {
			List<Region> regions = dao.get(Region.class);
			for(Region region : regions) {
				List<City> cities = dao.getCondition(City.class, "region", region);
				region.setCities(cities);
			}
			return Response.status(200).entity(regions).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	

	
	@GET
	@Path("regions/country/{param}")
	@SecuredUser
	public Response getCountryRegions(@PathParam(value="param") int countryId) {
		try {
			List<Region> regions = dao.getCondition(Region.class, "country.id", countryId);
			for(Region region : regions) {
				List<City> cities = dao.getCondition(City.class, "region", region);
				region.setCities(cities);
			}
			return Response.status(200).entity(regions).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@GET
	@Path("all-countries")
	@SecuredUser//only user can see this
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCountriesInternal(){
		try{
			List<Country> list = dao.get(Country.class);
			for(Country c : list){
				List<City> cities = dao.getCondition(City.class, "country", c);
				c.setCities(cities);
				List<Region> regions = dao.getCondition(Region.class, "country", c);
				for(Region region : regions) {
					List<City> cities2 = dao.getCondition(City.class, "region", region);
					region.setCities(cities2);
				}
				c.setRegions(regions);
			}
			return Response.status(200).entity(list).build();
		}
		catch (Exception ex){
			return Response.status(500).build();
		}
	}
	
	
	
	
	
	
	@GET
	@Path("active-countries-customers")
	@ValidApp
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveCountries(){
		try{
			List<Country> list = dao.getConditionOrderBy(Country.class, "customerStatus", 'A', "id");
			for(Country c : list){
				List<City> cities = dao.getTwoConditions(City.class, "country", "customerStatus", c, 'A');
				c.setCities(cities);
				List<Region> regions = dao.getCondition(Region.class, "country", c);
				for(Region region : regions) {
					List<City> cities2 = dao.getTwoConditions(City.class, "region", "customerStatus", region, 'A');
					region.setCities(cities2);
				}
				c.setRegions(regions);
				
			}
			return Response.status(200).entity(list).build();
		}
		catch(Exception ex){
			return Response.status(500).build();
		}
	}
	
	//idempotent
	@POST
	@Path("country")
	@SecuredUser//only user can create
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCountry(Country country){
		try{
			List<Country> l = dao.getCondition(Country.class, "name", country.getName());
			if(!l.isEmpty()){
				return Response.status(409).build();//conflict! Resource exists
			}
			dao.persist(country);
			return Response.status(200).build();
		}
		catch(Exception ex){
			return Response.status(500).build();
		}
	}
	
	@GET
	@Path("all-cities")
	@Secured
	public Response getAllCitiesInternall(){
		try{
			List<City> list = dao.get(City.class);
			return Response.status(200).entity(list).build();
		}
		catch (Exception ex){
			return Response.status(500).build();
		}
	}
	
	@GET
	@Path("active-cities-internal")
	@SecuredUser
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveCitiesInternal(){
		try{
			List<City> list = dao.getConditionOrderBy(City.class, "internalStatus", 'A', "name");
			return Response.status(200).entity(list).build();
		}
		catch(Exception ex){
			return Response.status(500).build();
		}
	}
	
	@GET
	@Path("active-cities-customer")
	@ValidApp
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveCities(){
		try{
			List<City> list = dao.getCondition(City.class, "customerStatus", 'A');
			return Response.status(200).entity(list).build();
		}
		catch(Exception ex){
			return Response.status(500).build();
		}
	}
	
	
	
	@GET
	@Path("active-cities-customer/country/{param}")
	@ValidApp
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveCities(@PathParam(value="param") int countryId){
		try{
			List<City> list = dao.getTwoConditionsOrdered(City.class, "country.id","customerStatus", countryId, 'A', "nameAr", "asc");
			return Response.status(200).entity(list).build();
		}
		catch(Exception ex){
			return Response.status(500).build();
		}
	}
	
	

	@GET
	@Path("active-region-cities-customer/country/{param}")
	@ValidApp
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveRegionCities(@PathParam(value="param") int countryId){
		try{
			List<Region> regions = dao.getConditionOrderBy(Region.class, "country.id", countryId, "nameAr");
			for(Region region : regions) {
				List<City> list = dao.getTwoConditionsOrdered(City.class, "region","customerStatus", region, 'A', "nameAr", "asc");
				region.setCities(list);
			}
			
			return Response.status(200).entity(regions).build();
		}
		catch(Exception ex){
			return Response.status(500).build();
		}
	}
	

	@GET
	@Path("active-cities-internal/country/{param}")
	@SecuredUser
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveCitiesInternal(@PathParam(value="param") int countryId){
		try{
			List<City> list = dao.getTwoConditionsOrdered(City.class, "country.id","internalStatus", countryId, 'A', "nameAr", "asc");
			return Response.status(200).entity(list).build();
		}
		catch(Exception ex){
			return Response.status(500).build();
		}
	}
	
	
	@POST
	@Path("region")
	@SecuredUser
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRegion(Region region) {
		List<Region> l = dao.getCondition(Region.class, "name", region.getName());
		if(!l.isEmpty()){
			return Response.status(409).build();//conflict! Resource exists
		}
		dao.persist(region);
		return Response.status(200).build();
	}
	
	//idempotent
	@POST
	@Path("city")
	@SecuredUser//only user can create
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCity(City city){
		try{
			List<City> l = dao.getCondition(City.class, "name", city.getName());
			if(!l.isEmpty()){
				return Response.status(409).build();//conflict! Resource exists
			}
			dao.persist(city);
			return Response.status(200).build();
		}
		catch(Exception ex){
			return Response.status(500).build();
		}
	}
	
	@Secured
	@GET
	@Path("city/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCity(@PathParam(value="param") int cityId) {
		try {
			City city = dao.find(City.class, cityId);
			if(city == null) {
				return Response.status(404).build();
			}
			return Response.status(200).entity(city).build();
		}
		catch(Exception ex){
			return Response.status(500).build();
		}
	}
	
	
	@Secured
	@GET
	@Path("region/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRegion(@PathParam(value="param") int regionId) {
		try {
			Region region = dao.find(Region.class, regionId);
			if(region == null) {
				return Response.status(404).build();
			}
			List<City> cities = dao.getCondition(City.class, "region", region);
			region.setCities(cities);
			return Response.status(200).entity(region).build();
		}
		catch(Exception ex){
			return Response.status(500).build();
		}
	}
	
	
}
