package com.project.controllers;

import com.project.Dtos.UserProjectDto;
import com.project.config.ModelMapperProject;
import com.project.exceptions.BadRequestException;
import com.project.exceptions.ProjectNotFoundException;
import com.project.exceptions.UserAlreadyExistsException;
import com.project.exceptions.UserNotFoundException;
import com.project.model.UserProject;
import com.project.services.ProjectService;
import com.project.services.UserProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
public class UserProjectController {

    @Autowired
    UserProjectService userProjectService;

    @Autowired
    ProjectService projectService;

    @Autowired
    ModelMapperProject modelMapper;


    @GetMapping(path="/users", produces = "application/json")
    public List<UserProjectDto> getAllUsers(@RequestParam(defaultValue = "1")  int page,
                                           @RequestParam(defaultValue = "5") int limit) throws ProjectNotFoundException {

        Pageable paging = PageRequest.of(page -1,limit);
        return modelMapper.mapList(userProjectService.findALlUsers(paging),UserProjectDto.class);
    }

    @GetMapping(path="/user", produces = "application/json")
    public List<UserProjectDto> getProjectByNameOrEmailOrBoth(@RequestParam(defaultValue = "1")  int page,
                                       @RequestParam(defaultValue = "5") int limit,
                                       @RequestParam Optional<String> name, @RequestParam Optional<String> email) throws ProjectNotFoundException, BadRequestException {


        List<UserProject> userProjects = new ArrayList<>();
        Pageable paging = PageRequest.of(page -1,limit);

        if(email.isEmpty() && name.isEmpty()) {
            throw new BadRequestException();
        } else if(email.isEmpty()) {
            userProjects = userProjectService.findByName(name.get(),paging);
        } else if(name.isEmpty()) {
            userProjects.add(userProjectService.findByEmail(email.get()));
        } else {
            userProjects.add(userProjectService.findByNameAndEmail(name.get(),email.get()));
        }

        return modelMapper.mapList(userProjects,UserProjectDto.class);
    }

    @GetMapping(path="/user/{id}", produces = "application/json")
    public UserProjectDto getUserProjectById(@PathVariable  Long id) throws ProjectNotFoundException, BadRequestException {

        return modelMapper.map(userProjectService.findById(id), UserProjectDto.class);
    }



    @PostMapping(path="/user", produces = "application/json")
    public UserProjectDto createUserProject(@RequestBody UserProjectDto userProjectDto) throws BadRequestException, UserAlreadyExistsException {
        validateUserProjectParam(userProjectDto);
        return modelMapper.map(userProjectService.createUserProject(modelMapper.map(userProjectDto, UserProject.class)),UserProjectDto.class);
    }

    private void validateUserProjectParam(UserProjectDto userProjectDto) throws BadRequestException {
        if ((userProjectDto.getName() == null || userProjectDto.getName().isBlank())
                && (userProjectDto.getEmail() == null || userProjectDto.getEmail().isBlank())
                && (validateFormatEmail(userProjectDto.getEmail()))) {
            throw new BadRequestException();
        }
    }

    private boolean validateFormatEmail(String email) {
        String regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }


    @DeleteMapping(path="/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteUserProject(@PathVariable Long id) throws UserNotFoundException, ProjectNotFoundException {

        projectService.deleteUserFromAllProject(id);
        userProjectService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}")
    public UserProjectDto modifyUserProject(@Valid @RequestBody UserProjectDto userProjectModified, @PathVariable Long id) throws ProjectNotFoundException, BadRequestException, UserAlreadyExistsException {
        validateUserProjectParam(userProjectModified);
        return  modelMapper.map(userProjectService.updateUserProject(id,modelMapper.map(userProjectModified,UserProject.class)),UserProjectDto.class);
    }

}
