package com.posicube.assignment.common.dto;

public record CommonResDto(String result) {

    public static CommonResDto success() {
        return new CommonResDto("success");
    }

    public static CommonResDto failure() {
        return new CommonResDto("failure");
    }

    public static CommonResDto available() {
        return new CommonResDto("available");
    }

    public static CommonResDto unavailable() {
        return new CommonResDto("unavailable");
    }
}