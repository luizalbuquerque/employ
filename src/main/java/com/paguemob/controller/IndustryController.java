package com.paguemob.controller;

import com.paguemob.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/industry")
@Tag(name = "Industry controller")
public class IndustryController {

    private final CompanyService companyService;

    public IndustryController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List available industries", description = "Lists all the available industries.")
    @ApiResponse(responseCode = "200", description = "Successfully listed the available industries, even if the list is empty.")
    public List<String> getAllIndustries() {
        return companyService.getAllIndustries();
    }
}
