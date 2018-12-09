package org.punit.foodtrucks;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

public class FoodTrucksRestClient {
	
	private static Logger logger = Logger.getLogger(FoodTrucksRestClient.class.getName());
	
	static {
		logger.setUseParentHandlers(false); //Disable console logging as it is a console based application
	}
	
	Client restClient;
	final String DATA_URL;
	final ZoneId zoneId;
	
	public FoodTrucksRestClient(final String DATA_URL, final ZoneId zoneId) {
		this.DATA_URL = DATA_URL;
		this.zoneId = zoneId;
		initializeRestClient();
	}
	
	private void initializeRestClient() {
		ClientBuilder clientBuilder = ClientBuilder.newBuilder();
		clientBuilder.connectTimeout(10, TimeUnit.SECONDS);
		clientBuilder.readTimeout(10, TimeUnit.SECONDS);
		restClient = ClientBuilder.newClient();
	}
	
	public List<FoodTruckData> fetchNowOpenFoodTrucks(final int offset) throws FoodTrucksFinderException {
	    	try {	
	    		final String url = buildURL(offset);
	    		return restClient
					.target(url)
					.request(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<FoodTruckData>>() {});
	    } catch (Exception ex) {
	    		logger.severe(ex.getMessage());
	    		throw new FoodTrucksFinderException(ex);
	    }
	}

	private String[] getCompareTimes(ZonedDateTime now) {
		String[] compareTimes = new String[2];
		int hour = now.getHour();
		int minute = now.getMinute();
		String hourStr = hour + "";
		if (hour < 10) {
			hourStr = "0" + hourStr;
		}
		String minuteStr = minute + "";
		if (minute < 10) {
			minuteStr = "0" + minuteStr;
		}
		String startCompareTime = hourStr + ":" + minuteStr;
		String endCompareTime = startCompareTime;
		if (now.getSecond() > 0 || now.getNano() > 0) {
			//The current minute is already past and next minute is in progress. Hence increment the end time by 1
			minuteStr = 1 + minute + "";
    		if (minute < 10) {
    			minuteStr = "0" + minuteStr;
    		}
    		endCompareTime = hourStr + ":" + minuteStr;
		}
		compareTimes[0] = startCompareTime;
		compareTimes[1] = endCompareTime;
		return compareTimes;
	}

	private String buildURL(final int offset) throws UnsupportedEncodingException {
		
		ZonedDateTime now = ZonedDateTime.now(this.zoneId);
		
		/**
		 * If the current time hour and minute are equal to a food truck's end24 time, then we need to check if there are any seconds or nano seconds past after this minute
		 * and if so, the particular food truck should be filtered out and this can be done by rounding off the current time to next minute. 
		 */
		String[] compareTimes = getCompareTimes(now);
		String startCompareTime = compareTimes[0];
		String endCompareTime = compareTimes[1];
		
		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append("SELECT applicant,location,start24,end24")
					.append(" WHERE upper(dayofweekstr) = '").append(now.getDayOfWeek()).append("'")
					.append(" AND start24 <= '").append(startCompareTime).append("'")
					.append(" AND end24 >= '").append(endCompareTime).append("'")
					.append(" ORDER BY applicant,location")
					.append(" LIMIT 10")
					.append(" OFFSET ").append(offset);
		
		final String query = URLEncoder.encode(queryBuilder.toString(), "UTF-8");
		
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(DATA_URL)
					.append("?$query=")
					.append(query);
		return urlBuilder.toString();
	}
}
