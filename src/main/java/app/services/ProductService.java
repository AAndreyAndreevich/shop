package app.services;

import app.entities.Product;
import app.repositories.InventoryRepository;
import app.repositories.ProductRepository;
import app.repositories.ShopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepo;
    private final ShopRepository shopRepo;
    private final InventoryRepository invRepo;

    @Autowired
    public ProductService(ProductRepository productRepo, ShopRepository shopRepo, InventoryRepository invRepo) {
        this.productRepo = productRepo;
        this.shopRepo = shopRepo;
        this.invRepo = invRepo;
    }

    @Transactional
    public String createProduct(String name, BigDecimal price) {
        if (name.isEmpty()) {
            log.warn("Попытка создать продукт с пустым названием");
            return "Название магазине не может быть пустым";
        }
        if (productRepo.existsByName(name)) {
            log.warn("Продукт с названием '{}' уже существует", name);
            return "Продукт с таким названием уже существует";
        }
        Product product = new Product(name, price);
        productRepo.save(product);
        log.info("Продукт '{}' создан", product.getName());
        return "Новый продукт успешно создан";
    }

    @Transactional
    public String deleteProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Такого продукта не существует"));
        productRepo.delete(product);
        log.info("Продукт '{}' был удален", product.getName());
        return "Продукт удален";
    }
}
