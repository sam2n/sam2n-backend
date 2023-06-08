package com.sam2n.backend.config.fakedata.service;

import com.sam2n.backend.domain.Activity;
import com.sam2n.backend.domain.MoneyRecipient;
import com.sam2n.backend.domain.Transaction;
import com.sam2n.backend.domain.User;
import com.sam2n.backend.domain.enumeration.TransactionState;
import com.sam2n.backend.domain.enumeration.TransactionType;
import com.sam2n.backend.repository.ActivityRepository;
import com.sam2n.backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import static com.sam2n.backend.config.Profiles.LOCAL;
import static com.sam2n.backend.config.fakedata.LocalFakeDataInitializer.FAKER_USER;

@Service
@Profile(LOCAL)
@RequiredArgsConstructor
@Slf4j
public class FakeTransactionService {
    private static final Random random = new Random();
    private final TransactionRepository transactionRepository;

    private final ActivityRepository activityRepository;

    public List<Transaction> generateAndPersistFakeTransactionsWithActivities(List<Activity> activities) {
        List<Transaction> fakeTransactions = activities.stream()
                .map(activity -> {
                    Transaction transaction = generateFakeTransaction(TransactionType.TRANSFORMED_ACTIVITY, activity.getAccount().getUser(), activity, null);
                    transaction = transactionRepository.save(transaction);
                    // update transaction id for activity
                    activity.setTransaction(transaction);
                    activityRepository.save(activity);
                    return transaction;
                })
                .toList();

        log.info(">>> Were generated and saved fake transactions for activities. Amount: " + fakeTransactions.size());
        return fakeTransactions;
    }

    public List<Transaction> generateAndPersistFakeTransactionsWithDonations(List<MoneyRecipient> moneyRecipients, List<User> users, int amountOfDonations) {

        List<Transaction> fakeTransactions = IntStream.rangeClosed(1, amountOfDonations)
                .mapToObj(n -> {
                    User randomUser = users.get(random.nextInt(users.size()));
                    MoneyRecipient randomMoneyRecipient = moneyRecipients.get(random.nextInt(moneyRecipients.size()));
                    Transaction transaction = generateFakeTransaction(TransactionType.DONATION, randomUser, null, randomMoneyRecipient);
                    transaction = transactionRepository.save(transaction);
                    return transaction;
                })
                .toList();

        log.info(">>> Were generated and saved fake transactions for donations. Amount: " + fakeTransactions.size());
        return fakeTransactions;
    }

    private Transaction generateFakeTransaction(TransactionType transactionType, User user, Activity activity, MoneyRecipient moneyRecipient) {
        var transactionBuilder = Transaction.builder()
                .transactionType(transactionType)
                .amount(random.nextDouble(1, 10))
                .wallet(user.getWallet())
                .createdBy(FAKER_USER);

        if (Objects.requireNonNull(transactionType) == TransactionType.DONATION) {
            TransactionState randomTransactionState = TransactionState.values()[random.nextInt(TransactionState.values().length)];
            transactionBuilder
                    .reasonId(moneyRecipient.getId())
                    .transactionState(randomTransactionState)
                    .dateOfPayment(
                            TransactionState.PAID == randomTransactionState
                                    ? Instant.now().minus(Duration.ofDays(random.nextInt(100)))
                                    : null)
                    .message("Donate for " + moneyRecipient.getName() + " from user: " + user.getLogin());
        } else {
            transactionBuilder
                    .transactionState(TransactionState.PAID)
                    .reasonId(activity.getId())
                    .message("Money transformed from activity " + activity.getTitle());
        }

        return transactionBuilder.build();
    }
}
