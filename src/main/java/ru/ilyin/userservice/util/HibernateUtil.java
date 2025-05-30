package ru.ilyin.userservice.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;


    public static SessionFactory getSessionFactory(){
        if(sessionFactory == null){
            try {
                Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

                String config = System.getProperty("test.config") != null
                        ? "test-hibernate.cfg.xml"
                        : "hibernate.cfg.xml";

                registry =new StandardServiceRegistryBuilder()
                        .configure(config)
                        .applySettings(System.getProperties())
                        .build();
                MetadataSources sources = new MetadataSources(registry);

                Metadata metadata = sources.getMetadataBuilder().build();

                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                if(registry != null ){
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }

        }
        return sessionFactory;
    }

    public static void shutdown(){
        if(registry != null){
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
