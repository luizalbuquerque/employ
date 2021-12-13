package com.paguemob.repository;

import com.paguemob.entity.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findByNameContains(String name, Pageable pageable);

    List<Company> findByIndustryContains(String industry, Pageable pageable);

    List<Company> findByNameContainsAndIndustryContains(String name, String industry, Pageable pageable);

    @Query(value = "SELECT DISTINCT c.industry FROM Company c")
    List<String> findAllIndustriesDistinct();
}
