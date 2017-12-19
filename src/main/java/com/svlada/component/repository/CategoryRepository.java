package com.svlada.component.repository;

import com.svlada.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findOneByName(String name);

}
