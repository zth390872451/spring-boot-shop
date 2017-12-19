package com.svlada.component.repository;

import com.svlada.entity.product.DetailsImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetailsImageRepository extends JpaRepository<DetailsImage, Long> {

    @Query(value = "select * from details_image di  where di.product_id = ?1 ORDER BY id ASC ",nativeQuery = true)
    List<DetailsImage> findAllByProductIdOrderById(Long productId);

    void deleteAllByProductId(Long productId);
}
