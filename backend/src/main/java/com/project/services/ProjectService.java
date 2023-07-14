package com.project.services;

import com.project.exceptions.ProjectNotFoundException;
import com.project.exceptions.UserAlreadyExistsException;
import com.project.exceptions.UserAlreadyExistsInProjectException;
import com.project.exceptions.UserNotFoundException;
import com.project.model.Project;
import com.project.model.UserProject;
import com.project.repositories.IProjectRepository;
import com.project.repositories.IUserProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class ProjectService {
    final private IProjectRepository projectRepository;
    final private UserProjectService userProjectService;

    public ProjectService(IProjectRepository projectRepository, UserProjectService userProjectService) {
        this.projectRepository = projectRepository;
        this.userProjectService = userProjectService;
    }

    public List<Project> findProjectByName(String name,Pageable paging) {
        return projectRepository.findByName(name,paging);
    }

    public Project findProjectById(Long id) throws ProjectNotFoundException {
        return projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id.toString()));
    }


    public List<Project> findAllProjects(Pageable paging) throws ProjectNotFoundException {
        return projectRepository.findAll(paging).getContent();
    }


    public Project findProjectByIdAllValues(Long id) throws ProjectNotFoundException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id.toString()));
        project.getUserProjects();
        return project;
    }

    public Project createProject(Project project) {
        projectRepository.save(project);
        return project;
    }

    public void deleteProject(Long id) throws ProjectNotFoundException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id.toString()));
        projectRepository.delete(project);
    }

    public Project updateProject(Long id,Project newProject) throws ProjectNotFoundException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id.toString()));
        project.setDescription(newProject.getDescription());
        project.setName(newProject.getName());
        projectRepository.save(project);
        return project;
    }

    public Project addUserToProject(Long projectId,Long userId) throws ProjectNotFoundException, UserAlreadyExistsInProjectException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId.toString()));
        UserProject userProject = userProjectService.findById(userId);

        if(project.getUserProjects() == null) {
            Set<UserProject> userProjects = new HashSet<>();
            project.setUserProjects(userProjects);
        }

        if(project.getUserProjects().stream().anyMatch(x -> x.getEmail().equals(userProject.getEmail()))) {
            throw new UserAlreadyExistsInProjectException(userProject.getEmail(),projectId.toString());
        }
        project.getUserProjects().add(userProject);
        projectRepository.save(project);
        return project;
    }

    public void deleteUserFromAllProject(Long userId) throws UserNotFoundException, ProjectNotFoundException {
        List<Project> projects = this.findAllProjectsWithUser(userId);

        for(Project project: projects) {
            this.deleteUserFromSpecificProject(project.getId(), userId);
        }

    }


    public void deleteUserFromSpecificProject(Long projectId, Long userId) throws ProjectNotFoundException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId.toString()));
        UserProject userProject = userProjectService.findById(userId);

        project.getUserProjects().remove(userProject);
        projectRepository.save(project);

    }

    public List<Project> findAllProjectsWithUser(Long userId) {
        UserProject userProject = userProjectService.findById(userId);

        return projectRepository.findAllByUserProjects(userProject);
    }
}
