import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class AppInitializer implements WebApplicationInitializer {

    public void onStartup(ServletContext container) throws ServletException {

        AnnotationConfigWebApplicationContext context =
                new AnnotationConfigWebApplicationContext();
        //tworzymy kontekst aplikacji

        context.register(AppConfig.class);  //rejestrujemy klasę konfiguracji
        context.setServletContext(container); //ustawiamy kontekst servletów
        ServletRegistration.Dynamic servlet =
                container.addServlet("dispatcher"
                        , new DispatcherServlet(context)); //tworzymy i ustawiamy DispatcherServlet
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");  //ustawiamy adres przechwytywania dla naszej apliakcji

        FilterRegistration.Dynamic fr = container.addFilter("encodingFilter", new CharacterEncodingFilter());
        fr.setInitParameter("encoding", "UTF-8");
        fr.setInitParameter("forceEncoding", "true");
        fr.addMappingForUrlPatterns(null, true, "/*");


    }
}