package com.svlada.component.repository;

import com.svlada.entity.product.MajorImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MajorImageRepository extends JpaRepository<MajorImage, Long> {

    @Query(value = "select mi.image_url from major_image mi  where mi.product_id = ?1 order by mi.id asc limt 1",nativeQuery = true)
    String findFirstImageUrlByProductIdOrderByIdAsc(Long product_id);


    @Query(value = "select * from major_image mi  where mi.product_id = ?1 order by mi.id asc ",nativeQuery = true)
    List<MajorImage> findAllByProductIdOrderById(Long product_id);

    void deleteAllByProductId(Long productId);
}
