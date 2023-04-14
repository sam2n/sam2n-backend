package com.sam2n.backend.service;

import com.sam2n.backend.domain.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActivityService {

    public Activity addNewActivity(String url) {

        return Activity.builder()
                .link(url)
                .build();
    }

}
