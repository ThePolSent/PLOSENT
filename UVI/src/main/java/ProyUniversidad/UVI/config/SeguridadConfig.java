package ProyUniversidad.UVI.config;

import ProyUniversidad.UVI.security.JwtFiltro;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true) 
public class SeguridadConfig {

  private final CorsFilter corsFilter;
  private final JwtFiltro jwtFiltro; 

  public SeguridadConfig(CorsFilter corsFilter, JwtFiltro jwtFiltro) {
    this.corsFilter = corsFilter;
    this.jwtFiltro = jwtFiltro;
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .addFilterBefore(corsFilter, org.springframework.security.web.context.SecurityContextPersistenceFilter.class)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/auth/**", "/api/public/**", "/error").permitAll() 
            .anyRequest().authenticated())
        .addFilterBefore(jwtFiltro, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}