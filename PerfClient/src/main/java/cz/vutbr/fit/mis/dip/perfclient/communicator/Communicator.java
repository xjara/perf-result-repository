package cz.vutbr.fit.mis.dip.perfclient.communicator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.vutbr.fit.mis.dip.perfclient.communicator.Method;

public class Communicator {
	private static final Logger logger = LoggerFactory.getLogger(Communicator.class);
	private HttpURLConnection connection;
	
	private static final String PERF_SERVER = "PerfServer";
	private static final int TIME = 30000;
	
	public Communicator() {
	}
	
	public void setUpConnection(String urlPath, Method method) {
		try {
			URL url = new URL(urlPath);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod(method.name());
			connection.setRequestProperty("Content-Type", "text/json");
			connection.setConnectTimeout(TIME);
			connection.setReadTimeout(TIME);		
		} catch (SocketTimeoutException e) {
			logger.error("Time out for connection " + TIME + " was exceeded.\n{}", e); 
		} catch (MalformedURLException e) {
			logger.error(PERF_SERVER + " url address has incorrect format.\n{}", e);
		} catch (IOException e) {
			logger.error(PERF_SERVER + " couldn't have been contacted.\n{}", e);
		}
	}
	
	public String receiveData() {
		StringBuffer result = new StringBuffer();
		try {
			if (connection.getResponseCode() == 333 || connection.getResponseCode() == 399) {
				logger.warn("CODE: {}", connection.getResponseCode());
			} else if (connection.getResponseCode() < 200 || connection.getResponseCode() >= 300) {
				logger.error("CODE: {}", connection.getResponseCode());
			} else {
				logger.debug("CODE: {}", connection.getResponseCode());
			}
			
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = br.readLine()) != null) {
				result.append(line);
			}
			br.close();
		} catch (IOException e) {
			logger.error(PERF_SERVER + " couldn't have been contacted.\n{}", e);		
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return result.toString();
	}
	
	public String sendDataAndReceiveAnswer(String data) {
		String response = null;
		try {
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.write(data.getBytes());
			out.flush();
			out.close();
			
			response = receiveData();
		} catch (MalformedURLException e) {
			logger.error(PERF_SERVER + " url address has incorrect format.\n{}", e);
		} catch (IOException e) {
			logger.error(PERF_SERVER + " couldn't have been contacted.\n{}", e);
		}
		return response;
	}
}
