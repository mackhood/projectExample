package com.project.services;


import com.project.exceptions.ProjectNotFoundException;
import com.project.exceptions.UserAlreadyExistsException;
import com.project.exceptions.UserNotFoundException;
import com.project.model.Project;
import com.project.model.UserProject;
import com.project.repositories.IUserProjectRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProjectService {
    final private IUserProjectRepository userProjectRepository;


    public UserProjectService(IUserProjectRepository userProjectRepository) {
        this.userProjectRepository = userProjectRepository;
    }

    public UserProject findById(Long id) throws UserNotFoundException {
        return userProjectRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id.toString()));
    }

    public List<UserProject> findByName(String name, Pageable paging) throws UserNotFoundException {
        return userProjectRepository.findByName(name,paging);
    }

    public UserProject findByEmail(String email) throws UserNotFoundException {
        return userProjectRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public UserProject findByNameAndEmail(String name,String email) throws UserNotFoundException {
        return userProjectRepository.findByNameAndEmail(name,email).orElseThrow(() -> new UserNotFoundException(name +" "+email));
    }

    public List<UserProject> findALlUsers(Pageable paging) throws UserNotFoundException {
        return userProjectRepository.findAll(paging).getContent();
    }

    public UserProject createUserProject(UserProject userProject) throws UserAlreadyExistsException {

        if(userProjectRepository.findByEmail(userProject.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(userProject.getEmail());
        }
        userProjectRepository.save(userProject);
        return userProject;
    }

    public UserProject updateUserProject(Long id,UserProject newUserProject) throws UserNotFoundException, UserAlreadyExistsException {
        UserProject userProject = userProjectRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id.toString()));
        userProject.setName(newUserProject.getName());

        if (userProjectRepository.findByEmail(newUserProject.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(userProject.getEmail());
        }
        userProject.setEmail(newUserProject.getEmail());
        userProjectRepository.save(userProject);
        return userProject;
    }

    public void deleteUser(Long userId) {
        UserProject userProject = userProjectRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));

        userProjectRepository.delete(userProject);
    }
}
