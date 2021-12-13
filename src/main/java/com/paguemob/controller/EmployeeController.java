package com.paguemob.controller;

import com.paguemob.dto.EmployeeDTO;
import com.paguemob.service.EmployeeService;
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
@RequestMapping("/api/employee")
@Tag(name = "Employee controller")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register employee", description = "Register a new employee.")
    @ApiResponse(responseCode = "201", description = "Successfully registered a new employee. Returns the employee information with its ID.")
    public EmployeeDTO register(@RequestBody @Valid EmployeeDTO employeeDTO) {
        return employeeService.register(employeeDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List employees", description = "List employees, allowing to filter by job title. Pagination enabled.")
    @ApiResponse(responseCode = "200", description = "Successfully listed the employees, even if the list is empty.")
    public List<EmployeeDTO> getAllEmployees(@RequestParam(value = "page", defaultValue = "0") @Parameter(description = "Page number") Integer page,
                                            @RequestParam(value = "size", required = false) @Parameter(description = "List size") Integer size,
                                            @RequestParam(value = "jobTitle", required = false) @Parameter(description = "Part of the job title to filter by") String jobTitle) {
        return employeeService.getAllEmployees(page, size, jobTitle);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find employee by ID", description = "Finds an employee by ID.")
    @ApiResponse(responseCode = "200", description = "Successfully completed the request, even if the employee hasn't been found.")
    public EmployeeDTO findById(@PathVariable("id") @Parameter(description = "Employee ID") Long idEmployee) {
        return employeeService.findById(idEmployee);
    }
}
