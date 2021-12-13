package com.paguemob.entity;

import com.paguemob.dto.CompanyDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

import static com.paguemob.utils.CpfCnpjUtils.clearCpfCnpj;

@Entity
@Table(name = "company")
@SequenceGenerator(name="company_seq", sequenceName="company_sequence", allocationSize = 1)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="company_seq")
    private Long idCompany;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    private String website;

    @Column(nullable = false)
    private String industry;

    public Company() {
    }

    public Company(CompanyDTO companyDTO) {
        this.idCompany = companyDTO.getIdCompany();
        this.name = companyDTO.getName().trim();
        this.cnpj = clearCpfCnpj(companyDTO.getCnpj().trim());
        this.telephone = companyDTO.getTelephone().trim();
        this.website = companyDTO.getWebsite().trim();
        this.industry = companyDTO.getIndustry().trim();
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
        Company company = (Company) o;
        return Objects.equals(idCompany, company.idCompany) && Objects.equals(name, company.name) && Objects.equals(cnpj, company.cnpj) && Objects.equals(telephone, company.telephone) && Objects.equals(website, company.website) && Objects.equals(industry, company.industry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCompany, name, cnpj, telephone, website, industry);
    }
}
