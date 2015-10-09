package co.gov.defensajuridica.arbitramentos.web.rest;

import co.gov.defensajuridica.arbitramentos.Application;
import co.gov.defensajuridica.arbitramentos.domain.Proceso;
import co.gov.defensajuridica.arbitramentos.repository.ProcesoRepository;
import co.gov.defensajuridica.arbitramentos.repository.search.ProcesoSearchRepository;
import co.gov.defensajuridica.arbitramentos.web.rest.dto.ProcesoDTO;
import co.gov.defensajuridica.arbitramentos.web.rest.mapper.ProcesoMapper;

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
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ProcesoResource REST controller.
 *
 * @see ProcesoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProcesoResourceTest {

    private static final String DEFAULT_DESCRIPCION_HECHOS = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPCION_HECHOS = "UPDATED_TEXT";

    private static final Integer DEFAULT_ESTADO_ACTUAL_PROCESO = 1;
    private static final Integer UPDATED_ESTADO_ACTUAL_PROCESO = 2;

    private static final LocalDate DEFAULT_FECHA_HECHOS = new LocalDate(0L);
    private static final LocalDate UPDATED_FECHA_HECHOS = new LocalDate();
    private static final String DEFAULT_HECHOS = "SAMPLE_TEXT";
    private static final String UPDATED_HECHOS = "UPDATED_TEXT";

    @Inject
    private ProcesoRepository procesoRepository;

    @Inject
    private ProcesoMapper procesoMapper;

    @Inject
    private ProcesoSearchRepository procesoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProcesoMockMvc;

    private Proceso proceso;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProcesoResource procesoResource = new ProcesoResource();
        ReflectionTestUtils.setField(procesoResource, "procesoRepository", procesoRepository);
        ReflectionTestUtils.setField(procesoResource, "procesoMapper", procesoMapper);
        ReflectionTestUtils.setField(procesoResource, "procesoSearchRepository", procesoSearchRepository);
        this.restProcesoMockMvc = MockMvcBuilders.standaloneSetup(procesoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        proceso = new Proceso();
        proceso.setDescripcionHechos(DEFAULT_DESCRIPCION_HECHOS);
        proceso.setEstadoActualProceso(DEFAULT_ESTADO_ACTUAL_PROCESO);
        proceso.setFechaHechos(DEFAULT_FECHA_HECHOS);
        proceso.setHechos(DEFAULT_HECHOS);
    }

    @Test
    @Transactional
    public void createProceso() throws Exception {
        int databaseSizeBeforeCreate = procesoRepository.findAll().size();

        // Create the Proceso
        ProcesoDTO procesoDTO = procesoMapper.procesoToProcesoDTO(proceso);

        restProcesoMockMvc.perform(post("/api/procesos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(procesoDTO)))
                .andExpect(status().isCreated());

        // Validate the Proceso in the database
        List<Proceso> procesos = procesoRepository.findAll();
        assertThat(procesos).hasSize(databaseSizeBeforeCreate + 1);
        Proceso testProceso = procesos.get(procesos.size() - 1);
        assertThat(testProceso.getDescripcionHechos()).isEqualTo(DEFAULT_DESCRIPCION_HECHOS);
        assertThat(testProceso.getEstadoActualProceso()).isEqualTo(DEFAULT_ESTADO_ACTUAL_PROCESO);
        assertThat(testProceso.getFechaHechos()).isEqualTo(DEFAULT_FECHA_HECHOS);
        assertThat(testProceso.getHechos()).isEqualTo(DEFAULT_HECHOS);
    }

    @Test
    @Transactional
    public void checkDescripcionHechosIsRequired() throws Exception {
        int databaseSizeBeforeTest = procesoRepository.findAll().size();
        // set the field null
        proceso.setDescripcionHechos(null);

        // Create the Proceso, which fails.
        ProcesoDTO procesoDTO = procesoMapper.procesoToProcesoDTO(proceso);

        restProcesoMockMvc.perform(post("/api/procesos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(procesoDTO)))
                .andExpect(status().isBadRequest());

        List<Proceso> procesos = procesoRepository.findAll();
        assertThat(procesos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaHechosIsRequired() throws Exception {
        int databaseSizeBeforeTest = procesoRepository.findAll().size();
        // set the field null
        proceso.setFechaHechos(null);

        // Create the Proceso, which fails.
        ProcesoDTO procesoDTO = procesoMapper.procesoToProcesoDTO(proceso);

        restProcesoMockMvc.perform(post("/api/procesos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(procesoDTO)))
                .andExpect(status().isBadRequest());

        List<Proceso> procesos = procesoRepository.findAll();
        assertThat(procesos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHechosIsRequired() throws Exception {
        int databaseSizeBeforeTest = procesoRepository.findAll().size();
        // set the field null
        proceso.setHechos(null);

        // Create the Proceso, which fails.
        ProcesoDTO procesoDTO = procesoMapper.procesoToProcesoDTO(proceso);

        restProcesoMockMvc.perform(post("/api/procesos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(procesoDTO)))
                .andExpect(status().isBadRequest());

        List<Proceso> procesos = procesoRepository.findAll();
        assertThat(procesos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProcesos() throws Exception {
        // Initialize the database
        procesoRepository.saveAndFlush(proceso);

        // Get all the procesos
        restProcesoMockMvc.perform(get("/api/procesos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(proceso.getId().intValue())))
                .andExpect(jsonPath("$.[*].descripcionHechos").value(hasItem(DEFAULT_DESCRIPCION_HECHOS.toString())))
                .andExpect(jsonPath("$.[*].estadoActualProceso").value(hasItem(DEFAULT_ESTADO_ACTUAL_PROCESO)))
                .andExpect(jsonPath("$.[*].fechaHechos").value(hasItem(DEFAULT_FECHA_HECHOS.toString())))
                .andExpect(jsonPath("$.[*].hechos").value(hasItem(DEFAULT_HECHOS.toString())));
    }

    @Test
    @Transactional
    public void getProceso() throws Exception {
        // Initialize the database
        procesoRepository.saveAndFlush(proceso);

        // Get the proceso
        restProcesoMockMvc.perform(get("/api/procesos/{id}", proceso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(proceso.getId().intValue()))
            .andExpect(jsonPath("$.descripcionHechos").value(DEFAULT_DESCRIPCION_HECHOS.toString()))
            .andExpect(jsonPath("$.estadoActualProceso").value(DEFAULT_ESTADO_ACTUAL_PROCESO))
            .andExpect(jsonPath("$.fechaHechos").value(DEFAULT_FECHA_HECHOS.toString()))
            .andExpect(jsonPath("$.hechos").value(DEFAULT_HECHOS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProceso() throws Exception {
        // Get the proceso
        restProcesoMockMvc.perform(get("/api/procesos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProceso() throws Exception {
        // Initialize the database
        procesoRepository.saveAndFlush(proceso);

		int databaseSizeBeforeUpdate = procesoRepository.findAll().size();

        // Update the proceso
        proceso.setDescripcionHechos(UPDATED_DESCRIPCION_HECHOS);
        proceso.setEstadoActualProceso(UPDATED_ESTADO_ACTUAL_PROCESO);
        proceso.setFechaHechos(UPDATED_FECHA_HECHOS);
        proceso.setHechos(UPDATED_HECHOS);
        
        ProcesoDTO procesoDTO = procesoMapper.procesoToProcesoDTO(proceso);

        restProcesoMockMvc.perform(put("/api/procesos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(procesoDTO)))
                .andExpect(status().isOk());

        // Validate the Proceso in the database
        List<Proceso> procesos = procesoRepository.findAll();
        assertThat(procesos).hasSize(databaseSizeBeforeUpdate);
        Proceso testProceso = procesos.get(procesos.size() - 1);
        assertThat(testProceso.getDescripcionHechos()).isEqualTo(UPDATED_DESCRIPCION_HECHOS);
        assertThat(testProceso.getEstadoActualProceso()).isEqualTo(UPDATED_ESTADO_ACTUAL_PROCESO);
        assertThat(testProceso.getFechaHechos()).isEqualTo(UPDATED_FECHA_HECHOS);
        assertThat(testProceso.getHechos()).isEqualTo(UPDATED_HECHOS);
    }

    @Test
    @Transactional
    public void deleteProceso() throws Exception {
        // Initialize the database
        procesoRepository.saveAndFlush(proceso);

		int databaseSizeBeforeDelete = procesoRepository.findAll().size();

        // Get the proceso
        restProcesoMockMvc.perform(delete("/api/procesos/{id}", proceso.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Proceso> procesos = procesoRepository.findAll();
        assertThat(procesos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
