package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
@Validated

public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@Valid @NotNull @RequestBody NewCategoryDto body) {
        log.info("POST /categories");
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.create(body));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteCategory(@PathVariable("catId") Long catId) {
        log.info("DELETE /categories/" + catId);
        categoryService.delete(catId);
    }

    @PatchMapping
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto catDto) {
        log.info("UPDATE /categories" + " category id=" + catDto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.update(catDto));
    }


}
