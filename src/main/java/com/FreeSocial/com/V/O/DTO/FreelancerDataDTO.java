package com.FreeSocial.com.V.O.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerDataDTO {
    private List<String> habilidades;
    private List<String> habilidadesPrincipales;
    private List<String> intereses;
}
