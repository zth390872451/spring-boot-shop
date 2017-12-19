package com.svlada.component.repository;

import com.svlada.entity.ShopCart;
import com.svlada.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ShopCartRepository extends JpaRepository<ShopCart, Long> {

    @Transactional
    @Modifying
    void deleteAllByUserIdAndProductIdIn(Long userId, Long[] productIds);

    @Query(value = "select sc.* from shop_cart sc WHERE sc.product_id = ?2 AND sc.user_id = ?1 order by sc.create_date",nativeQuery = true)
    ShopCart findOneByUserIdAndProduct(Long userId, Long productId);

    void deleteAllByUserId(Long userId);

    @Query(value = "select p.* from shop_cart sc inner join product p on  sc.product_id = p.id where sc.user_id = ?1 order by sc.create_date",nativeQuery = true)
    List<Product> findAllProductByUserId(Long userId);

    List<ShopCart> findAllByUserId(Long userId);
}
