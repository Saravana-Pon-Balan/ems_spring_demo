package com.e5.ems.configuration;

import com.e5.ems.util.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p>
 *     It is implementation of adding security configurations
 * </p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * <p>
     *     This method is used for configure filter chain for Authentication
     * </p>
     * @param http
     *          is used for add configuration for http requests
     * @return {@link SecurityFilterChain}
     *          the configured details are returned
     * @throws Exception
     *          if there is any exception occurs while configuration
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req.requestMatchers("v1/employees/register", "v1/employees/login")
                .permitAll()
                .anyRequest()
                .authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(Customizer.withDefaults());
        return http.build();
    }

    /**
     *<p>
     *     This method is used for configure authentication By userDetailsService
     *</p>
     * @return {@link AuthenticationProvider}
     *      the configured Authentication provider is returned
     */
    @Bean
    public AuthenticationProvider provideAuthentication() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    /**
     * <p>
     *     This method is used for get the AuthenticationManager
     * </p>
     * @param authenticationConfiguration
     *          is used for get the AuthenticationManager
     * @return {@link AuthenticationManager}
     *          the AuthenticationConfiguration to get AuthenticationManager
     * @throws Exception
     *          If the problem occurs while get the AuthenticationManager
     */
    @Bean
    public AuthenticationManager manageAuthentication(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
