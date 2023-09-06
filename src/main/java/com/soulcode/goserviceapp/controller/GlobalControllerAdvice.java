package com.soulcode.goserviceapp.controller;

import com.soulcode.goserviceapp.domain.Usuario;
import com.soulcode.goserviceapp.service.UsuarioService;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoAutenticadoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    UsuarioService usuarioService;
    @ModelAttribute("commonAttribute")
    public void addCommonAttribute(Authentication authentication, Model model) {
        try {
            Usuario usuario = usuarioService.findAuthenticated(authentication);
            System.err.println(usuario.getUrlFoto());
            model.addAttribute("urlFoto", usuario.getUrlFoto() );
        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Erro ao buscar dados do usuario.");
        }
    }

}