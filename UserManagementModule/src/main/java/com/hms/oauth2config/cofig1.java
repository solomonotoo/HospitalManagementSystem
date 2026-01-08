//package com.hms.oauth2config;
//
//@Component
//public class Cofig1 extends SavedRequestAwareAuthenticationSuccessHandler {
//
//    private final UserService userService;
//    private final JwtUtils jwtUtils;
//    private final RoleRepository roleRepo;
//
//    @Value("${frontend.url}")
//    private String frontendUrl;
//
//    private String username;
//    private String idAttributeKey;
//
//    private static final Logger logger = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);
//
//    public OAuth2LoginSuccessHandler(UserService userService, JwtUtils jwtUtils, RoleRepository roleRepo) {
//        this.userService = userService;
//        this.jwtUtils = jwtUtils;
//        this.roleRepo = roleRepo;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws ServletException, IOException {
//        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
//
//        if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()) ||
//            "google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
//
//            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
//            Map<String, Object> attributes = principal.getAttributes();
//
//            String email = attributes.getOrDefault("email", "").toString();
//            String name = attributes.getOrDefault("name", "").toString();
//
//            if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
//                username = attributes.getOrDefault("login", "").toString();
//                idAttributeKey = "id";
//            } else if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
//                username = email.split("@")[0];
//                idAttributeKey = "sub";
//            }
//
//            logger.debug("HELLO OAUTH: {} : {} : {}", email, name, username);
//
//            User user = userService.findByEmail(email);
//            if (user == null) {
//                user = createNewUser(email, username, oAuth2AuthenticationToken);
//            }
//
//            List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
//                    .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
//                    .collect(Collectors.toList());
//
//            DefaultOAuth2User oAuth2User = new DefaultOAuth2User(authorities, attributes, idAttributeKey);
//            Authentication securityAuth = createAuthentication(oAuth2User, authorities, oAuth2AuthenticationToken);
//            SecurityContextHolder.getContext().setAuthentication(securityAuth);
//
//            // JWT Token Logic
//            String jwtToken = jwtUtils.generateTokenFromUsername(new UserDetailsImpl(
//                    user.getId(), username, email, null, false, new HashSet<>(authorities)));
//
//            String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
//                    .queryParam("token", jwtToken).build().toUriString();
//
//            this.setAlwaysUseDefaultTargetUrl(true);
//            this.setDefaultTargetUrl(targetUrl);
//        }
//
//        super.onAuthenticationSuccess(request, response, authentication);
//    }
//
//    private User createNewUser(String email, String username, OAuth2AuthenticationToken token) {
//        User newUser = new User();
//        Optional<Role> userRole = roleRepo.findByRoleName(UserRoles.ROLE_USER);
//
//        if (userRole.isPresent()) {
//            newUser.setRolesFromJson(userRole.get());
//        } else {
//            throw new RuntimeException("Default Role Not Found");
//        }
//
//        newUser.setEmail(email);
//        newUser.setUserName(username);
//        newUser.setSignUpMethod(token.getAuthorizedClientRegistrationId());
//
//        return userService.registerUser(newUser);
//    }
//
//    private Authentication createAuthentication(OAuth2User oAuth2User, List<SimpleGrantedAuthority> authorities,
//                                               OAuth2AuthenticationToken token) {
//        return new OAuth2AuthenticationToken(oAuth2User, authorities, token.getAuthorizedClientRegistrationId());
//    }
//}