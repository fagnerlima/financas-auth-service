package br.pro.fagnerlima.spring.auth.api.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pro.fagnerlima.spring.auth.api.domain.model.grupo.Grupo;
import br.pro.fagnerlima.spring.auth.api.domain.service.GrupoService;
import br.pro.fagnerlima.spring.auth.api.infrastructure.service.ConverterService;
import br.pro.fagnerlima.spring.auth.api.infrastructure.service.ResponseService;
import br.pro.fagnerlima.spring.auth.api.presentation.dto.ResponseTO;
import br.pro.fagnerlima.spring.auth.api.presentation.dto.grupo.GrupoMinResponseTO;
import br.pro.fagnerlima.spring.auth.api.presentation.dto.grupo.GrupoReducedResponseTO;
import br.pro.fagnerlima.spring.auth.api.presentation.dto.grupo.GrupoResponseTO;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private ConverterService converterService;

    @Autowired
    private ResponseService responseService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_GRUPO_PESQUISAR') and #oauth2.hasScope('read')")
    @GetMapping
    public ResponseEntity<ResponseTO<Page<GrupoReducedResponseTO>>> findAll(Pageable pageable) {
        Page<Grupo> page = grupoService.findAll(pageable);
        Page<GrupoReducedResponseTO> responseTOPage = converterService.convert(page, GrupoReducedResponseTO.class);

        return responseService.ok(responseTOPage);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_GRUPO_BUSCAR') and #oauth2.hasScope('read')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTO<GrupoResponseTO>> find(@PathVariable Long id) {
        Grupo grupo = grupoService.findById(id);
        GrupoResponseTO responseTO = converterService.convert(grupo, GrupoResponseTO.class);

        return responseService.ok(responseTO);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_GRUPO_BUSCAR') and #oauth2.hasScope('read')")
    @GetMapping("/ativos")
    public ResponseEntity<ResponseTO<List<GrupoMinResponseTO>>> findAllActives() {
        List<Grupo> grupos = grupoService.findAllActives();
        List<GrupoMinResponseTO> responseTOList = converterService.convert(grupos, GrupoMinResponseTO.class);

        return responseService.ok(responseTOList);
    }

}
