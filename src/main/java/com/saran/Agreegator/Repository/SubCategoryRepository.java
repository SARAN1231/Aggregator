package com.saran.Agreegator.Repository;

import com.saran.Agreegator.Models.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    Optional<SubCategory> findByName(String subCategoryName);
}
