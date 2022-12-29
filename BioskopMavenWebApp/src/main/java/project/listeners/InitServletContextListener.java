package project.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.stereotype.Component;

@Component
public class InitServletContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event)  {
    	System.out.println("Azuriranje konteksta ServletContextListener...");
    	
    	System.out.println("Uspeh ServletContextListener!");
    }
    
	public void contextDestroyed(ServletContextEvent event)  { 
    	System.out.println("Brisanje konteksta ServletContextListener...");
    		
    	System.out.println("Uspeh ServletContextListener!");
    }
}

