package com.sam2n.backend.mapper;

import com.sam2n.backend.domain.User;
import com.sam2n.backend.dto.auth.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RegisterRequestMapper {
    User toUser(RegisterRequest registerRequest);
}
