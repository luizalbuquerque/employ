package com.paguemob.service;

import com.paguemob.dto.CompanyDTO;
import com.paguemob.dto.EmployeeDTO;
import com.paguemob.entity.Company;
import com.paguemob.enums.IndustryEnum;
import com.paguemob.exception.BusinessException;
import com.paguemob.repository.CompanyRepository;
import com.paguemob.repository.EmployeeRepository;
import com.paguemob.utils.CpfCnpjUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.paguemob.utils.PageableUtils.getPageable;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final EmployeeRepository employeeRepository;

    public CompanyService(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    public CompanyDTO register(CompanyDTO companyDTO) {
        validateCnpj(companyDTO);
        Company company = new Company(companyDTO);

        try {
            companyRepository.save(company);
            companyDTO.setIdCompany(company.getIdCompany());
            return companyDTO;
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("company.validation.cnpj.duplicate");
        }
    }

    public List<CompanyDTO> getAllCompanies(Integer page, Integer size, String name, String industry) {
        Pageable pageable = getPageable(page, size, "idCompany");
        List<Company> companies;

        if (filterNone(name, industry)) {
            companies = companyRepository.findAll(pageable).getContent();
        } else if (filterByName(name, industry)) {
            companies = companyRepository.findByNameContains(name, pageable);
        } else if (filterByIndustry(name, industry)) {
            companies = companyRepository.findByIndustryContains(industry, pageable);
        } else {
            companies = companyRepository.findByNameContainsAndIndustryContains(name, industry, pageable);
        }

        return companies
                .stream()
                .map(CompanyDTO::new)
                .collect(Collectors.toList());
    }

    public List<String> getAllIndustries() {
        List<String> industries = Arrays.stream(IndustryEnum.values())
                .map(IndustryEnum::getDescription)
                .collect(Collectors.toList());

        industries.addAll(companyRepository.findAllIndustriesDistinct());

        return industries
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    private void validateCnpj(CompanyDTO companyDTO) {
        if (!CpfCnpjUtils.isValidCnpj(companyDTO.getCnpj()))
            throw new BusinessException("company.validation.cnpj");
    }

    private boolean filterByIndustry(String name, String industry) {
        return ObjectUtils.isEmpty(name) && !ObjectUtils.isEmpty(industry);
    }

    private boolean filterByName(String name, String industry) {
        return !ObjectUtils.isEmpty(name) && ObjectUtils.isEmpty(industry);
    }

    private boolean filterNone(String name, String industry) {
        return ObjectUtils.isEmpty(name) && ObjectUtils.isEmpty(industry);
    }

    public CompanyDTO findById(Long idCompany) {
        Company company = companyRepository.findById(idCompany).orElse(null);
        if (company != null)
            return new CompanyDTO(company);
        return null;
    }

    public List<EmployeeDTO> findEmployeesByCompanyId(Long idCompany) {
        return employeeRepository.findByEmployerIdCompany(idCompany)
                .stream()
                .map(EmployeeDTO::new)
                .collect(Collectors.toList());
    }
}
