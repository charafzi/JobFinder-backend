package com.ilisi.jobfinder.security;

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
//
//        // Définir un rôle par défaut (par exemple Role.CANDIDAT) ou récupérer un rôle spécifique
//        Role role = Role.CANDIDAT;  // À ajuster en fonction de votre logique ou de l'attribut dans OAuth2User
//
//        // Créer l'objet Auth0Request avec le rôle
//        Auth0Request auth0Request = new Auth0Request(googleId, email, fullName, profilePicture, role);
//
//        // Appeler le service d'authentification pour générer le UserDTO
//        UserDTO userDTO = authentificationService.authenticateWithGoogle(auth0Request);
//
//        // Retourner le token JWT dans la réponse
//        response.setContentType("application/json");
//        response.getWriter().write("{\"token\": \"" + userDTO.getToken() + "\"}");
//    }
//}
