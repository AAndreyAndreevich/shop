package app.controllers;

import app.services.InventoryService;
import app.services.ProductService;
import app.services.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

    private static final Logger log = LoggerFactory.getLogger(ShopController.class);

    private final InventoryService invService;
    private final ShopService shopService;
    private final ProductService productService;

    @Autowired
    public ShopController(InventoryService invService, ShopService shopService, ProductService productService) {
        this.invService = invService;
        this.shopService = shopService;
        this.productService = productService;
    }

    /**
     * Создает новый магазин
     * @param name имя
     * @return результат запроса
     */
    @PostMapping("/createShop")
    public String createShop(Model model, @RequestParam String name) {
        log.info("Создан запрос на создание магазина с именем : {}", name);
        return shopService.createShop(name);
    }

    /**
     * Удаляет существующий магазин
     * @param shopId id магазина
     * @return результат запроса
     */
    @DeleteMapping("/deleteShop")
    public String deleteShop(Model model, @RequestParam Long shopId) {
        log.info("Создан запрос на удаление магазина с id : {}", shopId);
        return shopService.deleteShop(shopId);
    }
}