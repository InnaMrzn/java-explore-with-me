package ru.practicum.service;

import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto cat);

    CategoryDto update(CategoryDto cat);

    void delete(Long catId);

    List<CategoryDto> findCategories(int from, int size);

    CategoryDto findCategory(Long catId);
}
