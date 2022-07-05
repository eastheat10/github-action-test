package com.nhnacademy.taskapi.repository;

import com.nhnacademy.taskapi.dto.projection.person.PersonDto;
import com.nhnacademy.taskapi.entity.ThePersonInCharge;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<ThePersonInCharge, ThePersonInCharge.Pk> {

    @Query(
        "SELECT p.task.id AS taskId," +
        "          p.id.memberId AS memberId," +
        "          pm.username AS username " +
        "FROM ThePersonInCharge p " +
        "   JOIN ProjectMember pm " +
        "       ON p.id.memberId = pm.id.memberId " +
        "WHERE p.task.id = :taskId ")
    List<PersonDto> findDtoByTaskId(Long taskId);

    List<ThePersonInCharge> findByTaskId(Long taskId);
}
