import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by wb-zhangkenan on 2016/12/29.
 */
public class FirstServlet implements Servlet{

    public void init(ServletConfig servletConfig) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

        PrintWriter out = servletResponse.getWriter();
        out.println("Hello. Roses are red.");
    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }
}
