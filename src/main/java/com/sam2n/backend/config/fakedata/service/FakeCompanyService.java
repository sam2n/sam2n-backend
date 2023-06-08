package com.sam2n.backend.config.fakedata.service;

import com.sam2n.backend.domain.Company;
import com.sam2n.backend.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

import static com.sam2n.backend.config.Profiles.LOCAL;
import static com.sam2n.backend.config.fakedata.LocalFakeDataInitializer.FAKER_USER;

@Service
@Profile(LOCAL)
@RequiredArgsConstructor
@Slf4j
public class FakeCompanyService {
    private final CompanyRepository companyRepository;

    public List<Company> generateAndSave(int amount) {
        List<Company> fakeCompanies = generateFakeCompanies(amount);
        companyRepository.saveAll(fakeCompanies);
        log.info(">>> Were generated and saved fake companies. Amount: " + fakeCompanies.size());
        return fakeCompanies;
    }

    public List<Company> generateFakeCompanies(int amount) {
        List<Company> fakeCompanies = companyRepository.findAll();
        if (fakeCompanies.isEmpty()) {
            fakeCompanies = IntStream.rangeClosed(1, amount)
                    .mapToObj(this::generateFakeCompany)
                    .toList();
        }
        return fakeCompanies;
    }

    private Company generateFakeCompany(int index) {
        return Company.builder()
                .name("Company " + index)
                .url("http://company" + index + ".com/")
                .description("Just do it for company " + index)
                .createdBy(FAKER_USER)
                .imageUrl("http://company" + index + ".com/images/" + index + ".jpg")
                .build();
    }
}
