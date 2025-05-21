package com.dailyalcorcode.buynowdotcom.service.category;

import com.dailyalcorcode.buynowdotcom.model.Category;

import java.util.List;

public interface ICategoryService {

    Category addCategory(Category category);

    Category updateCategory(Long categoryId, Category category);

    void deleteCategory(Long categoryId);

    List<Category> getAllCategories();

    Category findCategoryByName(String name);

    Category findCategoryById(Long categoryId);
}
