package com.paguemob.controller;

import com.paguemob.dto.CompanyDTO;
import com.paguemob.dto.EmployeeDTO;
import com.paguemob.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/company")
@Tag(name = "Company controller")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register company", description = "Register a new company.")
    @ApiResponse(responseCode = "201", description = "Successfully registered a new company. Returns the company information with its ID.")
    public CompanyDTO register(@RequestBody @Valid CompanyDTO companyDTO) {
        return companyService.register(companyDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List companies", description = "List companies, allowing to filter by name and industry. Pagination enabled.")
    @ApiResponse(responseCode = "200", description = "Successfully listed the companies, even if the list is empty.")
    public List<CompanyDTO> getAllCompanies(@RequestParam(value = "page", defaultValue = "0") @Parameter(description = "Page number") Integer page,
                                            @RequestParam(value = "size", required = false) @Parameter(description = "List size") Integer size,
                                            @RequestParam(value = "name", required = false) @Parameter(description = "Part of the company name to filter by") String name,
                                            @RequestParam(value = "industry", required = false) @Parameter(description = "Part of the company industry to filter by") String industry) {
        return companyService.getAllCompanies(page, size, name, industry);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find employee by ID", description = "Finds an employee by ID.")
    @ApiResponse(responseCode = "200", description = "Successfully completed the request, even if the company hasn't been found.")
    public CompanyDTO findById(@PathVariable("id") @Parameter(description = "Company ID") Long idCompany) {
        return companyService.findById(idCompany);
    }

    @GetMapping("/{id}/employee")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List employees", description = "Lists employees by company ID.")
    @ApiResponse(responseCode = "200", description = "Successfully listed the employees by company ID, even if the list is empty.")
    public List<EmployeeDTO> findEmployeesByCompanyId(@PathVariable("id") @Parameter(description = "Company ID") Long idCompany) {
        return companyService.findEmployeesByCompanyId(idCompany);
    }
}
