package com.saran.Agreegator.Repository;

import com.saran.Agreegator.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByName(String name);

    Optional<Category> findByName(String categoryName);
}
