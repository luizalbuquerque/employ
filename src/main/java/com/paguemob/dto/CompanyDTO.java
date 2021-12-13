package com.paguemob.dto;

import com.paguemob.entity.Company;
import com.paguemob.utils.CpfCnpjUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Schema(description = "Information that composes a company.", name = "Company")
public class CompanyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idCompany;

    @NotBlank(message = "company.validation.name")
    private String name;

    @NotBlank(message = "company.validation.cnpj")
    private String cnpj;

    @NotBlank(message = "company.validation.telephone")
    private String telephone;

    @NotBlank(message = "company.validation.website")
    private String website;

    @NotBlank(message = "company.validation.industry")
    private String industry;

    public CompanyDTO() {
    }

    public CompanyDTO(Company company) {
        this.idCompany = company.getIdCompany();
        this.name = company.getName();
        this.cnpj = CpfCnpjUtils.formatCnpj(company.getCnpj());
        this.telephone = company.getTelephone();
        this.website = company.getWebsite();
        this.industry = company.getIndustry();
    }

    public Long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Long idCompany) {
        this.idCompany = idCompany;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyDTO that = (CompanyDTO) o;
        return Objects.equals(idCompany, that.idCompany) && Objects.equals(name, that.name) && Objects.equals(cnpj, that.cnpj) && Objects.equals(telephone, that.telephone) && Objects.equals(website, that.website) && Objects.equals(industry, that.industry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCompany, name, cnpj, telephone, website, industry);
    }
}
