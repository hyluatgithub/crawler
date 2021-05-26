package hydata.config;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Neo4jConfig{
//    @Value("${spring.data.neo4j.url}")
//    private String url;
//
//    @Value("${spring.data.neo4j.username}")
//    private String username;
//
//    @Value("${spring.data.neo4j.password}")
//    private String password;

//    private String url = "bolt://localhost:7687";
//    private String username = "neo4j";
//    private String password = "root";
//    @Bean(name = "session")
//    public Session neo4jSession() {
//        Driver driver = GraphDatabase.driver(url, AuthTokens.basic(username, password));
//        return driver.session();
//    }

}
