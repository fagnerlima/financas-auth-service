package br.pro.fagnerlima.spring.auth.api.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.pro.fagnerlima.spring.auth.api.application.facade.ResponseEntityFacade;
import br.pro.fagnerlima.spring.auth.api.application.factory.GrupoLinkFactory;
import br.pro.fagnerlima.spring.auth.api.application.factory.UsuarioLinkFactory;
import br.pro.fagnerlima.spring.auth.api.domain.model.usuario.Usuario;
import br.pro.fagnerlima.spring.auth.api.domain.service.UsuarioService;
import br.pro.fagnerlima.spring.auth.api.infrastructure.facade.ModelMapperFacade;
import br.pro.fagnerlima.spring.auth.api.presentation.dto.usuario.UsuarioAutenticadoRequestTO;
import br.pro.fagnerlima.spring.auth.api.presentation.dto.usuario.UsuarioResponseTO;
import br.pro.fagnerlima.spring.auth.api.presentation.dto.usuario.UsuarioSenhaRequestTO;

@RestController
@RequestMapping("/me")
public class UsuarioAutenticadoController {

    private UsuarioService usuarioService;

    private ModelMapperFacade converterService;

    public UsuarioAutenticadoController(UsuarioService usuarioService, ModelMapperFacade converterService) {
        this.usuarioService = usuarioService;
        this.converterService = converterService;
    }

    @GetMapping
    public ResponseEntity<UsuarioResponseTO> find() {
        Usuario usuario = usuarioService.getAutenticado();
        UsuarioResponseTO responseTO = converterService.map(usuario, UsuarioResponseTO.class);

        addLinks(responseTO);

        return ResponseEntityFacade.ok(responseTO);
    }

    @PutMapping
    public ResponseEntity<UsuarioResponseTO> update(@RequestBody UsuarioAutenticadoRequestTO requestTO) {
        Usuario usuario = converterService.map(requestTO, Usuario.class);
        Usuario updatedUsuario = usuarioService.updateAutenticado(usuario);
        UsuarioResponseTO responseTO = converterService.map(updatedUsuario, UsuarioResponseTO.class);

        addLinks(responseTO);

        return ResponseEntityFacade.ok(responseTO);
    }

    @PatchMapping("/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSenha(@RequestBody UsuarioSenhaRequestTO requestTO) {
        usuarioService.updateSenhaAutenticado(requestTO.getSenhaAtual(), requestTO.getSenhaNova());
    }

    private void addLinks(UsuarioResponseTO responseTO) {
        responseTO.add(UsuarioLinkFactory.create(responseTO.getId()));
        responseTO.getGrupos().stream().forEach(grupo -> grupo.add(GrupoLinkFactory.create(grupo.getId())));
    }

}
