package com.paguemob.service;

import com.paguemob.dto.EmployeeDTO;
import com.paguemob.entity.Company;
import com.paguemob.entity.Employee;
import com.paguemob.exception.BusinessException;
import com.paguemob.repository.CompanyRepository;
import com.paguemob.repository.EmployeeRepository;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(EasyMockExtension.class)
class EmployeeServiceTest extends EasyMockSupport {

    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CompanyRepository companyRepository;

    @BeforeEach
    void setup() {
        employeeService = new EmployeeService(employeeRepository, companyRepository);
    }

    @Test
    void shouldComplainEmptyCpf() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BusinessException businessException = assertThrows(BusinessException.class, () -> employeeService.register(employeeDTO));
        assertEquals("employee.validation.cpf", businessException.getMessage());
    }

    @Test
    void shouldComplainInvalidFormatCnpj() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setCpf("12345");
        BusinessException businessException = assertThrows(BusinessException.class, () -> employeeService.register(employeeDTO));
        assertEquals("employee.validation.cpf", businessException.getMessage());
    }

    @Test
    void shouldRegisterNewEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("Name");
        employeeDTO.setCpf("451.670.980-50");
        employeeDTO.setIdEmployer(1L);
        employeeDTO.setEmail("name@email.com");
        employeeDTO.setGender("male");
        employeeDTO.setJobTitle("Job title");
        employeeDTO.setSeed("seed");

        expect(companyRepository.findById(anyLong())).andReturn(Optional.of(new Company()));
        expect(employeeRepository.save(anyObject())).andReturn(new Employee());

        replayAll();
        EmployeeDTO returnedEmployeeDTO = employeeService.register(employeeDTO);
        verifyAll();

        assertEquals("Name", returnedEmployeeDTO.getName());
        assertEquals("451.670.980-50", returnedEmployeeDTO.getCpf());
        assertEquals(1L, returnedEmployeeDTO.getIdEmployer());
        assertEquals("name@email.com", returnedEmployeeDTO.getEmail());
        assertEquals("male", returnedEmployeeDTO.getGender());
        assertEquals("Job title", returnedEmployeeDTO.getJobTitle());
        assertEquals("seed", returnedEmployeeDTO.getSeed());
    }

    @Test
    void shouldListAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        employee.setEmployer(new Company());
        employees.add(employee);

        expect(employeeRepository.findByJobTitleContains(anyString(), anyObject(Pageable.class))).andReturn(employees);

        replayAll();
        List<EmployeeDTO> employeesDTO = employeeService.getAllEmployees(0, null, "job title");
        verifyAll();

        assertEquals(1, employeesDTO.size());
    }

    @Test
    void shouldFindById() {
        Employee employee = new Employee();
        employee.setEmployer(new Company());
        expect(employeeRepository.findById(anyLong())).andReturn(Optional.of(employee));

        replayAll();
        EmployeeDTO employeeDTO = employeeService.findById(1L);
        verifyAll();

        assertNotNull(employeeDTO);
    }
}
