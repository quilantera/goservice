package com.soulcode.goserviceapp.domain;

import com.soulcode.goserviceapp.domain.enums.Perfil;
import jakarta.persistence.Entity;

@Entity
public class Administrador extends Usuario {

    public Administrador() {
        super();
        setPerfil(Perfil.ADMIN);
    }

    public Administrador(Long id, String nome, String email, String senha, Perfil perfil, Boolean habilitado) {
        super(id, nome, email, senha, perfil, habilitado);
    }

    public Administrador(Long id, String nome, String email, String senha, Boolean habilitado, Perfil perfil, String urlFoto) {
        super(id, nome, email, senha, habilitado, perfil, urlFoto);
    }
}
