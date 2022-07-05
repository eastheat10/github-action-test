package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.entity.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findTagsByProject_id(Long projectId);
}
