package com.saran.Agreegator.Controllers;

import com.saran.Agreegator.Dtos.Response;
import com.saran.Agreegator.Dtos.SubCategoryDto;
import com.saran.Agreegator.Services.SubCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subcategories")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @PostMapping("/create")
    public ResponseEntity<Response> createSubCategory(@RequestBody @Valid SubCategoryDto subCategoryDto) {
        return ResponseEntity.ok(subCategoryService.createSubCategory(subCategoryDto));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<Response> getAllSubCategories() {
        return ResponseEntity.ok(subCategoryService.getAllSubCategories());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<Response> getSubCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateSubCategory(@PathVariable Long id, @RequestBody @Valid SubCategoryDto subCategoryDto) {
        return ResponseEntity.ok(subCategoryService.updateSubCategory(id, subCategoryDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteSubCategory(@PathVariable Long id) {
        return ResponseEntity.ok(subCategoryService.deleteSubCategory(id));
    }
}

