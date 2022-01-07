package letscode.challenge.moviesbattle.filters;

import letscode.challenge.moviesbattle.services.UserAndSessionService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/v1/match/*")
public class AuthFilter implements Filter {

    @Autowired
    private UserAndSessionService userService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        String userToken = ((HttpServletRequest) req).getHeader("x-usertoken");
        if (userToken == null || userToken.isEmpty()) {
            ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (!userService.isValidSession(userToken)) {
            ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
