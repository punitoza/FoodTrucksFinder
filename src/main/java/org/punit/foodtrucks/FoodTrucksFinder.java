package org.punit.foodtrucks;

import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

public class FoodTrucksFinder {
	
	final static String DATA_URL = "https://data.sfgov.org/resource/bbb8-hzi6.json";
	final static int MAX_NAME_LENGTH = 50;
	final static String OUTPUT_HEADER_NAME = "NAME";
	final static String OUTPUT_HEADER_ADDR = "ADDRESS";		
	
	private static Logger logger = Logger.getLogger(FoodTrucksFinder.class.getName());
	
	private FoodTrucksRestClient ftClient;
	
	static {
		logger.setUseParentHandlers(false); //Disable console logging as it is a console based application
	}
	
	public FoodTrucksFinder() {
		initializeRestClient();
	}
	
	private void initializeRestClient() {
		ftClient = new FoodTrucksRestClient(DATA_URL, ZoneId.of("America/Los_Angeles"));
	}
	
	public static void main(String[] args) {
		FoodTrucksFinder ftFinder = new FoodTrucksFinder();
		ftFinder.run();
	}
	public void run() {
		System.out.println("\nHi! Welcome to SF Food Trucks Finder!");
        System.out.println("\nGetting the list of currently open food trucks in San Francisco...");
        List<FoodTruckData> truckList;
        int offset = 0;
        try (Scanner scanner = new Scanner(System.in)) {
	        while (true) {
		        try {
		        		System.out.println("\nPlease wait while the food trucks data is being loaded...");
		        		truckList = ftClient.fetchNowOpenFoodTrucks(offset);
		        		if (truckList.isEmpty() && offset == 0) {
		        			System.out.println("\nSorry, there are no food trucks open at this time in San Francisco.");
		        			break;
		        		} else if (truckList.isEmpty()) {
		        			System.out.println("\nThat is it, you have reached the end of the list of food trucks open at this time in San Francisco.");
		        			break;
		        		} else {
		        			System.out.println("\n" + StringUtils.rightPad(OUTPUT_HEADER_NAME, MAX_NAME_LENGTH)  + "  " + OUTPUT_HEADER_ADDR + "\n");
		        			for (FoodTruckData data: truckList) {
		        				System.out.println(StringUtils.rightPad(StringUtils.abbreviate(data.getName(), MAX_NAME_LENGTH), MAX_NAME_LENGTH) + "  " + data.getAddress());
		        			}
		        			System.out.println("\nPress Enter to see more food trucks if available or press 'q' followed by Enter to quit.");
		        			if (checkForQuitting(scanner)) 
		        				break;
		        			offset += truckList.size();
		        		}
		        } catch (FoodTrucksFinderException ex) {
		        		logger.severe(ex.getMessage());
		        		System.out.println("\nOops, couldn't get the data due to system error."); 
	    				System.out.println("\nPress 'q' followed by Enter to quit or press Enter to try again...");
	    				if (checkForQuitting(scanner))
	    					break;
		        }
	        }
        } catch (Exception ex) {
        		logger.severe(ex.getMessage());
        }
        quit();
    }
	
	private boolean checkForQuitting(final Scanner scanner) {
		String inputLine = scanner.nextLine();
		if (inputLine.length() > 0) {
			char input = inputLine.charAt(0);
			if (input == 'q') { 
				return true;
			}
		}
		return false;
	}
	
	private void quit() {
		System.out.println("\nThank you for using Food Trucks Finder, Good Bye!");
	}
	
}
