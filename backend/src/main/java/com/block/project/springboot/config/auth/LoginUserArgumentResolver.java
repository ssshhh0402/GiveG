package com.block.project.springboot.config.auth;

import com.block.project.springboot.domain.user.SocialType;
import com.block.project.springboot.domain.user.User;
import com.block.project.springboot.domain.user.UserRepository;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;
import java.util.*;

import static com.block.project.springboot.domain.user.Role.USER;
import static com.block.project.springboot.domain.user.SocialType.*;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private UserRepository userRepository;

    public LoginUserArgumentResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean supportsParameter(MethodParameter parameter){
        return parameter.getParameterAnnotation(LoginUser.class) != null &&
                parameter.getParameterType().equals(User.class);
    }

    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.
                currentRequestAttributes()).getRequest().getSession();
        User user = (User) session.getAttribute("user");

        return getUser(user, session);
    }

    private User getUser(User user, HttpSession session) {
        if(user == null) {
            try {
                OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken)
                        SecurityContextHolder.getContext().getAuthentication();
                Map<String, Object> map = authentication.getPrincipal().getAttributes();
                User convertUser = convertUser(authentication.getAuthorizedClientRegistrationId(), map);
                user = userRepository.findByEmail(convertUser.getEmail()).orElse(null);
                if (user == null) { user = userRepository.save(convertUser); }
                setRoleIfNotSame(user, authentication, map);
                session.setAttribute("user", user);
            } catch (ClassCastException e) {
                return user;
            }
        }
        return user;
    }


    private User convertUser(String authority, Map<String, Object> map) {
        if(GOOGLE.getValue().equals(authority)) return getModernUser(GOOGLE, map);
        else if(KAKAO.getValue().equals(authority)) return getKaKaoUser(map);
        return null;
    }

    private User getModernUser(SocialType socialType, Map<String, Object> map) {
        return User.builder()
                .name(String.valueOf(map.get("name")))
                .email(String.valueOf(map.get("email")))
                .role(USER)
                .socialType(socialType)
                .build();
    }

    private User getKaKaoUser(Map<String, Object> map) {
        HashMap<String, String> propertyMap = (HashMap<String, String>)(Object) map.get("properties");
        return User.builder()
                .name(propertyMap.get("nickname"))
                .email(String.valueOf(map.get("kaccount_email")))
                .role(USER)
                .socialType(KAKAO)
                .build();
    }

    private void setRoleIfNotSame(User user, OAuth2AuthenticationToken authentication, Map<String, Object> map) {
        if(!authentication.getAuthorities().contains(new
                SimpleGrantedAuthority(user.getSocialType().getRoleType()))) {
            SecurityContextHolder.getContext().setAuthentication(new
                    UsernamePasswordAuthenticationToken(map, "N/A",
                    AuthorityUtils.createAuthorityList(user.getSocialType().
                            getRoleType())));
        }
    }
}
