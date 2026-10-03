package com.FreeSocial.com.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtToUserConverter {

    // status 
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


}
