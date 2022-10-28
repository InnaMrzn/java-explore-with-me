package ru.practicum.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.dto.mappers.CategoryMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.WrongActionException;
import ru.practicum.model.Category;
import ru.practicum.repo.CategoryRepository;
import ru.practicum.repo.EventRepository;
import ru.practicum.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<CategoryDto> findCategories(int from, int size) {
        int page = from / size;
        Pageable paging = PageRequest.of(page, size);
        List<Category> categories = categoryRepository.findAll(paging).getContent();
        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto findCategory(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Категория с Id=" + catId + " не найдена."));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto catDto) {
        Category oldCat = categoryRepository.findById(catDto.getId()).orElseThrow(() ->
                new NotFoundException("Категория с id= " + catDto.getId() + " не найдена."));
        if (catDto.getName().equalsIgnoreCase(oldCat.getName())) {
            throw new WrongActionException("категория с таким именем \"" + catDto.getName() + "\" уже существует");
        }
        Category cat = CategoryMapper.updateCategory(catDto, oldCat);
        return CategoryMapper.toCategoryDto(categoryRepository.save(cat));
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Категория с id=" + catId + " не найдена."));
        if (eventRepository.countByCategory_Id(catId) > 0) {
            throw new WrongActionException("Невозможно удалить категорию id=" + catId + " в ней содержатся события");
        }
        categoryRepository.deleteById(catId);

    }


}
