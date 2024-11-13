//package com.mykare.client_registeration;
//
///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.annotation.Order;
//import org.springframework.core.env.Environment;
//import org.springframework.lang.Nullable;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.gtl.ws.constants.IConstant.INVALID_REQIEST;
//import static com.gtl.ws.constants.IConstant.UNAUTHORIZED_401;
//
///**
// * @author neens
// */
////@Component
//@Order(1)
//@Log4j2
//public class PageUrlFilter implements Filter {
//
//    @Autowired
//    CommonUtil commonUtil;
//    @Autowired
//    TokenUtil tokenUtil;
//    @Value("${access-control-allow-credentials}")
//    private Boolean access_control_allow_credentials;
//    @Value("${access-control-allow-methods}")
//    private String access_control_allow_methods;
//    @Value("${access-control-max-age}")
//    private String access_control_max_age;
//    @Value("${access-control-allow-headers}")
//    private String access_control_allow_headers;
//    @Value("${x-frame-options}")
//    private String x_frame_options;
//    @Value("${permissions-policy}")
//    private String permissionsPolicy;
//    @Value("${content-security-policy}")
//    private String csp;
//    @Value("${allowed.origins}")
//    private String allowedOrigins;
//    @Value("${modify.requests}")
//    private String modifyRequests;
//    @Value("${basic.auth.username}")
//    private String basicAuthUsername;
//    @Value("${basic.auth.password}")
//    private String basicAuthPassword;
//    private Environment env;
//    @Autowired
//    private RepositoryService repository;
//
////    public PageUrlFilter(List<String> allowedOriginsList) {
////        this.allowedOriginsList = Arrays.asList(allowedOrigins.split(","));
////    }
//
//    public PageUrlFilter(Environment env) {
//        this.env = env;
//    }
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//        final String TOKEN_MISSING = "Jwt validation failed: missing required token";
//        final String INVALID_SIGNATURE = "Jwt validation failed: invalid signature";
//        final String TOKEN_EXPIRED = "Jwt validation failed: access token us expired";
//        final String CLAIM_MISSING = "Jwt validation failed: missing required claim";
//        final String CLAIM_INCORRECT = "Jwt validation failed: required claim has incorrect value";
//
////        response.setHeader("SET-COOKIE", "JSESSIONID=" + request.getSession().getId() +
////                ";HttpOnly" + ";path=" + request.getRequestURI() + ";SameSite=None;Secure;");
//
//        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//        response.setHeader("Pragma", "no-cache");
//        response.setDateHeader("Expires", -1);
//        response.setHeader("X-Content-Type-Options", "nosniff");
//        response.setHeader("Referrer-Policy", "same-origin");
//        response.setHeader("Permissions-Policy", permissionsPolicy);
//        response.setHeader("X-Frame-Options", x_frame_options);
//        response.setHeader("Content-Security-Policy", csp);
//        response.setHeader("Server", "CustomServer");
//        response.setHeader("Access-Control-Allow-Origin", checkOrigin(request.getHeader("Origin")));
//        log.info("Origin::::::::::::::::" + checkOrigin(request.getHeader("Origin")));
//        response.setHeader("Access-Control-Allow-Credentials", access_control_allow_credentials.toString());
//        response.setHeader("Access-Control-Allow-Methods", access_control_allow_methods);
//        response.setHeader("Access-Control-Max-Age", access_control_max_age);
//        response.setHeader("Access-Control-Allow-Headers", access_control_allow_headers);
//
//        String requestURI = request.getRequestURI();
//        log.info("Starting a transaction for req : {}", request.getRequestURI());
//
//        List<String> specificPaths = Arrays.asList(modifyRequests.split(","));
//        if (specificPaths.contains(requestURI)) {
//            String mediaTypeParam = request.getParameter("mediaType");
//            if (mediaTypeParam == null) {
//                // Parameter is not present, add "mediaType=json" parameter to the request
//                String queryString = request.getQueryString();
//                if (queryString == null) {
//                    queryString = "mediaType=json";
//                } else {
//                    queryString += "&mediaType=json";
//                }
//                String modifiedURL = request.getRequestURI() + "?" + queryString;
//                log.info("modifiedURL" + modifiedURL);
//                req.getRequestDispatcher(modifiedURL).forward(req, res);
//                return;
//            }
//        }
//
//        if (request.getMethod().equals("OPTIONS")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        if (requestURI.contains("/admin")) {
//            String jwtToken = null;
//            final String requestTokenHeader = request.getHeader("Authorization");
//            log.info("Token :" + requestTokenHeader);
//            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//                jwtToken = requestTokenHeader.substring(7);
//                if (jwtToken != null) {
//                    try {
//                        Jws<Claims> claims = tokenUtil.validateAccessToken(jwtToken);
//                        log.info("ThirdPartyAuthenticationFilter : doFilter : Token is Valid!");
//                        log.debug("Claims " + claims);
//                        chain.doFilter(request, response);
//                        return;
//                    } catch (SignatureException ex) {
//                        log.warn("Jwt validation failed: invalid signature");
//                        response.sendError(UNAUTHORIZED_401, INVALID_SIGNATURE);
//                    } catch (ExpiredJwtException ex) {
//                        log.warn("Jwt validation failed: access token has expired");
//                        response.sendError(UNAUTHORIZED_401, TOKEN_EXPIRED);
//                    } catch (MissingClaimException ex) {
//                        log.warn("Jwt validation failed: missing required claim");
//                        response.sendError(UNAUTHORIZED_401, CLAIM_MISSING);
//                    } catch (IncorrectClaimException ex) {
//                        log.warn("Jwt validation failed: required claim has incorrect value");
//                        response.sendError(UNAUTHORIZED_401, CLAIM_INCORRECT);
//                    } catch (Exception e) {
//                        log.error(" doFilter : Exception ", e);
//                        response.sendError(UNAUTHORIZED_401, INVALID_REQIEST);
//                    }
//                } else {
//                    log.warn("JWT Token does not begin with Bearer String");
//                    response.sendError(UNAUTHORIZED_401, TOKEN_MISSING);
//                }
//
//
//            } else {
//                log.warn("JWT Token does not begin with Bearer String");
//                response.sendError(UNAUTHORIZED_401, TOKEN_MISSING);
//            }
//
//        } else if (requestURI.contains("/users")) {
//            String username = request.getHeader("X-Username");
//            String password = request.getHeader("X-Password");
//
//            // Check if the username and password match the values defined in application properties
//            if (basicAuthUsername.equals(username) && basicAuthPassword.equals(password)) {
//                // User is authenticated, allow access
//                chain.doFilter(request, res);
//            } else {
//                log.warn("Unauthorized access");
//                // User is not authenticated, deny access
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Unauthorized");
//            }
//        } else {
//            chain.doFilter(request, res);
//        }
//    }
//
//    @Nullable
//    public String checkOrigin(@Nullable String requestOrigin) {
//        List<String> allowedOriginsList = Arrays.asList(allowedOrigins.split(","));
//        if (!StringUtils.hasText(requestOrigin)) {
//            return null;
//        }
//        if (ObjectUtils.isEmpty(this.allowedOrigins)) {
//            return null;
//        }
//        if (this.allowedOrigins.contains("*")) {
//            if (this.access_control_allow_credentials != Boolean.TRUE) {
//                return "*";
//            } else {
//                return requestOrigin;
//            }
//        }
//        for (String allowedOrigin : allowedOriginsList) {
//            if (requestOrigin.equalsIgnoreCase(allowedOrigin)) {
//                log.info("Origin Success");
//                return requestOrigin;
//            }
//        }
//        return null;
//    }
//}
