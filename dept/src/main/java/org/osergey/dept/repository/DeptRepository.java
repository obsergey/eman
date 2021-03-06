package org.osergey.dept.repository;

import org.osergey.dept.domain.Dept;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty("dept.micro.service")
public interface DeptRepository extends JpaRepository<Dept, Integer> {
}
