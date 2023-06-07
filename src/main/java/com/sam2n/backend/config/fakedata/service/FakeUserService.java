package com.sam2n.backend.fakedata.service;

import com.github.javafaker.Faker;
import com.sam2n.backend.domain.Authority;
import com.sam2n.backend.domain.Company;
import com.sam2n.backend.domain.User;
import com.sam2n.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static com.sam2n.backend.config.DataBaseConfig.CREATED_BY_USER;
import static com.sam2n.backend.config.Profiles.LOCAL;

@Service
@Profile(LOCAL)
@RequiredArgsConstructor
@Slf4j
public class FakeUserService {
    private final Random random = new Random();
    private final Faker faker = Faker.instance();
    private final UserRepository userRepository;
    private final FakeWalletService fakeWalletService;
//    private final PasswordEncoder passwordEncoder;

    public List<User> generateAndSave(List<Company> companies, Authority userAuthority, int amount) {
        List<User> fakeUsers = userRepository.findAll();
        if (fakeUsers.isEmpty()) {
            // generate and save fake users
            fakeUsers = IntStream.rangeClosed(1, amount)
                    .mapToObj(value -> generateFakeUser(companies.get(random.nextInt(companies.size() - 1)), userAuthority))
                    .toList();
            fakeUsers.forEach(user -> log.debug(">>> Generated fake user: " + user.toString()));

            fakeUsers = userRepository.saveAll(fakeUsers);
            /* generate wallets for users */
            fakeUsers.forEach(user -> user.setWallet(fakeWalletService.generateAndPersistFakeWallet(user)));
            fakeUsers = userRepository.saveAll(fakeUsers);
            log.info(">>> Were generated and saved fake users with wallets. Amount: " + fakeUsers.size());
        }
        return fakeUsers;
    }

    public List<User> generateAndSave(List<Company> companies, Authority adminAuthority) {
        List<User> fakeAdmins = userRepository.findAll();
        if (fakeAdmins.isEmpty()) {
            // generate and save fake admins 1 per company
            fakeAdmins = IntStream.rangeClosed(0, companies.size() - 1)
                    .mapToObj(value -> generateFakeUser(companies.get(value), adminAuthority))
                    .toList();
            fakeAdmins = userRepository.saveAll(fakeAdmins);
            log.info(">>> Were generated and saved fake admins. Amount: " + fakeAdmins.size());
        }
        return fakeAdmins;
    }

    private User generateFakeUser(Company fakeCompany, Authority authority) {
        String login = faker.name().lastName().trim().toLowerCase().replace(" ", "_") + random.nextInt(1000);
        return User.builder()
                .activated(true)
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .login(login)
                .password(login)
                .company(fakeCompany)
                .email(faker.internet().emailAddress())
                .authorities(Set.of(authority))
                .imageUrl(faker.internet().avatar())
                .activationKey(faker.number().digits(20))
                .resetKey(faker.number().digits(20))
                .createdBy(CREATED_BY_USER)
                .build();
    }
}