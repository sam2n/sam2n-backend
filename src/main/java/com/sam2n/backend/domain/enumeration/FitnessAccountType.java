package com.sam2n.backend.domain.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The FitnessAccountType enumeration.
 */
@RequiredArgsConstructor
@Getter
public enum FitnessAccountType {
    STRAVA(true),
    APPLE_HEALTH(false),
    GOOGLE_FIT(false),
    GARMIN_CONNECT(false);
    private final boolean isActive;


}
