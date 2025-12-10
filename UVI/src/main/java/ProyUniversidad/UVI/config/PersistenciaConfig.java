package ProyUniversidad.UVI.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@EnableMongoRepositories(basePackages = "ProyUniversidad.UVI.models.matricula.repositorys" // ✅ Corregido
)
public class PersistenciaConfig {


  @Bean
  public MongoClient mongoClient() {
    return MongoClients.create();
  }

  @Bean
  public MongoTemplate mongoTemplate() {

    return new MongoTemplate(mongoClient(), "db_matricula");
  }
}