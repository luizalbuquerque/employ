package com.paguemob.service;


import com.paguemob.dto.CompanyDTO;
import com.paguemob.dto.EmployeeDTO;
import com.paguemob.entity.Company;
import com.paguemob.entity.Employee;
import com.paguemob.enums.IndustryEnum;
import com.paguemob.exception.BusinessException;
import com.paguemob.repository.CompanyRepository;
import com.paguemob.repository.EmployeeRepository;
import org.easymock.EasyMockExtension;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(EasyMockExtension.class)
class CompanyServiceTest extends EasyMockSupport {

    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setup() {
        companyService = new CompanyService(companyRepository, employeeRepository);
    }

    @Test
    void shouldComplainEmptyCnpj() {
        CompanyDTO companyDTO = new CompanyDTO();
        BusinessException businessException = assertThrows(BusinessException.class, () -> companyService.register(companyDTO));
        assertEquals("company.validation.cnpj", businessException.getMessage());
    }

    @Test
    void shouldComplanyInvalidFormatCnpj() {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCnpj("12345");
        BusinessException businessException = assertThrows(BusinessException.class, () -> companyService.register(companyDTO));
        assertEquals("company.validation.cnpj", businessException.getMessage());
    }

    @Test
    void shouldRegisterNewCompany() {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Name");
        companyDTO.setCnpj("68.212.846/0001-29");
        companyDTO.setWebsite("https://site.com");
        companyDTO.setTelephone("12312312312");
        companyDTO.setIndustry(IndustryEnum.ENERGY.getDescription());

        expect(companyRepository.save(anyObject())).andReturn(new Company());

        replayAll();
        CompanyDTO returnedCompanyDTO = companyService.register(companyDTO);
        verifyAll();

        assertEquals("Name", returnedCompanyDTO.getName());
        assertEquals("68.212.846/0001-29", returnedCompanyDTO.getCnpj());
        assertEquals("12312312312", returnedCompanyDTO.getTelephone());
        assertEquals("https://site.com", returnedCompanyDTO.getWebsite());
        assertEquals(IndustryEnum.ENERGY.getDescription(), returnedCompanyDTO.getIndustry());
    }

    @Test
    void shouldListAllCompanies() {
        List<Company> companies = new ArrayList<>();
        companies.add(new Company());

        expect(companyRepository.findByNameContainsAndIndustryContains(anyString(), anyString(), anyObject(Pageable.class))).andReturn(companies);

        replayAll();
        List<CompanyDTO> companiesDTO = companyService.getAllCompanies(0, null, "industry", "industry");
        verifyAll();

        assertEquals(1, companiesDTO.size());
    }

    @Test
    void shouldListAllIndustries() {
        expect(companyRepository.findAllIndustriesDistinct()).andReturn(new ArrayList<>());

        replayAll();
        List<String> industries = companyService.getAllIndustries();
        verifyAll();

        assertEquals(11, industries.size());
    }

    @Test
    void shouldFindById() {
        expect(companyRepository.findById(anyLong())).andReturn(Optional.of(new Company()));

        replayAll();
        CompanyDTO companyDTO = companyService.findById(1L);
        verifyAll();

        assertNotNull(companyDTO);
    }

    @Test
    void shouldFindEmployeesByCompanyId() {
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        employee.setEmployer(new Company());
        employees.add(employee);
        employees.add(employee);
        employees.add(employee);

        expect(employeeRepository.findByEmployerIdCompany(anyLong())).andReturn(employees);

        replayAll();
        List<EmployeeDTO> employeesDTO = companyService.findEmployeesByCompanyId(1L);
        verifyAll();

        assertEquals(3, employeesDTO.size());
    }
}
