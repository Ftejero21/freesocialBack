package com.FreeSocial.com.V.O.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Table(name = "ROL")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Override
    public String getAuthority() {
        return nombre;
    }
}
