package com.github.camelya58.springjpapostgresql.controller;

import com.github.camelya58.springjpapostgresql.model.City;
import com.github.camelya58.springjpapostgresql.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Class CityController represents a simple controller.
 *
 * @author Kamila Meshcheryakova
 * created 02.06.2020
 */
@Controller
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping("/showCities")
    public String findCities (Model model) {
        var cities = cityService.findAll();
        model.addAttribute("cities", cities);
        return "showCities";
    }
    @GetMapping("/showCity/{name}")
    public String findCity(@PathVariable String name, Model model) {
        City city = cityService.findByName(name);
        model.addAttribute("city", city);
        return "showCity";
    }


}
