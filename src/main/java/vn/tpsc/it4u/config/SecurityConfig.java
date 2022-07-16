package vn.tpsc.it4u.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import vn.tpsc.it4u.security.CustomAuthenticationEntryPoint;
import vn.tpsc.it4u.security.CustomUserDetailsService;
import vn.tpsc.it4u.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, // enable the @Secured annotation
		jsr250Enabled = true, // enable the @RolesAllowed annotation
		prePostEnabled = true // enable more complex expression
)
public class SecurityConfig {

	@Value("${app.api.version}")
	String apiVersion;

	@Value("${app.cors.allowedOrigins}")
	private String allowedOrigin;

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	private CustomAuthenticationEntryPoint unauthorizedHandler;

	@Bean
	JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.cors()
				.and()
				// .cors(withDefaults())
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
						// H2 DB
						"/h2-console/**",
						// swagger
						"/swagger-ui/**",
						"/api-docs/**",
						"/api-docs**",
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
				.antMatchers(apiVersion + "/user/checkUsernameAvailability",
						apiVersion + "/user/checkEmailAvailability")
				.permitAll()
				.anyRequest()
				.authenticated();

		http.headers().frameOptions().disable();
		// Add our custom JWT security filter
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		// configuration.addAllowedOrigin(allowedOrigin);
		configuration.addAllowedOriginPattern(allowedOrigin);
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		// configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		// configuration.setAllowedMethods(Arrays.asList("GET","POST"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}