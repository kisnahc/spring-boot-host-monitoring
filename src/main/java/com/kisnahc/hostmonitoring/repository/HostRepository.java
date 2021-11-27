package com.kisnahc.hostmonitoring.repository;

import com.kisnahc.hostmonitoring.domain.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HostRepository extends JpaRepository<Host, Long> {

    @Query("select count(h.id)from Host as h")
    long findCount();

}
