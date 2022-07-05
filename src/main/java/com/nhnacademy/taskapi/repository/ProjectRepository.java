package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(
        "SELECT p " +
        "FROM Project p " +
        "   LEFT JOIN ProjectMember m " +
        "      ON p.id = m.project.id " +
        "WHERE m.username = :username"
    )
    List<Project> findProjectByUsername(String username);
}
