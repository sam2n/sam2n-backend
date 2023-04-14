package com.sam2n.backend.fakedata.service;

import com.sam2n.backend.domain.User;
import com.sam2n.backend.domain.Wallet;
import com.sam2n.backend.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

import static com.sam2n.backend.config.DataBaseConfig.CREATED_BY_USER;
import static com.sam2n.backend.config.Profiles.LOCAL;

@Service
@Profile(LOCAL)
@RequiredArgsConstructor
@Slf4j
public class FakeWalletService {
    private final Random random = new Random();
    private final WalletRepository walletRepository;

    public Wallet generateAndPersistFakeWallet(User user) {
        Wallet fakeWallet = generateWallet(user);
        fakeWallet = walletRepository.save(fakeWallet);
        log.info(">>> Was generated and saved wallet for user: " + user.getLogin());
        return fakeWallet;
    }

    private Wallet generateWallet(User user) {
        return Wallet.builder()
                .amount(BigDecimal.valueOf(random.nextInt(100, 10000)))
                .user(user).createdBy(CREATED_BY_USER)
                .build();
    }
}
