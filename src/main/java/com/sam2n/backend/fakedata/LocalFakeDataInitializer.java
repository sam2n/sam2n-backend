package com.sam2n.backend.fakedata;

import com.sam2n.backend.domain.*;
import com.sam2n.backend.fakedata.service.*;
import com.sam2n.backend.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.sam2n.backend.config.SecurityConfig.Authority.ADMIN;
import static com.sam2n.backend.config.SecurityConfig.Authority.USER;

@RequiredArgsConstructor
@Slf4j
@Component
@ConditionalOnProperty(prefix = "sam2n.fake-data", name = "generate", havingValue = "true")
@EnableConfigurationProperties(FakeDataAmountConfigurationProperties.class)
public class LocalFakeDataInitializer implements CommandLineRunner {
    private final FakeCompanyService fakeCompanyService;
    private final FakeMoneyRecipientService fakeMoneyRecipientService;
    private final AuthorityService authorityService;
    private final FakeUserService fakeUserService;
    private final FakeFitnessAccountService fakeFitnessAccountService;
    private final FakeActivityService fakeActivityService;
    private final FakeTransactionService fakeTransactionService;
    private final FakeDataAmountConfigurationProperties fakeDataAmount;
    @Override
    public void run(String... args) throws Exception {
        log.warn(">>> Process of generation fake data has been started");
        int amountOfGeneratedRows = generateFakeDBData();
        log.warn(">>> Process of generation fake data is finished. Amount of generated rows is: " + amountOfGeneratedRows);
    }

    public int generateFakeDBData() {
        /* ORGANISATIONS */
        List<Company> fakeCompanies = fakeCompanyService.generateAndSave(fakeDataAmount.getCompanies());
        List<MoneyRecipient> fakeMoneyRecipients = fakeMoneyRecipientService.generateAndSave(fakeDataAmount.getMoneyRecipients());

        /* USERS & ACCOUNTS */
        List<Authority> realAuthorities = authorityService.initAndSave(USER.getName(), ADMIN.getName());
        Authority userAuthority = realAuthorities.stream()
                .filter(a -> a.getName().equalsIgnoreCase(USER.getName()))
                .findFirst()
                .orElse(null);
        List<User> fakeUsers = fakeUserService.generateAndSave(fakeCompanies, userAuthority, fakeDataAmount.getUsers());

        Authority adminAuthority = realAuthorities.stream()
                .filter(a -> a.getName().equalsIgnoreCase(ADMIN.getName()))
                .findFirst()
                .orElse(null);
        List<User> fakeAdmins = fakeUserService.generateAndSave(fakeCompanies, adminAuthority);

        List<FitnessAccount> fakeFitnessAccounts = fakeFitnessAccountService.generateAndSave(fakeUsers);
        List<Activity> fakeActivities = fakeActivityService.generateAndSave(fakeFitnessAccounts, fakeDataAmount.getPerUserActivities());

        /* TRANSACTIONS */
        List<Transaction> fakeTransactionsWithActivities = fakeTransactionService
                .generateAndPersistFakeTransactionsWithActivities(fakeActivities);
        List<Transaction> fakeTransactionsWithDonations = fakeTransactionService
                .generateAndPersistFakeTransactionsWithDonations(fakeMoneyRecipients, fakeUsers, fakeDataAmount.getDonations());

        return /* ORGANISATIONS */ fakeCompanies.size() + fakeMoneyRecipients.size()
                /* USERS & ACCOUNTS */ + realAuthorities.size() + fakeUsers.size() + fakeAdmins.size() + fakeFitnessAccounts.size() + fakeActivities.size()
                /* TRANSACTIONS */ + fakeTransactionsWithActivities.size() + fakeTransactionsWithDonations.size();
    }

}
