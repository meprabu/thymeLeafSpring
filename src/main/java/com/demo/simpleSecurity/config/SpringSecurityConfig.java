package com.demo.simpleSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	AccessDeniedHandler accessDeniedHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		
		http.csrf().disable()
					.authorizeRequests()
						.antMatchers("/", "/home", "/about").permitAll()
						.antMatchers("/admin/**").hasAnyRole("ADMIN")
						.antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
						.anyRequest().authenticated()
					.and()
					.formLogin()
						.loginPage("/login")
						.permitAll()
						.and()
					.logout()
						.permitAll().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
						.and()
					.exceptionHandling().accessDeniedHandler(accessDeniedHandler);	
		
	}
	
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		
		auth.inMemoryAuthentication()
			.withUser("user").password("{noop}password").roles("USER")
			.and()
			.withUser("admin").password("{noop}password").roles("ADMIN");
		
	}
	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/webjars/**");
		web.ignoring().antMatchers("/css/**","/fonts/**","/libs/**");
	}
	
	

}
