package com.sam2n.backend.fakedata;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(ignoreUnknownFields = false, prefix = "sam2n.fake-data.amount-of")
public class FakeDataAmountConfigurationProperties {
    private int companies;
    private int moneyRecipients;
    private int users;
    private int perUserActivities;
    private int donations;
}
