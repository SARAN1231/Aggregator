package com.saran.Agreegator.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Products")
public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank(message = "Product name is required")
        @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
        private String name;

        @NotBlank(message = "Barcode is required")
        @Column(unique = true, nullable = false)
        private String barcode;

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        private String description;

        @NotNull(message = "MRP price is required")
        @Positive(message = "MRP price must be a positive value")
        private Double mrpPrice;

        @NotNull(message = "Selling price is required")
        @Positive(message = "Selling price must be a positive value")
        private Double sellingPrice;

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity cannot be negative")
        private Integer quantity;

        @FutureOrPresent(message = "Expiry date cannot be in the past")
        private LocalDate expiryDate;

        @NotBlank(message = "Image URL is required")
        private String imageUrl;

        @NotNull(message = "Inventory count is required")
        @Min(value = 0, message = "Inventory count cannot be negative")
        private Integer inventoryCount;

        @ManyToOne
        @JoinColumn(name = "sub_category_id", nullable = false)
        @NotNull(message = "Sub-category is required")
        private SubCategory subCategory;

        @Override
        public String toString() {
                return "Product{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", barcode='" + barcode + '\'' +
                        ", description='" + description + '\'' +
                        ", mrpPrice=" + mrpPrice +
                        ", sellingPrice=" + sellingPrice +
                        ", quantity=" + quantity +
                        ", expiryDate=" + expiryDate +
                        ", imageUrl='" + imageUrl + '\'' +
                        ", inventoryCount=" + inventoryCount +
                        '}';
        }
}
