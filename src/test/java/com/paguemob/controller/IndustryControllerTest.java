package com.paguemob.controller;

import com.paguemob.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IndustryControllerTest {

    @MockBean
    private CompanyRepository companyRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldListAllIndustries() throws Exception {
        List<String> industries = new ArrayList<>();
        industries.add("Custom industry");

        when(companyRepository.findAllIndustriesDistinct()).thenReturn(industries);

        mockMvc.perform(get("/api/industry")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
