package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.WebProperties;

public class HeadFilter implements Filter {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		if (!req.getRequestURI().endsWith("manageNews.jsp")) {
			req.getServletContext().getRequestDispatcher(WebProperties.config.getString("headJsp")).include(req, res);
		}

		chain.doFilter(request, response);

		if (!req.getRequestURI().endsWith("manageNews.jsp")) {
			req.getServletContext().getRequestDispatcher(WebProperties.config.getString("tailJsp")).include(req, res);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void destroy() {
	}

}
