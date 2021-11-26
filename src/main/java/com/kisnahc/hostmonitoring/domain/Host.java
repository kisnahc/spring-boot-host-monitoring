package com.kisnahc.hostmonitoring.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Host extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "host_id")
    private Long id;

    @NotBlank(message = "공백을 사용할 수 없습니다.")
    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String address;

    private HostStatus hostStatus;

    @Builder
    public Host(String name, String address) {
        this.name = name;
        this.address = address;
    }

    /* 수정 메서드 */
    public void updateHostName(String hostName) {
        this.name = hostName;
    }

    /* 호스트 상태 변경 메서드 */
    public void setStatus(HostStatus status) {
        this.hostStatus = status;
    }

}
