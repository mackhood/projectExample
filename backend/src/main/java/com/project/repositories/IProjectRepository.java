package com.project.repositories;

import com.project.model.Project;
import com.project.model.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface IProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findById(Long id);
    List<Project> findByName(String name, Pageable paging);
    List<Project> findAllByUserProjects(UserProject user);

}
