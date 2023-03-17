package com.sam2n.backend.fakedata.service;

import com.github.javafaker.Faker;
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

import static com.sam2n.backend.config.DataBaseConfig.CREATED_BY_USER;
import static com.sam2n.backend.config.Profiles.LOCAL;

@Service
@Profile(LOCAL)
@RequiredArgsConstructor
@Slf4j
public class FakeActivityService {
    private static final Random random = new Random();
    private final Faker faker = Faker.instance();
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
                .mapToObj(n -> generateFakeActivityForFitnessAccount(fitnessAccount))
                .toList();
    }

    private Activity generateFakeActivityForFitnessAccount(FitnessAccount fitnessAccount) {
        Activity fakeActivity = new Activity();
        fakeActivity.setAccount(fitnessAccount);
        // get random activity state
        fakeActivity.setActivityState(ActivityState.values()[random.nextInt(ActivityState.values().length)]);
        fakeActivity.setCreatedBy(CREATED_BY_USER);
        fakeActivity.setCalories(random.nextInt(100, 1000));
        fakeActivity.setDistance(random.nextDouble(1, 20));
        fakeActivity.setTitle("Activity on the street: " + faker.address().streetName());
        fakeActivity.setLink(faker.internet().url());
        fakeActivity.setSportType(SportType.values()[random.nextInt(SportType.values().length)]);
        fakeActivity.setMovingTime(Duration.ofMinutes(random.nextInt(100)));
        fakeActivity.setAvgPace(random.nextDouble(4, 10));
        fakeActivity.setAvgHeartRate(random.nextDouble(60, 240));
        // random date between now and -100 days from now
        fakeActivity.setActivityDate(Instant.now().minus(Duration.ofDays(random.nextInt(100))));

        return fakeActivity;
    }
}
