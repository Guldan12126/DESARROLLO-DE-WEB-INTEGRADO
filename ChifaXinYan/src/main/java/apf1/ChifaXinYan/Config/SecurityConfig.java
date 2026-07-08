package apf1.ChifaXinYan.Config;

import apf1.ChifaXinYan.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración principal de Spring Security.
 *
 * Flujo de autenticación con base de datos:
 *   1. Usuario envía email + contraseña
 *   2. AuthenticationManager delega a DaoAuthenticationProvider
 *   3. DaoAuthenticationProvider llama a CustomUserDetailsService.loadUserByUsername()
 *   4. Se obtiene el usuario de la BD con su contraseña BCrypt
 *   5. BCryptPasswordEncoder verifica la contraseña ingresada vs la almacenada
 *   6. Si coincide → autenticado; si no → acceso denegado
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * BCrypt es el algoritmo recomendado para almacenar contraseñas.
     * Genera un hash diferente cada vez (salt aleatorio) y es resistente a ataques de fuerza bruta.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Proveedor de autenticación basado en base de datos (DAO = Data Access Object).
     * Une nuestro UserDetailsService con el PasswordEncoder.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * AuthenticationManager es el punto de entrada para autenticar usuarios
     * desde nuestro AuthController (REST API).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define las reglas de seguridad HTTP:
     * - Qué rutas son públicas
     * - Qué rutas requieren autenticación
     * - Configuración del formulario de login
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())

            // Deshabilitamos CSRF para la API REST (en apps con SPA/frontend separado)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**", "/h2-console/**")
            )

            // Reglas de autorización por URL
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas: login API, consola H2, recursos estáticos
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/", "/menu", "/css/**", "/js/**", "/images/**").permitAll()

                // Solo ADMIN puede gestionar usuarios
                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                // ADMIN y COCINA pueden ver pedidos
                .requestMatchers("/api/pedidos/**").hasAnyRole("ADMIN", "COCINA")

                // Cualquier usuario autenticado puede acceder al dashboard
                .requestMatchers("/dashboard").authenticated()

                // Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )

            // Formulario de login de Spring Security (accesible en /login)
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )

            // Logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )

            // Necesario para que la consola H2 funcione (usa iframes)
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}
