package com.sam2n.backend.config.fakedata;

import com.sam2n.backend.config.fakedata.service.*;
import com.sam2n.backend.domain.*;
import com.sam2n.backend.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.sam2n.backend.config.security.AuthorityType.ADMIN;
import static com.sam2n.backend.config.security.AuthorityType.USER;

@RequiredArgsConstructor
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "sam2n.fake-data", name = "generate", havingValue = "true")
@EnableConfigurationProperties(FakeDataAmountConfigurationProperties.class)
public class LocalFakeDataInitializer {
    private final FakeCompanyService fakeCompanyService;
    private final FakeMoneyRecipientService fakeMoneyRecipientService;
    private final AuthorityService authorityService;
    private final FakeUserService fakeUserService;
    private final FakeFitnessAccountService fakeFitnessAccountService;
    private final FakeActivityService fakeActivityService;
    private final FakeTransactionService fakeTransactionService;
    private final FakeDataAmountConfigurationProperties fakeDataAmount;

    // Regex for acceptable logins
    public static final String FAKER_USER = "FAKER";

    @Bean
    public CommandLineRunner initFakeData() {
        return args -> generateFakeDBData();
    }

    private void generateFakeDBData() {
        /* ORGANISATIONS */
        List<Company> fakeCompanies = fakeCompanyService.generateAndSave(fakeDataAmount.getCompanies());
        List<MoneyRecipient> fakeMoneyRecipients = fakeMoneyRecipientService.generateAndSave(fakeDataAmount.getMoneyRecipients());

        /* USERS & ACCOUNTS */
        List<Authority> realAuthorities = authorityService.initAndSave(USER.getDbName(), ADMIN.getDbName());
        Authority userAuthority = realAuthorities.stream()
                .filter(a -> a.getName().equalsIgnoreCase(USER.getDbName()))
                .findFirst()
                .orElse(null);
        List<User> fakeUsers = fakeUserService.generateAndSave(fakeCompanies, userAuthority, fakeDataAmount.getUsers());

        Authority adminAuthority = realAuthorities.stream()
                .filter(a -> a.getName().equalsIgnoreCase(ADMIN.getDbName()))
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

        log.debug(">>> Available credentials of users to login: ");
        fakeUsers.forEach(u -> log.debug(">>>      User name: " + u.getLogin() + ", password: " + u.getLogin()));
        log.debug(">>> Available credentials of admins to login: ");
        fakeAdmins.forEach(a -> log.debug(">>>      Admin name: " + a.getLogin() + ", password: " + a.getLogin()));

        log.warn(">>> Final amount of generated records is: " +
                /* ORGANISATIONS */ fakeCompanies.size() + fakeMoneyRecipients.size()
                /* USERS & ACCOUNTS */ + realAuthorities.size() + fakeUsers.size() + fakeAdmins.size() + fakeFitnessAccounts.size() + fakeActivities.size()
                /* TRANSACTIONS */ + fakeTransactionsWithActivities.size() + fakeTransactionsWithDonations.size());
    }

}
