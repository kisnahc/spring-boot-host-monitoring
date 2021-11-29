package com.kisnahc.hostmonitoring.dto;

import lombok.Data;

@Data
public class DeleteHostResponseDto {

    private Long deleteHostId;

    public DeleteHostResponseDto(Long deleteHostId) {
        this.deleteHostId = deleteHostId;
    }
}
