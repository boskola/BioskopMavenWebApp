package project.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import project.controller.FilmoviController;
import project.model.FilmStatistika;

@Component
public final class InitServletContextInitializer implements ServletContextInitializer {
 
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	System.out.println("Inicijalizacija konteksta pri ServletContextInitializer...");

		servletContext.setAttribute(FilmoviController.STATISTIKA_FILMOVA_KEY, new FilmStatistika());
		
    	System.out.println("Uspeh ServletContextInitializer!");
    }

}