package com.erp.erp.domain.model.item;

import com.erp.erp.application.dto.ProductDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long> {
    Optional<ProductMaster> findByProductMasterId(Long ProductMasterId);

    Page<ProductDto> findByBrandIgnoreCase(String brand, Pageable pageable);

}
