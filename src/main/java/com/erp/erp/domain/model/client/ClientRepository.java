package com.erp.erp.domain.model.client;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

  Optional<Client> findByEmail(String clientEmail);

}
