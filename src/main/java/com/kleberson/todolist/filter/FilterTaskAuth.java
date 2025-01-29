package com.kleberson.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.kleberson.todolist.user.UserModel;
import com.kleberson.todolist.user.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if (servletPath.equals("/tasks/create")) {
            String authorization = request.getHeader("Authorization");

            String authEncode = authorization.substring("Basic".length()).trim();

            byte[] authDecode = Base64.getDecoder().decode(authEncode);

            String authString = new String(authDecode);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];
            UserModel user = this.userRepository.findByName(username);
            if (user == null) {
                response.sendError(401, "unauthorized user");
            }else {
                BCrypt.Result passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray());
                if (passwordVerify.verified){
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                }else {
                    response.sendError(401, "unauthorized user");
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
