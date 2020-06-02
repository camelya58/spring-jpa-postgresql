package com.github.camelya58.springjpapostgresql.service;

import com.github.camelya58.springjpapostgresql.model.City;
import com.github.camelya58.springjpapostgresql.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class CityServiceImpl represents the realization of the CityService interface.
 *
 * @author Kamila Meshcheryakova
 * created 02.06.2020
 */
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    /**
     * Provides a contract method to get all cities from the data source.
     * @return list of cities
     */
    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    /**
     * Provides a contract method to get the city by name from the data source.
     * @return city the object of class City
     */
    @Override
    public City findByName(String name) {
        return cityRepository.findCityByName(name);
    }
}
