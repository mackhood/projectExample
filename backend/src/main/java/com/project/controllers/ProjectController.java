package com.project.controllers;

import com.project.Dtos.ProjectDto;
import com.project.config.ModelMapperProject;
import com.project.exceptions.BadRequestException;
import com.project.exceptions.ProjectNotFoundException;
import com.project.exceptions.UserAlreadyExistsInProjectException;
import com.project.exceptions.UserNotFoundException;
import com.project.model.Project;
import com.project.services.ProjectService;
import com.project.services.UserProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
public class ProjectController {


    @Autowired
    ProjectService projectService;

    @Autowired
    private ModelMapperProject modelMapper;

    @GetMapping(path="/projects", produces = "application/json")
    public List<Project> getAllProjects(@RequestParam(defaultValue = "1")  int page,
                                        @RequestParam(defaultValue = "5") int limit) throws ProjectNotFoundException {

        Pageable paging = PageRequest.of(page -1,limit);
        //return modelMapper.mapList(projectService.findAllProjects(paging),ProjectDto.class);
        return projectService.findAllProjects(paging);
    }

    @GetMapping(path="/project", produces = "application/json")
    public List<ProjectDto> getProject(@RequestParam(defaultValue = "1")  int page,
                                        @RequestParam(defaultValue = "5") int limit,
                                       @RequestParam String name) throws ProjectNotFoundException {

        Pageable paging = PageRequest.of(page -1,limit);
        projectService.findProjectByName(name,paging);
        return modelMapper.mapList(projectService.findAllProjects(paging),ProjectDto.class);
    }

    @GetMapping(path="/project/full/{id}", produces = "application/json")
    public ProjectDto getProjectFull(@PathVariable  Long id) throws ProjectNotFoundException {
        return modelMapper.map(projectService.findProjectByIdAllValues(id),ProjectDto.class);
    }

    @GetMapping(path="/project/{id}", produces = "application/json")
    public ProjectDto getProject(@PathVariable  Long id) throws ProjectNotFoundException {
        return modelMapper.map(projectService.findProjectById(id),ProjectDto.class);
    }

    @PostMapping(path="/project", produces = "application/json")
    public ProjectDto createProject(@RequestBody ProjectDto projectDto) throws BadRequestException {
        validateProjectParam(projectDto);
        return modelMapper.map(projectService.createProject(modelMapper.map(projectDto, Project.class)),ProjectDto.class);
    }

    private void validateProjectParam(ProjectDto projectDto) throws BadRequestException {
        if (projectDto.getName() == null || projectDto.getName().isBlank()) {
            throw new BadRequestException();
        }
    }


    @DeleteMapping(path="/project/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteProject(@PathVariable Long id) throws ProjectNotFoundException {

        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/projects/{id}")
    public ProjectDto modifyProject(@Valid @RequestBody ProjectDto projectModified, @PathVariable Long id) throws ProjectNotFoundException, BadRequestException {
        validateProjectParam(projectModified);
        return  modelMapper.map(projectService.updateProject(id,modelMapper.map(projectModified,Project.class)),ProjectDto.class);
    }

    @PatchMapping("/project/{projectId}/user/{userId}")
    public ProjectDto modifyProject(@PathVariable Long projectId,@PathVariable Long userId) throws ProjectNotFoundException, UserAlreadyExistsInProjectException {
        return  modelMapper.map(projectService.addUserToProject(projectId,userId),ProjectDto.class);
    }

    @DeleteMapping(path="/project/{projectId}/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteUserFromProject(@PathVariable Long projectId,@PathVariable Long userId) throws UserNotFoundException, ProjectNotFoundException {
        projectService.deleteUserFromSpecificProject(projectId,userId);
        return ResponseEntity.noContent().build();
    }

}
