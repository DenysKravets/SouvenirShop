package org.example.database;

import org.example.entity.Product;
import org.example.factory.ProductFactory;

public class ProductTableHandler extends TableHandler<Product> {
    public ProductTableHandler(String product_file) {
        super(product_file, new ProductFactory());
    }
}
