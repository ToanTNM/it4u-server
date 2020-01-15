package vn.tpsc.it4u.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import vn.tpsc.it4u.security.CustomAuthenticationEntryPoint;
import vn.tpsc.it4u.security.CustomUserDetailsService;
import vn.tpsc.it4u.security.JwtAuthenticationFilter;

/**
 * SecurityConfig
 */
@Configuration

/**
 * Enable web security in a project
 */
@EnableWebSecurity

/**
 * Enable method level security
 */
@EnableGlobalMethodSecurity(
        securedEnabled = true, //enable the @Secured annotation
        jsr250Enabled = true, //enable the @RolesAllowed annotation
        prePostEnabled = true //enable more complex expression
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.api.version}")
    String apiVersion;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void configure(final AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // PasswordEncoder passwordEncoder =     PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // return passwordEncoder;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .cors()
                .and()
            .csrf()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers("/",
                    // h2 db
                    "/h2-console/**",
                    //swagger
                    "/swagger**", 
                    "/v2/api-docs", 
                    "/swagger-resources", 
                    "/swagger-resources/configuration/ui", 
                    "/swagger-resources/configuration/security",
                    "/webjars/springfox-swagger-ui/fonts/**",
                    "/favicon.ico",
                    "/**/*.png",
                    "/**/*.gif",
                    "/**/*.svg",
                    "/**/*.jpg",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js")
                    .permitAll()
                .antMatchers(apiVersion + "/auth", apiVersion + "/auth/**")
                    .permitAll()
                .antMatchers(apiVersion + "/user/checkUsernameAvailability", apiVersion + "/user/checkEmailAvailability")
                    .permitAll()
                .anyRequest()
                    .authenticated();
        
        http.headers().frameOptions().disable();
        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() 
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        //configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        //configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}