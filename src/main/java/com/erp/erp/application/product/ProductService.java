package com.erp.erp.application.product;

import com.erp.erp.application.dto.ProductDto;
import com.erp.erp.domain.model.item.ProductMaster;
import com.erp.erp.domain.model.item.ProductMasterRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {
    private final ProductMasterRepository productMasterRepository;

    public Page<ProductDto> getByBrand(String brand, int page) {
        Pageable pg = PageRequest.of(page, 10, Sort.by("productName").ascending());
        return productMasterRepository
            .findByBrandIgnoreCase(brand, pg);
    }
}
