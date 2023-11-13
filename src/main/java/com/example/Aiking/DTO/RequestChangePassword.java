package com.example.Aiking.DTO;

import io.micrometer.common.lang.Nullable;

public record RequestChangePassword(
        @Nullable
        String userName,
        @Nullable
        String opt,
        @Nullable
        String newPassword
) {
}
