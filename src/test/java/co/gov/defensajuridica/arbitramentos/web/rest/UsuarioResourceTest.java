package co.gov.defensajuridica.arbitramentos.web.rest;

import co.gov.defensajuridica.arbitramentos.Application;
import co.gov.defensajuridica.arbitramentos.domain.Usuario;
import co.gov.defensajuridica.arbitramentos.repository.UsuarioRepository;
import co.gov.defensajuridica.arbitramentos.repository.search.UsuarioSearchRepository;
import co.gov.defensajuridica.arbitramentos.web.rest.dto.UsuarioDTO;
import co.gov.defensajuridica.arbitramentos.web.rest.mapper.UsuarioMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the UsuarioResource REST controller.
 *
 * @see UsuarioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UsuarioResourceTest {

    private static final String DEFAULT_EMAIL = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL = "UPDATED_TEXT";
    private static final String DEFAULT_PASSWORD = "SAMPLE_TEXT";
    private static final String UPDATED_PASSWORD = "UPDATED_TEXT";
    private static final String DEFAULT_USERNAME = "SAMPLE_TEXT";
    private static final String UPDATED_USERNAME = "UPDATED_TEXT";

    @Inject
    private UsuarioRepository usuarioRepository;

    @Inject
    private UsuarioMapper usuarioMapper;

    @Inject
    private UsuarioSearchRepository usuarioSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UsuarioResource usuarioResource = new UsuarioResource();
        ReflectionTestUtils.setField(usuarioResource, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(usuarioResource, "usuarioMapper", usuarioMapper);
        ReflectionTestUtils.setField(usuarioResource, "usuarioSearchRepository", usuarioSearchRepository);
        this.restUsuarioMockMvc = MockMvcBuilders.standaloneSetup(usuarioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        usuario = new Usuario();
        usuario.setEmail(DEFAULT_EMAIL);
        usuario.setPassword(DEFAULT_PASSWORD);
        usuario.setUsername(DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    public void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.usuarioToUsuarioDTO(usuario);

        restUsuarioMockMvc.perform(post("/api/usuarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
                .andExpect(status().isCreated());

        // Validate the Usuario in the database
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarios.get(usuarios.size() - 1);
        assertThat(testUsuario.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUsuario.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUsuario.getUsername()).isEqualTo(DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    public void getAllUsuarios() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarios
        restUsuarioMockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())));
    }

    @Test
    @Transactional
    public void getUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc.perform(get("/api/usuarios/{id}", usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get("/api/usuarios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

		int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario
        usuario.setEmail(UPDATED_EMAIL);
        usuario.setPassword(UPDATED_PASSWORD);
        usuario.setUsername(UPDATED_USERNAME);
        
        UsuarioDTO usuarioDTO = usuarioMapper.usuarioToUsuarioDTO(usuario);

        restUsuarioMockMvc.perform(put("/api/usuarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
                .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarios.get(usuarios.size() - 1);
        assertThat(testUsuario.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsuario.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUsuario.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    public void deleteUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

		int databaseSizeBeforeDelete = usuarioRepository.findAll().size();

        // Get the usuario
        restUsuarioMockMvc.perform(delete("/api/usuarios/{id}", usuario.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
