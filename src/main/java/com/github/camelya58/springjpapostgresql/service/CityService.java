package com.github.camelya58.springjpapostgresql.service;

import com.github.camelya58.springjpapostgresql.model.City;

import java.util.List;

/**
 * Interface CityService represents the business-logic of the application and contains a single method.
 *
 * @author Kamila Meshcheryakova
 * created 02.06.2020
 */
public interface CityService {

    /**
     * Provides a contract method to get all cities from the data source.
     * @return list of cities
     */
    List<City> findAll();

    /**
     * Provides a contract method to get the city by name from the data source.
     * @return city the object of class City
     */
    City findByName(String name);
}
