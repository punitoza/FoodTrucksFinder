# FoodTrucksFinder
Find food trucks in San Francisco!

This app will show the list of food trucks open in San Francisco at the current time. It will show up to 10 food trucks' names and locations in a page. The app uses the live food trucks data provided by San Francisco government’s website: https://data.sfgov.org/Economy-and-Community/Mobile-Food-Schedule/jjew-r69b

To interact with the app, follow the prompts on the console.

### Steps to Run
1. Install Java 8 JRE if not already installed.
2. Download the zip file named "ftf-exec.zip" from the repository home directory.
3. Extract the zip file into a directory at any desired location
4. On the console, navigate to the directory where the zip contents are extracted.
5. Run the command "sh food-trucks-finder.sh".
6. Follow the prompts to continue or quit.

### Steps to Build
1. Install the Java 8 JDK if not already installed.
2. Install maven if not already installed.
3. Clone the repository
4. On the console, navigate to the repository folder (FoodTrucksFinder)
5. Run command - "mvn clean package"
6. Once the build is successful, run command - "cp target/food-trucks-finder-0.0.1-SNAPSHOT.jar export/"
7. Update the zip file using the command - "zip -r ftf-exec.zip export"
 
