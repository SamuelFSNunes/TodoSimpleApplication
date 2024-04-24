package com.samuelnunes.todosimple.repositories;

import com.samuelnunes.todosimple.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
//    @Query(value = "SELECT t FROM Task t WHERE t.user.id = :user_id")
//    List<Task> findByUserId(@Param("user_id") Long id);
    List<Task> findByUserId(Long id);
}
