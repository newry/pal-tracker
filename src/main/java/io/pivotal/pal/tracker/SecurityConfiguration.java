package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/time-entries-jpa**");
        super.configure(web);
    }

    protected void configure(HttpSecurity http) throws Exception {
        String forceHttps = System.getenv("SECURITY_FORCE_HTTPS");
        if (forceHttps != null && forceHttps.equals("true")) {
            http.requiresChannel().anyRequest().requiresSecure();
        }
        String authRequired = System.getenv("AUTH_REQUIRED");
        if(Boolean.valueOf(authRequired)) {
            http
                    .authorizeRequests().antMatchers("/**").hasRole("USER")
                    .and()
                    .httpBasic()
                    .and()
                    .csrf().disable();
        }

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }
}
