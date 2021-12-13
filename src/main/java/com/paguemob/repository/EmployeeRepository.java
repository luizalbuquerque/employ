package com.paguemob.repository;

import com.paguemob.entity.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByEmployerIdCompany(Long idCompany);

    List<Employee> findByJobTitleContains(String jobTitle, Pageable pageable);
}
