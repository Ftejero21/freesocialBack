package com.FreeSocial.com.Service.CiudadService;

import com.FreeSocial.com.V.O.DTO.CiudadDTO;

import java.util.List;

public interface CiudadService {

    /**
     * Me retorna todas las ciudades
     * @return
     */
    public List<CiudadDTO> getAllCitys();
}
