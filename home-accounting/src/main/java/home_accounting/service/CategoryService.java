package home_accounting.service;


import home_accounting.dto.request.CategoryRequestDTO;
import home_accounting.dto.response.CategoryResponseDTO;
import home_accounting.entity.Category;
import home_accounting.entity.enums.TransactionType;
import home_accounting.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {


    private final CategoryRepository categoryRepository;

    private CategoryResponseDTO toDto(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getType(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public List<CategoryResponseDTO> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<CategoryResponseDTO> getByType(TransactionType type) {
        return categoryRepository.findByType(type)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public CategoryResponseDTO getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategoriya topilmadi: " + id));
        return toDto(category);
    }

    public CategoryResponseDTO create(CategoryRequestDTO request) {

        if (categoryRepository.existsByName(request.name())) {
            throw new RuntimeException("Bu nomdagi categoriya mavjud: " + request.name());
        }

        Category category = Category.builder()
                .name(request.name())
                .type(request.type())
                .build();

        return toDto(categoryRepository.save(category));
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategoriya topilmadi: " + id));
        category.setName(request.name());
        category.setType(request.type());

        return toDto(categoryRepository.save(category));

    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Kategoriya topilmadi: " + id);
        }
        categoryRepository.deleteById(id);
    }

}
