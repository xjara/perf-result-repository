package cz.vutbr.fit.mis.dip.perffilter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import cz.vutbr.fit.mis.dip.perfclient.attrs.MAtt;
import cz.vutbr.fit.mis.dip.perfclient.constants.A;
import cz.vutbr.fit.mis.dip.perfclient.constants.U;
import cz.vutbr.fit.mis.dip.perfclient.monitor.PerfMonitor;


public class RtFilter implements Filter {
    private final Object lock = new Object();
    // create PerfMonitor in remote mode, it has not defined any automatic attributes
    private PerfMonitor p = new PerfMonitor().asRemote();

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
        ServletException {
 
        long t1 = 0;
        RtFilterResponseWrapper hresp = new RtFilterResponseWrapper(resp);
        
        synchronized (lock) {
        	t1 = System.currentTimeMillis();
        }

        try {
            chain.doFilter(req, hresp);
		} finally {
            synchronized (lock) {
            	long t2 = System.currentTimeMillis();
            	MAtt[] attrs = {new MAtt(A.REMOTE_RESPONSE_TIME, U.MILLISEC, (double) (t2 - t1)) {}};
            	p.sendAttributes(attrs);
            }
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }
}
