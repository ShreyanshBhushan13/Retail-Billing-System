package in.smitapriya.billingsoftware.service;

import in.smitapriya.billingsoftware.io.CategoryRequest;
import in.smitapriya.billingsoftware.io.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    CategoryResponse add(CategoryRequest request, MultipartFile file);
    List<CategoryResponse> read();

    void delete(String categoryId);

}
