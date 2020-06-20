package com.jt.tours.security.config;

import com.jt.tours.security.CsrUserDetailsService;
import com.jt.tours.security.filters.JwtTokenFilter;
import com.jt.tours.security.filters.RestAccessDeniedHandler;
import com.jt.tours.security.filters.RestAuthenticationEntryPoint;
import com.jt.tours.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Web security configuration for the application.
 *
 * Created by Jason Tao on 6/3/2020
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // Default rounds of hash to generate the password using bcrypt.
    private static final Integer HASH_ROUND = 12;

    // Default authority group for a user.
    private static final String DEFAULT_AUTH_GROUP = "CSR_USER";

    private final CsrUserDetailsService csrUserDetailsService;

    private final RestAccessDeniedHandler restAccessDeniedHandler;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private final JwtTokenProvider jwtTokenProvider;

    public WebSecurityConfiguration(CsrUserDetailsService csrUserDetailsService, RestAccessDeniedHandler restAccessDeniedHandler,
                                    RestAuthenticationEntryPoint restAuthenticationEntryPoint, JwtTokenProvider jwtTokenProvider) {
        this.csrUserDetailsService= csrUserDetailsService;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Configures the authentication manager that will be used for authenticating user.
     *
     * @param authManagerBuilder - builder uses for constructing the auth manager.
     * @throws Exception exception arises when configuring the auth manager.
     */

    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {

        authManagerBuilder.authenticationProvider(authenticationProvider());
    }

    /**
     * Defines authorization right to any incoming http request.
     *
     * @param http the http security object for specific http request.
     * @throws Exception exception arises when configuring the HttpSecurity object.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Set entry points to the application
        http.authorizeRequests()
                .antMatchers("/", "/index", "/css/*", "/js/*").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers( "/api/v1/users/login").permitAll()
                // Authenticate everything
                .anyRequest().authenticated();

        // Use basic authentication with username and password thru a browser
        http.httpBasic();

        // Disable CSRF (cross site request forgery)
        http.csrf().disable();

        // Allow h2-console to be displayed.
        http.headers().frameOptions().disable();

        // No session will be created or used by spring security. Session won't store user's state.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Perform post-handling when a user logouts.
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        // define custom handlers to handle authorization and authentication issues.
        http.exceptionHandling().accessDeniedHandler(restAccessDeniedHandler)
                                .authenticationEntryPoint(restAuthenticationEntryPoint);

        // Adding the customized token filter before the UsernamePasswordAuthenticationFilter class.
        http.addFilterBefore(new JwtTokenFilter(csrUserDetailsService, jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Allow swagger to be accessed without authentication
        web.ignoring().antMatchers("/v2/api-docs")//
                .antMatchers("/swagger-resources/**")//
                .antMatchers("/swagger-ui.html")//
                .antMatchers("/configuration/**")//
                .antMatchers("/webjars/**")//
                .antMatchers("/public");
    }

    /**
     * Define the AuthenticationManager as bean to be exposed to the application.
     *
     * @return the authentication manager bean
     * @throws Exception any exception thrown
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * Processes authentication request when user logs in.
     *
     * @return DaoAuthenticationProvider bean
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(csrUserDetailsService);
        provider.setAuthoritiesMapper(authorityMapper());
        return provider;
    }

    /**
     * A mapper that sets the default authority group as "CSR_USER" if not defined.
     *
     * @return SimpleAuthorityMapper bean
     */
    @Bean
    public SimpleAuthorityMapper authorityMapper() {
        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
        mapper.setConvertToUpperCase(true);
        mapper.setDefaultAuthority(DEFAULT_AUTH_GROUP);
        return mapper;
    }

    /**
     * Define a password encoder bean using BCrypt.
     *
     * @return the password encoder uses for hashing the password.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(HASH_ROUND);
    }
}
