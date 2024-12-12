package com.ilisi.jobfinder.security;
//
//import com.ilisi.jobfinder.dto.Auth0Request;
//import com.ilisi.jobfinder.dto.UserDTO;
//import com.ilisi.jobfinder.service.AuthService;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final AuthService authentificationService;
//
//    public OAuth2LoginSuccessHandler(AuthService authentificationService) {
//        this.authentificationService = authentificationService;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
//        String googleId = oauthUser.getAttribute("sub");
//        String email = oauthUser.getAttribute("email");
//        String fullName = oauthUser.getAttribute("name");
//        String profilePicture = oauthUser.getAttribute("picture");
//        Auth0Request auth0Request=new Auth0Request(googleId,email,profilePicture);
//
//
//        UserDTO userDTO = authentificationService.authenticateWithGoogle(auth0Request);
//
//        response.setContentType("application/json");
//        response.getWriter().write("{\"token\": \"" + userDTO.getToken() + "\"}");
//    }
//}