package co.gov.defensajuridica.arbitramentos.web.rest;

import com.codahale.metrics.annotation.Timed;
import co.gov.defensajuridica.arbitramentos.domain.Usuario;
import co.gov.defensajuridica.arbitramentos.repository.UsuarioRepository;
import co.gov.defensajuridica.arbitramentos.repository.search.UsuarioSearchRepository;
import co.gov.defensajuridica.arbitramentos.web.rest.util.HeaderUtil;
import co.gov.defensajuridica.arbitramentos.web.rest.util.PaginationUtil;
import co.gov.defensajuridica.arbitramentos.web.rest.dto.UsuarioDTO;
import co.gov.defensajuridica.arbitramentos.web.rest.mapper.UsuarioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Usuario.
 */
@RestController
@RequestMapping("/api")
public class UsuarioResource {

    private final Logger log = LoggerFactory.getLogger(UsuarioResource.class);

    @Inject
    private UsuarioRepository usuarioRepository;

    @Inject
    private UsuarioMapper usuarioMapper;

    @Inject
    private UsuarioSearchRepository usuarioSearchRepository;

    /**
     * POST  /usuarios -> Create a new usuario.
     */
    @RequestMapping(value = "/usuarios",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuarioDTO) throws URISyntaxException {
        log.debug("REST request to save Usuario : {}", usuarioDTO);
        if (usuarioDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new usuario cannot already have an ID").body(null);
        }
        Usuario usuario = usuarioMapper.usuarioDTOToUsuario(usuarioDTO);
        Usuario result = usuarioRepository.save(usuario);
        usuarioSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/usuarios/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("usuario", result.getId().toString()))
                .body(usuarioMapper.usuarioToUsuarioDTO(result));
    }

    /**
     * PUT  /usuarios -> Updates an existing usuario.
     */
    @RequestMapping(value = "/usuarios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UsuarioDTO> updateUsuario(@RequestBody UsuarioDTO usuarioDTO) throws URISyntaxException {
        log.debug("REST request to update Usuario : {}", usuarioDTO);
        if (usuarioDTO.getId() == null) {
            return createUsuario(usuarioDTO);
        }
        Usuario usuario = usuarioMapper.usuarioDTOToUsuario(usuarioDTO);
        Usuario result = usuarioRepository.save(usuario);
        usuarioSearchRepository.save(usuario);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("usuario", usuarioDTO.getId().toString()))
                .body(usuarioMapper.usuarioToUsuarioDTO(result));
    }

    /**
     * GET  /usuarios -> get all the usuarios.
     */
    @RequestMapping(value = "/usuarios",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios(Pageable pageable)
        throws URISyntaxException {
        Page<Usuario> page = usuarioRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/usuarios");
        return new ResponseEntity<>(page.getContent().stream()
            .map(usuarioMapper::usuarioToUsuarioDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /usuarios/:id -> get the "id" usuario.
     */
    @RequestMapping(value = "/usuarios/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable Long id) {
        log.debug("REST request to get Usuario : {}", id);
        return Optional.ofNullable(usuarioRepository.findOne(id))
            .map(usuarioMapper::usuarioToUsuarioDTO)
            .map(usuarioDTO -> new ResponseEntity<>(
                usuarioDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /usuarios/:id -> delete the "id" usuario.
     */
    @RequestMapping(value = "/usuarios/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        log.debug("REST request to delete Usuario : {}", id);
        usuarioRepository.delete(id);
        usuarioSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("usuario", id.toString())).build();
    }

    /**
     * SEARCH  /_search/usuarios/:query -> search for the usuario corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/usuarios/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Usuario> searchUsuarios(@PathVariable String query) {
        return StreamSupport
            .stream(usuarioSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
