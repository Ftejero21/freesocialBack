package com.FreeSocial.com.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtToUserConverter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


}
