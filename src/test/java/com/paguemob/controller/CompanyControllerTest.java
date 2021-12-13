package com.paguemob.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.paguemob.controller.CompanyController;
import com.paguemob.dto.CompanyDTO;
import com.paguemob.entity.Company;
import com.paguemob.entity.Employee;
import com.paguemob.enums.IndustryEnum;
import com.paguemob.repository.CompanyRepository;
import com.paguemob.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CompanyControllerTest {

    @Autowired
    private CompanyController companyController;

    @MockBean
    private CompanyRepository companyRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private MockMvc mockMvc;

    private String companyJson;

    @BeforeEach
    void buildCompanyJson() throws JsonProcessingException {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Name");
        companyDTO.setCnpj("68.212.846/0001-29");
        companyDTO.setIndustry(IndustryEnum.BANKING.getDescription());
        companyDTO.setTelephone("12312312312");
        companyDTO.setWebsite("https://site.com");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        companyJson = objectWriter.writeValueAsString(companyDTO);
    }

    @Test
    void shouldRegisterNewCompany() throws Exception {
        companyRepository.save(any(Company.class));

        mockMvc.perform(post("/api/company")
                        .contentType("application/json")
                        .content(companyJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldComplainEmptyFieldsOnRegisterNewCompanyEndpoint() throws Exception {
        mockMvc.perform(post("/api/company")
                        .contentType("application/json")
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[\"Invalid name.\",\"Invalid CNPJ.\"," +
                        "\"Invalid website.\",\"Invalid telephone.\",\"Invalid industry.\"]}"));
    }

    @Test
    void shouldListCompanies() throws Exception {
        List<Company> companies = new ArrayList<>();
        Company company = new Company();
        companies.add(company);
        companies.add(company);
        companies.add(company);

        Page<Company> page = new PageImpl<>(companies);

        when(companyRepository.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/company")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindCompanyById() throws Exception {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(new Company()));

        mockMvc.perform(get("/api/company/1")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindEmployeesByCompanyId() throws Exception {
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        employee.setEmployer(new Company());
        employees.add(employee);
        employees.add(employee);
        employees.add(employee);

        when(employeeRepository.findByEmployerIdCompany(anyLong())).thenReturn(employees);

        mockMvc.perform(get("/api/company/1/employee")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
