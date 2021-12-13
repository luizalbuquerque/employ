package com.paguemob.service;

import com.paguemob.dto.EmployeeDTO;
import com.paguemob.entity.Company;
import com.paguemob.entity.Employee;
import com.paguemob.exception.BusinessException;
import com.paguemob.repository.CompanyRepository;
import com.paguemob.repository.EmployeeRepository;
import com.paguemob.utils.CpfCnpjUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.paguemob.utils.PageableUtils.getPageable;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final CompanyRepository companyRepository;

    public EmployeeService(EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
    }

    public EmployeeDTO register(EmployeeDTO employeeDTO) {
        validateCpf(employeeDTO);

        Company company = getCompany(employeeDTO);
        Employee employee = new Employee(employeeDTO, company);

        try {
            employeeRepository.save(employee);

            employeeDTO.setIdEmployee(employee.getIdEmployee());

            return employeeDTO;
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("employee.validation.cpfEmail.duplicate");
        }
    }

    private void validateCpf(EmployeeDTO employeeDTO) {
        if (!CpfCnpjUtils.isValidCpf(employeeDTO.getCpf()))
            throw new BusinessException("employee.validation.cpf");
    }

    private Company getCompany(EmployeeDTO employeeDTO) {
        return companyRepository.findById(employeeDTO.getIdEmployer())
                .orElseThrow(() -> new BusinessException("employee.validation.employerNotFound"));
    }

    public List<EmployeeDTO> getAllEmployees(Integer page, Integer size, String jobTitle) {
        Pageable pageable = getPageable(page, size, "idEmployee");
        List<Employee> employees;

        if (ObjectUtils.isEmpty(jobTitle))
            employees = employeeRepository.findAll(pageable).getContent();
        else
            employees = employeeRepository.findByJobTitleContains(jobTitle, pageable);

        return employees
                .stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
    }

    public EmployeeDTO findById(Long idEmployee) {
        Employee employee = employeeRepository.findById(idEmployee).orElse(null);
        if (employee != null)
            return new EmployeeDTO(employee);
        return null;
    }
}
