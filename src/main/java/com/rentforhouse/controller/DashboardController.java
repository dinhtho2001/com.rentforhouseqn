package com.rentforhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rentforhouse.service.DasdboardService;

@CrossOrigin(origins = {"http://random-quotes-webs.s3-website-ap-southeast-1.amazonaws.com/", "http://localhost:3000/"})
@RestController(value = "DasdboardAPIOfWeb")
@RequestMapping("/api/dasdboard")
public class DashboardController {

	@Autowired
	private DasdboardService dasdboardService; 
	
	@GetMapping("/interactive/chart")
	public ResponseEntity<?> interactiveLineChart(@RequestParam int year) {
		return dasdboardService.interactiveByYear(year);
	}
	
	@GetMapping("/interactive/chart/month/")
	public ResponseEntity<?> interactiveLineChart(@RequestParam int year, @RequestParam int month) {
		return dasdboardService.interactiveMonthByYear(year, month);
	}
	
	@GetMapping("/interactive/pie/chart/")
	public ResponseEntity<?> interactivePieChart(@RequestParam(required = false) int year) {
		return dasdboardService.interactivePieChart(year);
	}
}
