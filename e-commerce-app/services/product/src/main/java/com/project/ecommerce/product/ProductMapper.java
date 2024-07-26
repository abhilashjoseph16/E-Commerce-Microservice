package com.project.ecommerce.product;

import org.springframework.stereotype.Service;

import com.project.ecommerce.category.Category;

@Service
public class ProductMapper {

    public Product toProduct(ProductRequest request) {
        return Product.builder()
                        .id(request.id())
                        .name(request.name())
                        .description(request.name())
                        .price(request.price())
                        .availableQuantity(request.availableQuantity())
                        .category(Category.builder()
                                            .id(request.categoryId())
                                            .build())
                        .build();
    }

    public ProductResponse toProductResponse(Product product){
        return new ProductResponse(product.getId(), 
                                    product.getName(), 
                                    product.getDescription(), 
                                    product.getAvailableQuantity(), 
                                    product.getPrice(), 
                                    product.getCategory().getId(), 
                                    product.getCategory().getName(), 
                                    product.getCategory().getDescription());
    


    }

    public ProductPurchaseResponse toProductPurchaseResponse(Product product, double quantity) {
        return new ProductPurchaseResponse(product.getId(),
                                        product.getName(),
                                        product.getDescription(),
                                        product.getPrice(),
                                        quantity);
    }

}
