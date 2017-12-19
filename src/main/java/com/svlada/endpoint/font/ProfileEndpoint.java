package com.svlada.endpoint.font;

import com.svlada.security.auth.JwtAuthenticationToken;
import com.svlada.security.model.UserContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * End-point for retrieving logged-in component details.
 * 
 * @author vladimir.stankovic
 *
 * Aug 4, 2016
 */
@RestController
@RequestMapping(value="api/font/")
public class ProfileEndpoint {

//    @PreAuthorize("hasAnyAuthority('ROLE_PREMIUM_MEMBER')")
    @RequestMapping(value="/premium_member", method=RequestMethod.GET)
    public @ResponseBody UserContext premium_member(JwtAuthenticationToken token) {
        return (UserContext) token.getPrincipal();
    }

//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(value="/admin", method=RequestMethod.GET)
    public @ResponseBody UserContext admin(JwtAuthenticationToken token) {
        return (UserContext) token.getPrincipal();
    }


//    @PreAuthorize("hasAnyAuthority('ROLE_MEMBER')")
    @RequestMapping(value="/member", method=RequestMethod.GET)
    public @ResponseBody UserContext member(JwtAuthenticationToken token) {
        return (UserContext) token.getPrincipal();
    }


}
