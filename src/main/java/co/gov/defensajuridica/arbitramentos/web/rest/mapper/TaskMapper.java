package co.gov.defensajuridica.arbitramentos.web.rest.mapper;

import org.camunda.bpm.engine.task.Task;
import org.mapstruct.Mapper;

import co.gov.defensajuridica.arbitramentos.web.rest.dto.TaskDTO;

/**
 * Mapper for the entity Usuario and its DTO UsuarioDTO.
 */
//@Mapper(componentModel = "spring", uses = {})
public interface TaskMapper {

    TaskDTO taskToTaskDTO(Task task);

    Task taskDTOToTask(TaskDTO taskDTO);
}
