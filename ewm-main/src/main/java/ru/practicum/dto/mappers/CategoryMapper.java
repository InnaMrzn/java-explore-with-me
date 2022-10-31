package ru.practicum.dto.mappers;

import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.model.Category;

public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category updateCategory(CategoryDto catDto, Category cat) {
        if (catDto.getName() != null) {
            cat.setName(catDto.getName());
        }
        return cat;
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(newCategoryDto.getName());
    }
}
