package com.FreeSocial.com.Controller.CiudadController;

import com.FreeSocial.com.Service.CiudadService.CiudadService;
import com.FreeSocial.com.Utils.FreeSocialConstants;
import com.FreeSocial.com.V.O.DTO.CiudadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = FreeSocialConstants.CIUDADES_URL)
@CrossOrigin(origins = "*")
public class CiudadController {

    @Autowired
    private CiudadService ciudadService;



    @GetMapping(value = FreeSocialConstants.GET_ALL_CITY_URL)
    public List<CiudadDTO> obtenerTodasLasCiudades() {
        return ciudadService.getAllCitys();
    }
}
