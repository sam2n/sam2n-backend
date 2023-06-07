package com.sam2n.backend.config.fakedata.service;

import com.sam2n.backend.domain.Activity;
import com.sam2n.backend.domain.FitnessAccount;
import com.sam2n.backend.domain.enumeration.ActivityState;
import com.sam2n.backend.domain.enumeration.SportType;
import com.sam2n.backend.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.sam2n.backend.config.Profiles.LOCAL;
import static com.sam2n.backend.config.fakedata.LocalFakeDataInitializer.FAKER_USER;

@Service
@Profile(LOCAL)
@RequiredArgsConstructor
@Slf4j
public class FakeActivityService {
    private static final Random random = new Random();
    private final ActivityRepository activityRepository;

    public List<Activity> generateAndSave(List<FitnessAccount> fitnessAccounts, int amountPerUser) {
        List<Activity> fakeActivities = fitnessAccounts.stream()
                .map(fa -> generateAndSave(fa, amountPerUser))
                .flatMap(List::stream)
                .toList();

        fakeActivities = activityRepository.saveAll(fakeActivities);
        log.info(">>> Were generated and saved fake activities. Amount: " + fakeActivities.size());
        return fakeActivities;
    }

    private List<Activity> generateAndSave(FitnessAccount fitnessAccount, int amountPerUser) {
        return IntStream.rangeClosed(1, amountPerUser)
                .mapToObj(n -> generateFakeActivityForFitnessAccount(fitnessAccount, n))
                .toList();
    }

    private Activity generateFakeActivityForFitnessAccount(FitnessAccount fitnessAccount, int number) {
        return Activity.builder()
                .account(fitnessAccount)
                .activityState(ActivityState.values()[random.nextInt(ActivityState.values().length)])
                .createdBy(FAKER_USER)
                .calories(random.nextInt(100, 1000))
                .distance(random.nextDouble(1, 20))
                .title("Activity on the street: " + number)
                .link(fitnessAccount.getUrl() + "/activities/" + number)
                .sportType(SportType.values()[random.nextInt(SportType.values().length)])
                .movingTime(Duration.ofMinutes(random.nextInt(100)))
                .avgPace(random.nextDouble(4, 10))
                .avgHeartRate(random.nextDouble(60, 240))
                // random date between now and -100 days from now
                .activityDate(Instant.now().minus(Duration.ofDays(random.nextInt(100))))
                .build();
    }
}
