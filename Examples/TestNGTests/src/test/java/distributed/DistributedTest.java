package distributed;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import cz.vutbr.fit.mis.dip.perfclient.monitor.PerfMonitor;

/*
 * Komplextni distribuovane testy. 
 */
public class DistributedTest {
	private static final String SERVER_URL = "http://localhost:8080/TestedPerfServer/";
	private static final int TEST_RUN_ID10 = 1;
	private static final int TEST_RUN_ID100 = 2;
	private static final int TEST_RUN_ID1000 = 3;
	private PerfMonitor p = new PerfMonitor();
	
	@BeforeSuite
	public void beforeSuite() throws IOException {
		// remote PerfMonitor is on SERVER_URL + URL of PerfClientSerServlet
		p.initializeRemotePerfMonitor(SERVER_URL + "init");
				
		// more remote PerfMonitors can be initialized here
	}
	
	private void send20GetRequestOnPageResults(long testRunId) throws IOException {
		for(int i = 0; i < 20; i++) {
			p.gainAttributesAndSend();
			Document document = Jsoup.connect(SERVER_URL + "faces/results.xhtml?testrunid=" + testRunId).get();
		}
	}
	
	@Test(description="Test posle 20 HTTP GET pozadavku na stranku, jejiz data byla ziskana pomoci 10 mereni automatickych atributu.")
	public void getRequestsOnPageResultsWith10MeasuredAttributes() throws IOException {
		send20GetRequestOnPageResults(TEST_RUN_ID10);
	}
	
	@Test(description="Test posle 20 HTTP GET pozadavku na stranku, jejiz data byla ziskana pomoci 100 mereni automatickych atributu.")
	public void getRequestsOnPageResultsWith100MeasuredAttributes() throws IOException {
		send20GetRequestOnPageResults(TEST_RUN_ID100);
	}
	
	@Test(description="Test posle 20 HTTP GET pozadavku na stranku, jejiz data byla ziskana pomoci 1000 mereni automatickych atributu.")
	public void getRequestsOnPageResultsWith1000MeasuredAttributes() throws IOException {
		send20GetRequestOnPageResults(TEST_RUN_ID1000);
	}
}
