package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import cz.vutbr.fit.mis.dip.perfobjects.constants.Format;
import cz.vutbr.fit.mis.dip.perfserver.enums.Const;

@ManagedBean
@ApplicationScoped
public class ConfigBean {	
	private int rows = Const.BASIC_ROW_COUNT;
	private final String rowsPerPage = rows + "," + (rows + Const.ADD_ROW_COUNT) + "," + (rows + Const.ADD_ROW_COUNT + Const.ADD_ROW_COUNT);
	private int fractionDigits = Const.MAX_FRACTION_DIGITS;
	private SimpleDateFormat dateTimeF = new SimpleDateFormat(Format.DATE_TIME);
	private String testSuiteDateTime = Const.DATE_TIME_SUITE;
	private SimpleDateFormat suiteDateTimeF = new SimpleDateFormat(testSuiteDateTime);
	private TimeZone localTimeZone;
	
	@PostConstruct
	public void init() {
		localTimeZone = Calendar.getInstance().getTimeZone();
		suiteDateTimeF.setTimeZone(localTimeZone);
	}
	
	public String getRowsPerPage() {
		return rowsPerPage;
	}

	public int getRows() {
		return rows;
	}
	
	public int getFractionDigits() {
		return fractionDigits;
	}
	
	public String getTestSuiteDateTime() {
		return testSuiteDateTime;
	}
	
	public String formatDateTime(Date date) {
		return dateTimeF.format(date);
	}
	
	public String formatSuiteDateTime(Date date) {
		return suiteDateTimeF.format(date);
	}
	
	public TimeZone getLocalTimeZone() {
		return localTimeZone;
	}
}
