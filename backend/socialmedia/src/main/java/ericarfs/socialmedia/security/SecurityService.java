package ericarfs.socialmedia.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public String getUsername() {
        var context = SecurityContextHolder.getContext();
        if (context == null) {
            return "anonymousUser";
        }

        var authentication = context.getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return "anonymousUser";
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        return principal.toString();
    }

    public boolean authenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AUTHENTICATION:" + authentication);
        return authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(getUsername());
    }
}
