package com.sam2n.backend.fakedata.service;

import com.github.javafaker.Faker;
import com.sam2n.backend.domain.MoneyRecipient;
import com.sam2n.backend.repository.MoneyRecipientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

import static com.sam2n.backend.config.Profiles.LOCAL;

@Service
@Profile(LOCAL)
@RequiredArgsConstructor
@Slf4j
public class FakeMoneyRecipientService {
    private final Faker faker = Faker.instance();
    private final MoneyRecipientRepository moneyRecipientRepository;

    public List<MoneyRecipient> generateAndSave(int amount) {
        List<MoneyRecipient> fakeMoneyRecipient = generateFakeMoneyRecipient(amount);
        moneyRecipientRepository.saveAll(fakeMoneyRecipient);
        log.info(">>> Were generated and saved fake money recipients. Amount: " + fakeMoneyRecipient.size());
        return fakeMoneyRecipient;
    }

    public List<MoneyRecipient> generateFakeMoneyRecipient(int amount) {
        List<MoneyRecipient> fakeMoneyRecipient = moneyRecipientRepository.findAll();
        if (fakeMoneyRecipient.isEmpty()) {
            fakeMoneyRecipient = IntStream.rangeClosed(1, amount)
                    .mapToObj(value -> generateFakeMoneyRecipient())
                    .toList();
        }
        return fakeMoneyRecipient;
    }

    private MoneyRecipient generateFakeMoneyRecipient() {
        MoneyRecipient moneyRecipient = new MoneyRecipient();
        moneyRecipient.setName(faker.company().name());
        moneyRecipient.setAvatarUrl(faker.internet().avatar());
        moneyRecipient.setDescription(faker.company().catchPhrase());
        moneyRecipient.setAccountDetails(faker.company().catchPhrase());
        moneyRecipient.setSiteUrl(faker.internet().url());
        return moneyRecipient;
    }
}
