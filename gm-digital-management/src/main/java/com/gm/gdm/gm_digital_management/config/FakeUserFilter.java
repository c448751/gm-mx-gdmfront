package com.gm.gdm.gm_digital_management.config;

import com.gm.gdm.gm_digital_management.models.User;
import com.gm.gdm.gm_digital_management.respository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Component
public class FakeUserFilter implements Filter {

    @Autowired
    private UserRepository userRepo;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        // Si no hay usuario logueado, inyectamos uno
        if (session.getAttribute("USER_LOGGED") == null) {
            // Cambia el ID al ID de un usuario real de tu base de datos
            User admin = userRepo.findById(1).orElse(null);

            if (admin != null) {
                session.setAttribute("USER_LOGGED", admin);
            }
        }

        chain.doFilter(request, response);
    }
}
