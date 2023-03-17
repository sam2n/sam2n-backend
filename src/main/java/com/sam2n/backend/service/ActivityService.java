package com.sam2n.backend.service;

import com.sam2n.backend.domain.Activity;
import com.sam2n.backend.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public Activity addNewActivity(String url) {

        return new Activity();
    }

}
