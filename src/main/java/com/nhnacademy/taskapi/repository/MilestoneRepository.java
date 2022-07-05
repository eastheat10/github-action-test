package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.Milestone;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

    List<Milestone> findMilestoneByProject_Id(Long projectId);
}
