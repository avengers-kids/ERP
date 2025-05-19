package com.erp.erp.domain.model.item;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneDetailsRepository extends JpaRepository<PhoneDetails, Long> {
    Optional<PhoneDetails> findByPhoneDetailsId(Long phoneDetailsId);

}
