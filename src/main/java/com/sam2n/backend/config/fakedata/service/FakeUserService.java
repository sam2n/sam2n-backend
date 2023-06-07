package com.sam2n.backend.config.fakedata.service;

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

import static com.sam2n.backend.config.Profiles.LOCAL;
import static com.sam2n.backend.config.fakedata.LocalFakeDataInitializer.FAKER_USER;

@Service
@Profile(LOCAL)
@RequiredArgsConstructor
@Slf4j
public class FakeUserService {
    private final Random random = new Random();
    private final UserRepository userRepository;
    private final FakeWalletService fakeWalletService;

    public List<User> generateAndSave(List<Company> companies, Authority userAuthority, int amount) {
        List<User> fakeUsers = userRepository.findAll();
        if (fakeUsers.isEmpty()) {
            // generate and save fake users
            fakeUsers = IntStream.rangeClosed(1, amount)
                    .mapToObj(value -> generateFakeUser(companies.get(random.nextInt(companies.size() - 1)), userAuthority, value))
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
                    .mapToObj(value -> generateFakeUser(companies.get(value), adminAuthority, value))
                    .toList();
            fakeAdmins = userRepository.saveAll(fakeAdmins);
            log.info(">>> Were generated and saved fake admins. Amount: " + fakeAdmins.size());
        }
        return fakeAdmins;
    }

    private User generateFakeUser(Company fakeCompany, Authority authority, int number) {

        return User.builder()
                .activated(true)
                .firstName("FirstName" + number)
                .lastName("LastName" + number)
                .login("Login" + number)
                .password("Login" + number)
                .company(fakeCompany)
                .email("FirstName" + number + "LastName" + number + "@gmail.com")
                .authorities(Set.of(authority))
                .imageUrl(fakeCompany.getUrl() + "/users/" + number + "/avatar")
                .activationKey("1234567890" + number)
                .resetKey("0000000000" + number)
                .createdBy(FAKER_USER)
                .build();
    }
}
