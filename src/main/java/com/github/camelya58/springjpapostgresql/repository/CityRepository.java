package com.github.camelya58.springjpapostgresql.repository;

import com.github.camelya58.springjpapostgresql.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface CityRepository extends all the methods from JpaRepository.
 *
 * @author Kamila Meshcheryakova
 * created 02.06.2020
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    City findCityByName(String name);
}
