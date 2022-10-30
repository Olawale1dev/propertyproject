package com.example.property.security;

import com.example.property.service.RegistrationService;
import com.example.property.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;


    @Bean
    public RegistrationService registrationService() {
        return new RegistrationService() {

        };
    }

   /* @Bean
    public BlogPostRequestService blogPostRequestService() {
        return new BlogPostRequestService() {

        };
    }*/



    @Bean
    public String string() {
        return new String();
    }

  /*  @Bean
    public CommonsMultipartFile commonsMultipartFile(FileItem fileItem) {
        return new CommonsMultipartFile(fileItem);
    }

    @Bean
    public FileItem fileItem() {
        return new FileItem() {
            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public boolean isInMemory() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] get() {
                return new byte[0];
            }

            @Override
            public String getString(String s) throws UnsupportedEncodingException {
                return null;
            }

            @Override
            public String getString() {
                return null;
            }

            @Override
            public void write(File file) throws Exception {

            }

            @Override
            public void delete() {

            }

            @Override
            public String getFieldName() {
                return null;
            }

            @Override
            public void setFieldName(String s) {

            }

            @Override
            public boolean isFormField() {
                return false;
            }

            @Override
            public void setFormField(boolean b) {

            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                return null;
            }

            @Override
            public FileItemHeaders getHeaders() {
                return null;
            }

            @Override
            public void setHeaders(FileItemHeaders fileItemHeaders) {

            }
        };
    }*/


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userServiceImpl);
        return provider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return  source;
    }


    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(userServiceImpl)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        getHttp()

                .authorizeHttpRequests()
                .antMatchers("api/v1/registration", "/1$/post", "/2$/blog").permitAll()
                .antMatchers("/user").authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .usernameParameter("email")
                .defaultSuccessUrl("http://localhost:3000")
                .permitAll()
                .and()
                .logout().invalidateHttpSession(true)
                .logoutSuccessUrl("/")
                .clearAuthentication(true)
                .permitAll();
        getHttp().csrf().disable();
    }


}
