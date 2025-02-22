package com.saran.Agreegator.Repository;

import com.saran.Agreegator.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
