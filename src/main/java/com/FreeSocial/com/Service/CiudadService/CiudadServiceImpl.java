package com.FreeSocial.com.Service.CiudadService;

import com.FreeSocial.com.Repository.CiudadRepository;
import com.FreeSocial.com.Utils.Mapping;
import com.FreeSocial.com.V.O.DTO.CiudadDTO;
import com.FreeSocial.com.V.O.Entity.CiudadesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.FreeSocial.com.Utils.Mapping.convertEntityToDTO;

@Service
public class CiudadServiceImpl implements CiudadService{


    @Autowired
    private CiudadRepository ciudadRepository;


    @Override
    public List<CiudadDTO> getAllCitys() {
        List<CiudadesEntity> ciudadesEntities = ciudadRepository.findAll();
        List<CiudadDTO> ciudadDTOs = ciudadesEntities.stream()
                .map(Mapping::convertEntityToDTO)
                .collect(Collectors.toList());
        return ciudadDTOs;
    }
}
