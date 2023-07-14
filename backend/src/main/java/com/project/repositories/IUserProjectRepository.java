package com.project.repositories;

import com.project.model.UserProject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUserProjectRepository extends JpaRepository<UserProject, Long> {
    Optional<UserProject> findById(Long id);
    List<UserProject> findByName(String name,Pageable paging);
    Optional<UserProject> findByEmail(String email);
    Optional<UserProject> findByNameAndEmail(String name,String email);
}
