package com.rentforhouse.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.rentforhouse.service.IDateService;

@Service
public class DateService implements IDateService{

	@Override
	public Date getDate() {
		Date date = new Date();
		return date;
	}

	@Override
	public String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

}
