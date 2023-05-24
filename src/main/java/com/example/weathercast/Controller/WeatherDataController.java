package com.example.weathercast.Controller;

import com.example.weathercast.Modal.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class WeatherDataController {

    private static final Logger logger = LogManager.getLogger(WeatherDataController.class);
    @Autowired
    private WeatherDataRepository weatherDataRepository;
    public static final String API_KEY = "Api_Key";
    public static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static final String API_Id = "&appid=";
    public static final String API_imperial = "&units=imperial";
    public static final String API_metric = "&units=metric";
    public static final String FOW_URL ="https://api.openweathermap.org/data/2.5/forecast?q=";

    private WeatherCast fetchWeatherData(String city,String units) {

        String apiUrl;
        if (units.equals("imperial")) {
             apiUrl = API_URL + city + API_Id + API_KEY + API_imperial;
        } else {
             apiUrl = API_URL + city + API_Id + API_KEY + API_metric;
        }

        logger.info("Fetching weather data for city {}", city);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiUrl, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        long timestamp= root.path("dt").asInt();
        int temp = root.path("main").path("temp").asInt();
        String icon = root.path("weather").get(0).path("icon").asText();
        String description = root.path("weather").get(0).path("description").asText();
        int temp_min = root.path("main").path("temp_min").asInt();
        int temp_max = root.path("main").path("temp_max").asInt();
        long rise = root.path("sys").path("sunrise").asInt();
        long set = root.path("sys").path("sunset").asInt();
        int speed = root.path("wind").path("speed").asInt();
        int wind_deg = root.path("wind").path("deg").asInt();
        int humidity = root.path("main").path("humidity").asInt();
        int visibility= root.path("visibility").asInt();
        int timezoneOffset= root.path("timezone").asInt();

        ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(timezoneOffset);
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.ofOffset("UTC", zoneOffset));
        LocalDateTime sun_rise = LocalDateTime.ofInstant(Instant.ofEpochSecond(rise), ZoneId.ofOffset("UTC", zoneOffset));
        LocalDateTime sun_set = LocalDateTime.ofInstant(Instant.ofEpochSecond(set), ZoneId.ofOffset("UTC", zoneOffset));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, h:mma", Locale.ENGLISH);
        DateTimeFormatter timer = DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH);

        String dt = date.format(formatter);
        String sunrise = sun_rise.format(timer);
        String sunset = sun_set.format(timer);

        WeatherCast weatherCast= new WeatherCast(city,dt,temp,icon,description,temp_min,temp_max,
                sunrise,sunset,speed,wind_deg,humidity,visibility);

        logger.info("Fetched weather data for city {}: {}", city, weatherCast);

        return weatherCast;
    }

    private List<HourlyForecast> fetchForecast(String city, String units) {

        String apiUrl;
        if (units.equals("imperial")) {
            apiUrl = FOW_URL + city + API_Id + API_KEY + API_imperial;
        } else {
            apiUrl = FOW_URL + city + API_Id + API_KEY + API_metric;
        }

        logger.info("Fetching forecast data for city {}", city);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiUrl, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        List<HourlyForecast> hourlyForecastDataList = new ArrayList<>();

        JsonNode listNode = root.get("list");
        for (int i = 0; i < Math.min(8, listNode.size()); i++)  {
            JsonNode dataNode = listNode.get(i);

            long timestamp= dataNode.path("dt").asInt();
            String mappedIcon = dataNode.get("weather").get(0).get("icon").asText();
            String icon = mapIcon(mappedIcon);

            int temperature = dataNode.get("main").get("temp").asInt();
            int timezoneOffset= root.path("timezone").asInt();

            ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(timezoneOffset);
            LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.ofOffset("UTC", zoneOffset));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH);
            String dt = date.format(formatter);

            HourlyForecast hourlyForecastData = new HourlyForecast(dt, icon, temperature);
            hourlyForecastDataList.add(hourlyForecastData);
            logger.info("Fetched forecastData for city {}: {}", city, hourlyForecastData);

        }

        return hourlyForecastDataList;
    }

    private List<DailyForecast> fetchDailyForecast(String city, String units) {
        String apiUrl;
        if (units.equals("imperial")) {
            apiUrl = FOW_URL + city + API_Id + API_KEY + API_imperial;
        } else {
            apiUrl = FOW_URL + city + API_Id + API_KEY + API_metric;
        }

        logger.info("Fetching daily forecast data for city {}", city);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiUrl, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<DailyForecast> dailyForecasts = new ArrayList<>();

        JsonNode listNode = root.get("list");

        // Group forecast data by date
        Map<LocalDate, List<JsonNode>> forecastByDate = new TreeMap<>();
        for (JsonNode dataNode : listNode) {
            long timestamp = dataNode.path("dt").asLong();
            int timezoneOffset = root.path("timezone").asInt();

            ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(timezoneOffset);
            LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.ofOffset("UTC", zoneOffset));
            LocalDate localDate = date.toLocalDate();

            List<JsonNode> forecastList = forecastByDate.getOrDefault(localDate, new ArrayList<>());
            forecastList.add(dataNode);
            forecastByDate.put(localDate, forecastList);
        }

        // Process forecast data for each date
        int daysLimit = 5;
        int daysCount = 0;
        for (Map.Entry<LocalDate, List<JsonNode>> entry : forecastByDate.entrySet()) {
            if (daysCount >= daysLimit) {
                break;
            }

            LocalDate date = entry.getKey();
            List<JsonNode> forecastList = entry.getValue();

            int tempMin = Integer.MAX_VALUE;
            int tempMax = Integer.MIN_VALUE;
            Map<String, Integer> iconCounts = new HashMap<>();

            // Calculate min/max temperature and count icon occurrences
            for (JsonNode dataNode : forecastList) {
                int temp = dataNode.get("main").get("temp").asInt();
                String iconId = dataNode.get("weather").get(0).get("icon").asText();

                tempMin = Math.min(tempMin, temp);
                tempMax = Math.max(tempMax, temp);

                int count = iconCounts.getOrDefault(iconId, 0) + 1;
                iconCounts.put(iconId, count);
            }

            // Determine the most repeated icon ID
            String mostRepeatedIconId = "";
            int maxCount = 0;
            for (Map.Entry<String, Integer> iconEntry : iconCounts.entrySet()) {
                if (iconEntry.getValue() > maxCount) {
                    maxCount = iconEntry.getValue();
                    mostRepeatedIconId = iconEntry.getKey();
                }
            }

            // Map the most repeated icon ID to a simplified icon name
            String icon = mapIcon(mostRepeatedIconId);

            // Determine the date in format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH);
            String dt = date.format(formatter);

            // Create DailyForecast object and add it to the list
            DailyForecast dailyForecast = new DailyForecast(dt, icon, tempMax, tempMin);
            dailyForecasts.add(dailyForecast);

            logger.info("Fetched daily forecast for city {}: {}", city, dailyForecast);

            daysCount++;
        }

        return dailyForecasts;
    }

    private String mapIcon(String icon) {
        switch (icon) {
            case "01d":
            case "02d":
                return "sun";
            case "01n":
            case "02n":
                return "moon";
            case "03d":
            case "03n":
                return "cloud";
            case "04d":
                return "cloudsun";
            case "04n":
                return "cloudnight";
            case "09n":
            case "09d":
            case "10d":
            case "10n":
                return "rain";
            case "11d":
            case "11n":
                return "thunderstorm";
            case "13d":
            case "13n":
                return "snowflake";
            case "50d":
            case "50n":
                return "mist";
            default:
                return icon;
        }
    }

    @GetMapping("/")
    public String index() {
        logger.info("WeatherCast Main page");
        return "index";
    }

    @PostMapping("/weather")
    public String getWeather(@RequestParam("search-city") String city, @RequestParam(value = "units", defaultValue = "metric") String units, Model model) {

        logger.info("Weather for city {}", city);
        logger.info("Units: {}", units);

        WeatherCast weatherCast = fetchWeatherData(city, units);
        List<HourlyForecast> hourlyForecastDataList = fetchForecast(city, units);
        List<DailyForecast> dailyForecasts = fetchDailyForecast(city, units);

        model.addAttribute("weatherCast", weatherCast);
        model.addAttribute("forecast", hourlyForecastDataList);
        model.addAttribute("dailyForecasts", dailyForecasts);
        return "weatherInfo";
    }

    @PostMapping("/save")
    public String saveWeather(@RequestParam("add-city") String city, @RequestParam(value = "units", defaultValue = "metric") String units, Model model) {
        City_Name cityEntity = new City_Name();
        cityEntity.setCity(city);
        weatherDataRepository.save(cityEntity);

        logger.info("City {} saved in the database", city);
        logger.info("Units: {}", units);
        model.addAttribute("city", city);

        return "redirect:/list";
    }

    @GetMapping("/list")
    public String getAllWeatherData(@RequestParam(value = "units", defaultValue = "metric") String units,Model model) {
        List<City_Name> cityList = (List<City_Name>) weatherDataRepository.findAll();
        List<String> cityNames = cityList.stream()
                .map(City_Name::getCity)
                .collect(Collectors.toList());

        logger.info("Retrieved all city names from the database");

        List<WeatherCast> weatherDataList = new ArrayList<>();
        for (String cityName : cityNames) {
            WeatherCast weatherCast = fetchWeatherData(cityName,units);
            weatherDataList.add(weatherCast);
        }

        model.addAttribute("weatherDataList", weatherDataList);
        return "display";
    }

    @PostMapping("/delete")
    public String deleteWeatherData(@RequestParam("cityToDelete") String city) {
        Optional<City_Name> cityEntityOptional = weatherDataRepository.findByCity(city);

        if (cityEntityOptional.isPresent()) {
            City_Name cityEntity = cityEntityOptional.get();
            weatherDataRepository.delete(cityEntity);

            logger.info("Deleted weather data for city: {}", city);
        } else {
            logger.warn("Weather data not found for city: {}", city);
        }

        return "redirect:/list";
    }

}
