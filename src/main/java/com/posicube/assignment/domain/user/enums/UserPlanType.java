package com.posicube.assignment.domain.user.enums;

import com.posicube.assignment.error.GlobalErrorCode;
import com.posicube.assignment.error.exception.ApiCommonException;
import lombok.Getter;

@Getter
public enum UserPlanType {
    PRO("PRO", 50000),
    LITE("LITE", 10000);

    private final String name;
    private final int quota;

    UserPlanType(String name, int quota) {
        this.name = name;
        this.quota = quota;
    }

    public static int getQuotaByName(String name) {
        for (UserPlanType plan : values()) {
            if (plan.getName().equals(name)) return plan.getQuota();
        }
        throw new ApiCommonException(GlobalErrorCode.NOT_FOUND_PLAN);
    }
}

