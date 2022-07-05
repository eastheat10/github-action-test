package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMembersRepository extends JpaRepository<ProjectMember, ProjectMember.Pk> {

    boolean existsById_memberId(Long memberId);
}
