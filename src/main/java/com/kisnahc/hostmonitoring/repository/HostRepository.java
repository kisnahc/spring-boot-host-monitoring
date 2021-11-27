package com.kisnahc.hostmonitoring.repository;

import com.kisnahc.hostmonitoring.domain.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host, Long> {


}
