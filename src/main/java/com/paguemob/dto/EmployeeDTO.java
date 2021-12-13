package com.paguemob.dto;

import com.paguemob.entity.Employee;
import com.paguemob.utils.CpfCnpjUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Schema(description = "Information that composes an employee.", name = "Employee")
public class EmployeeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idEmployee;

    @NotBlank(message = "employee.validation.name")
    private String name;

    @NotBlank(message = "employee.validation.gender")
    private String gender;

    @NotBlank(message = "employee.validation.email")
    @Email(message = "employee.validation.email")
    private String email;

    @NotBlank(message = "employee.validation.cpf")
    @CPF(message = "employee.validation.cpf")
    private String cpf;

    @NotNull(message = "employee.validation.employer")
    private Long idEmployer;

    @NotBlank(message = "employee.validation.jobTitle")
    private String jobTitle;

    @NotBlank(message = "employee.validation.seed")
    private String seed;

    public EmployeeDTO() {
    }

    public EmployeeDTO(Employee employee) {
        this.idEmployee = employee.getIdEmployee();
        this.name = employee.getName();
        this.gender = employee.getGender();
        this.email = employee.getEmail();
        this.cpf = CpfCnpjUtils.formatCpf(employee.getCpf());
        this.idEmployer = employee.getEmployer().getIdCompany();
        this.jobTitle = employee.getJobTitle();
        this.seed = employee.getSeed();
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

    public Long getIdEmployer() {
        return idEmployer;
    }

    public void setIdEmployer(Long idEmployer) {
        this.idEmployer = idEmployer;
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
        EmployeeDTO that = (EmployeeDTO) o;
        return Objects.equals(idEmployee, that.idEmployee) && Objects.equals(name, that.name) && gender == that.gender && Objects.equals(email, that.email) && Objects.equals(cpf, that.cpf) && Objects.equals(jobTitle, that.jobTitle) && Objects.equals(seed, that.seed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmployee, name, gender, email, cpf, jobTitle, seed);
    }
}
