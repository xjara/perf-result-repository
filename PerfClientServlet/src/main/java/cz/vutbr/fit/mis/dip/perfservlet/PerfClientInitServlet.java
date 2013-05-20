 package cz.vutbr.fit.mis.dip.perfservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PerfClientInitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String repURL = req.getParameter("repurl");
		String testSuiteRunId = req.getParameter("testsuiterunid");
		
		if (repURL != null) {
			System.setProperty("repURL", repURL);
		}
		if (testSuiteRunId != null) {
			System.setProperty("testSuiteRunId", testSuiteRunId);
		}
		
		PrintWriter pw = resp.getWriter();
		pw.println("<h1>Remote PerfMonitor</h1>");
		pw.println("<h3>Configuration:</h3>");
		pw.println("testSuiteRunId: " + testSuiteRunId + "<br>");
		pw.println("PerfServer URL: " + repURL);
	}
}
