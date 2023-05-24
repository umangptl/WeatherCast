package com.example.weathercast.Modal;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WeatherDataRepository extends CrudRepository<City_Name, String> {
    Optional<City_Name> findByCity(String city);
}
