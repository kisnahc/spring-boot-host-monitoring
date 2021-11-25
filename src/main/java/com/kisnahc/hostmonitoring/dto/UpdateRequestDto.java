package com.kisnahc.hostmonitoring.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateRequestDto {

    @NotBlank(message = "공백을 사용할 수 없습니다.")
    private String hostName;
}
