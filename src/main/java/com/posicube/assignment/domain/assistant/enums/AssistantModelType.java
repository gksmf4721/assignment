package com.posicube.assignment.domain.assistant.enums;

import com.posicube.assignment.error.GlobalErrorCode;
import com.posicube.assignment.error.exception.ApiCommonException;
import lombok.Getter;

@Getter
public enum AssistantModelType {
    GPT_5("gpt-5", 0.25),
    GPT_4O_MINI("gpt-4o-mini", 0.15);

    private final String name;
    private final double pricePerK;

    AssistantModelType(String name, double pricePerK) {
        this.name = name;
        this.pricePerK = pricePerK;
    }

    public static double getPriceByName(String name) {
        for (AssistantModelType model : values()) {
            if (model.getName().equals(name)) return model.getPricePerK();
        }
        throw new ApiCommonException(GlobalErrorCode.NOT_FOUND_ASSISTANT_MODEL);
    }

    public static boolean existsPriceByName(String name) {
        for (AssistantModelType model : values()) {
            if (model.getName().equals(name)) return true;
        }
        return false;
    }
}
