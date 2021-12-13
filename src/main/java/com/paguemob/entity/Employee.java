package com.paguemob.entity;

import com.paguemob.dto.EmployeeDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "employee")
@SequenceGenerator(name="employee_seq", sequenceName="employee_sequence", allocationSize = 1)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="employee_seq")
    private Long idEmployee;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String cpf;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_employer")
    private Company employer;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String seed;

    public Employee() {
    }

    public Employee(EmployeeDTO employeeDTO, Company employer) {
        this.name = employeeDTO.getName();
        this.gender = employeeDTO.getGender();
        this.email = employeeDTO.getEmail();
        this.cpf = employeeDTO.getCpf();
        this.employer = employer;
        this.jobTitle = employeeDTO.getJobTitle();
        this.seed = employeeDTO.getSeed();
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Company getEmployer() {
        return employer;
    }

    public void setEmployer(Company employer) {
        this.employer = employer;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(idEmployee, employee.idEmployee) && Objects.equals(name, employee.name) && Objects.equals(gender, employee.gender) && Objects.equals(email, employee.email) && Objects.equals(cpf, employee.cpf) && Objects.equals(employer, employee.employer) && Objects.equals(jobTitle, employee.jobTitle) && Objects.equals(seed, employee.seed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmployee, name, gender, email, cpf, employer, jobTitle, seed);
    }
}
