package com.sam2n.backend.config.fakedata.service;

import com.sam2n.backend.domain.FitnessAccount;
import com.sam2n.backend.domain.User;
import com.sam2n.backend.domain.enumeration.FitnessAccountType;
import com.sam2n.backend.repository.FitnessAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.sam2n.backend.config.Profiles.LOCAL;
import static com.sam2n.backend.config.fakedata.LocalFakeDataInitializer.FAKER_USER;

@Service
@Profile(LOCAL)
@RequiredArgsConstructor
@Slf4j
public class FakeFitnessAccountService {
    private static final Random random = new Random();
    private final FitnessAccountRepository fitnessAccountRepository;

    public List<FitnessAccount> generateAndSave(List<User> users) {
        List<FitnessAccount> fakeFitnessAccounts = users.stream()
                .map(this::generateAndSave)
                .flatMap(List::stream)
                .toList();

        fakeFitnessAccounts = fitnessAccountRepository.saveAll(fakeFitnessAccounts);

        log.info(">>> Were generated and saved fake fitness accounts. Amount: " + fakeFitnessAccounts.size());

        return fakeFitnessAccounts;
    }

    private List<FitnessAccount> generateAndSave(User user) {
        int randomAmount = random.nextInt(FitnessAccountType.values().length);
        return IntStream.rangeClosed(1, randomAmount)
                .mapToObj(n -> generateFakeFitnessAccount(user, FitnessAccountType.values()[random.nextInt(FitnessAccountType.values().length)], n))
                .toList();
    }


    private FitnessAccount generateFakeFitnessAccount(User user, FitnessAccountType fitnessAccountType, int activityNumber) {
        return FitnessAccount.builder()
                .fitnessAccountType(fitnessAccountType)
                .user(user)
                .url("https://" + fitnessAccountType.name().toLowerCase() + ".com/" + user.getLogin() + "/" + activityNumber)
                .isActive(random.nextBoolean())
                .createdBy(FAKER_USER)
                .build();
    }
}
