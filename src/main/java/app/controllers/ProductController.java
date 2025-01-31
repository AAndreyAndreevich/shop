package app.controllers;

import app.services.InventoryService;
import app.services.ProductService;
import app.services.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final InventoryService invService;
    private final ShopService shopService;
    private final ProductService productService;

    @Autowired
    public ProductController(InventoryService invService, ShopService shopService, ProductService productService) {
        this.invService = invService;
        this.shopService = shopService;
        this.productService = productService;
    }

    /**
     * Создает новый продукт
     * @param name имя
     * @param price цена
     * @return результат запроса
     */
    @PostMapping("/createProduct")
    public String createProduct(Model model, @RequestParam String name, @RequestParam BigDecimal price) {
        log.info("Создан запрос на создание продукта с именем : {}", name);
        return productService.createProduct(name, price);
    }
}
