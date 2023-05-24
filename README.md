# WeatherCast
### Introduction
WeatherCast is a web-based application that provides real-time weather information for any location worldwide. The app is designed to help users search accurate and up-to-date weather information. With WeatherCast, users can quickly access current weather conditions and Save for any city, allowing them to make informed decisions based on the weather.

### Features
- Search and view real-time weather information for any location worldwide.
- Save favorite cities for quick access to weather updates.
- Display a 3-hour forecast with temperature and weather icons.
- Display a 5-day forecast with temperature ranges and weather icons.
- Fetch weather data using the OpenWeatherMap API.
- Store user data and favorite cities in a PostgreSQL database.

### Technologies Used
- Java
- Spring Boot
- Thymeleaf (HTML templating engine)
- PostgreSQL (Relational Database Management System)
- OpenWeatherMap API

### Prerequisites
- Java Development Kit (JDK) 8 or above installed.
- PostgreSQL database server installed.
- OpenWeatherMap API key. (Register at https://openweathermap.org/ and obtain an API key)

### Getting Started
1. Clone the WeatherCast repository to your local machine:
```
git clone https://github.com/umangptl/weatherCast.git
```
2. Navigate to the project directory:
```
cd weathercast
```
3. Open the src/main/resources and create a application.properties file and configure the following properties:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/weathercast_db
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```
Open the src/main/java/com/example/weatherast/controller/WeatherDataController
```
public static final String API_KEY = "Api_Key";
```
Replace your-username, your-password, and your-api-key with your PostgreSQL credentials and OpenWeatherMap API key, respectively.

4. Create a new database named weathercast_db in your PostgreSQL server.

5. Build the project using Maven:
```
mvn clean install
```
6. Run the application:
```
mvn spring-boot:run
```
7. Access the application in your web browser at http://localhost:8080

###Usage
1. On the WeatherCast home page, enter the name of a city in the search bar and click the "Search" button. and use the switch button to change the units metric or imperial  
2. View the current weather conditions for the searched city, including temperature, humidity, wind speed, and weather description. 3-hour forecast, 5-day forecast.
3. click Save on the header see your Favorites cities and search a city to save.

### Contributions
Contributions to WeatherCast are welcome! If you find any issues or have suggestions for improvements, feel free to open an issue or submit a pull request.

When contributing, please follow the existing code style and conventions. Provide clear and descriptive commit messages and ensure that your code changes are thoroughly tested.
