package ru.practicum.controller.publics;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    ResponseEntity<List<CategoryDto>> getCategories(
            @RequestParam(value = "from", required = false,
                    defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        log.info("GET /categories");
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findCategories(from, size));
    }

    @GetMapping("/{catId}")
    ResponseEntity<CategoryDto> getCategory(@PathVariable("catId") Long catId) {
        log.info("GET /category/" + catId);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findCategory(catId));
    }


}
