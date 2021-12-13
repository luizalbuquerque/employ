package com.paguemob.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.paguemob.controller.EmployeeController;
import com.paguemob.dto.EmployeeDTO;
import com.paguemob.entity.Company;
import com.paguemob.entity.Employee;
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
class EmployeeControllerTest {

    @Autowired
    private EmployeeController employeeController;

    @MockBean
    private CompanyRepository companyRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private MockMvc mockMvc;

    private String employeeJson;

    @BeforeEach
    void buildEmployeeJson() throws JsonProcessingException {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("Name");
        employeeDTO.setEmail("name@email.com");
        employeeDTO.setCpf("029.574.620-36");
        employeeDTO.setGender("male");
        employeeDTO.setJobTitle("Job Title");
        employeeDTO.setSeed("seed");
        employeeDTO.setIdEmployer(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        employeeJson = objectWriter.writeValueAsString(employeeDTO);
    }

    @Test
    void shouldRegisterNewUser() throws Exception {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(new Company()));
        employeeRepository.save(any(Employee.class));

        mockMvc.perform(post("/api/employee")
                        .contentType("application/json")
                        .content(employeeJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldComplainEmptyFieldsOnRegisterNewUserEndpoint() throws Exception {
        mockMvc.perform(post("/api/employee")
                        .contentType("application/json")
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[\"Invalid employer ID.\",\"Invalid seed.\"," +
                        "\"Invalid name.\",\"Invalid CPF.\",\"Invalid job title.\",\"Invalid gender.\",\"Invalid e-mail;\"]}"));
    }

    @Test
    void shouldListEmployees() throws Exception {
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        employee.setEmployer(new Company());
        employees.add(employee);
        employees.add(employee);
        employees.add(employee);

        Page<Employee> page = new PageImpl<>(employees);

        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/employee")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindEmployeeById() throws Exception {
        Employee employee = new Employee();
        employee.setEmployer(new Company());

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/api/employee/1")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
