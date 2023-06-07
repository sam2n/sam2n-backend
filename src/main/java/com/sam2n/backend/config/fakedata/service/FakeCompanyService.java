package com.sam2n.backend.fakedata.service;

import com.github.javafaker.Faker;
import com.sam2n.backend.domain.Company;
import com.sam2n.backend.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

import static com.sam2n.backend.config.DataBaseConfig.CREATED_BY_USER;
import static com.sam2n.backend.config.Profiles.LOCAL;

@Service
@Profile(LOCAL)
@RequiredArgsConstructor
@Slf4j
public class FakeCompanyService {
    private final Faker faker = Faker.instance();
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
                    .mapToObj(value -> generateFakeCompany())
                    .toList();
        }
        return fakeCompanies;
    }

    private Company generateFakeCompany() {
        return Company.builder()
                .name(faker.company().name())
                .url(faker.company().url()).description(faker.company().catchPhrase())
                .createdBy(CREATED_BY_USER)
                .imageUrl(faker.internet().avatar())
                .build();
    }
}
