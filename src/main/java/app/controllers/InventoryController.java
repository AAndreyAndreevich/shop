package app.controllers;

import app.dtos.InventoryDTO;
import app.dtos.ProductDTO;
import app.services.InventoryService;
import app.services.ProductService;
import app.services.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/inv")
public class InventoryController {

    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService invService;
    private final ShopService shopService;
    private final ProductService productService;

    @Autowired
    public InventoryController(InventoryService invService, ShopService shopService, ProductService productService) {
        this.invService = invService;
        this.shopService = shopService;
        this.productService = productService;
    }

    /**
     * Нужен для создания формы
     * @return форму страницы
     */
    @GetMapping("/getAllProductsForm")
    public String getAllProductsForm() {
        return "getAllProductsForm";
    }

    /**
     * Создает запрос на вызов списка продукта
     * @param shopId магазин
     * @return результат запроса
     */
    @GetMapping("/getAllProducts")
    public String getAllProducts(Model model, @RequestParam Long shopId) {
        log.info("Создан запрос на список продуктов из магазина с id : {}", shopId);
        List<InventoryDTO> inventories = invService.getAllProducts(shopId);
        model.addAttribute("inventories", inventories);
        model.addAttribute("shopId", shopId);
        return "getAllProductsResult";
    }

    /**
     * Нужен для создания формы
     * @return форму страницы
     */
    @GetMapping("/buyProductForm")
    public String buyProductForm() {
        return "buyProductForm";
    }

    /**
     * Создает запрос на покупку продукта из магазина
     * @param shopId магазин
     * @param productId продукт
     * @param count количество
     * @return результат запроса
     */
    @PostMapping("/buyProduct")
    public String buyProduct(@RequestParam Long shopId,
                             @RequestParam Long productId,
                             @RequestParam Integer count,
                             Model model) {
        log.info("Создан запрос на покупку продукта(-ов) в магазин с id : {}", shopId);

        String resultMessage = invService.addProductToInventory(shopId, productId, count);

        model.addAttribute("resultMessage", resultMessage);
        model.addAttribute("shopId", shopId);

        return "buyProductResult";
    }

    /**
     * Нужен для создания формы
     * @return форму страницы
     */
    @GetMapping("/sellProductForm")
    public String sellProductForm() {
        return "sellProductForm";
    }

    /**
     * Создает запрос на продажу продукта из магазина
     * @param shopId магазин
     * @param productId продукт
     * @param count количество
     * @return результат запроса
     */
    @PostMapping("/sellProduct")
    public String sellProduct(Model model, @RequestParam Long shopId, @RequestParam Long productId, @RequestParam Integer count) {
        log.info("Создан запрос на продажу продукта(-ов) из магазина с id: {}", shopId);

        String resultMessage = invService.removeProductFromInventory(shopId, productId, count);

        model.addAttribute("resultMessage", resultMessage);
        model.addAttribute("shopId", shopId);

        return "sellProductResult";
    }
}