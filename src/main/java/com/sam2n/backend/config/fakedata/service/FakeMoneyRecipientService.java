package com.sam2n.backend.config.fakedata.service;

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
    private final MoneyRecipientRepository moneyRecipientRepository;

    public List<MoneyRecipient> generateAndSave(int amount) {
        List<MoneyRecipient> fakeMoneyRecipient = generateFakeMoneyRecipients(amount);
        moneyRecipientRepository.saveAll(fakeMoneyRecipient);
        log.info(">>> Were generated and saved fake money recipients. Amount: " + fakeMoneyRecipient.size());
        return fakeMoneyRecipient;
    }

    public List<MoneyRecipient> generateFakeMoneyRecipients(int amount) {
        List<MoneyRecipient> fakeMoneyRecipient = moneyRecipientRepository.findAll();
        if (fakeMoneyRecipient.isEmpty()) {
            fakeMoneyRecipient = IntStream.rangeClosed(1, amount)
                    .mapToObj(this::generateFakeMoneyRecipient)
                    .toList();
        }
        return fakeMoneyRecipient;
    }

    private MoneyRecipient generateFakeMoneyRecipient(int number) {
        return MoneyRecipient.builder()
                .name("MoneyRecipient")
                .avatarUrl("https://MoneyRecipient" + number + ".com/image" + number + ".jpg")
                .description("Some description for the fake money recipient with id = " + number)
                .accountDetails("Some account details for the fake money recipient with id = " + number)
                .siteUrl("https://MoneyRecipient" + number + ".com")
                .build();
    }
}
