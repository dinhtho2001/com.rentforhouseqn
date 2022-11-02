package com.rentforhouse.service;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public interface IDateService {
	Date getDate();
	String formatDate(Date date);
}
