package app.controllers;

import app.dtos.InventoryDTO;
import app.handlers.NotFoundException;
import app.services.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

@Controller
@RequestMapping("/api/inv")
public class InventoryController {

    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService invService;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    @Autowired
    public InventoryController(InventoryService invService) {
        this.invService = invService;
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
    public ResponseEntity<String> getAllProducts(Model model, @RequestParam Long shopId) {
        log.info("Создан запрос на список продуктов из магазина с id : {}", shopId);
        try {
            List<InventoryDTO> inventories = invService.getAllProducts(shopId);
            model.addAttribute("inventories", inventories);
            model.addAttribute("shopId", shopId);
            String htmlContent = renderHtmlTemplate("getAllProductsResult", model);
            return ResponseEntity.ok(htmlContent);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
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
    public ResponseEntity<String> buyProduct(@RequestParam Long shopId,
                             @RequestParam Long productId,
                             @RequestParam Integer count,
                             Model model) {
        log.info("Создан запрос на покупку продукта(-ов) в магазин с id : {}", shopId);
        try {
            String resultMessage = invService.addProductToInventory(shopId, productId, count);
            model.addAttribute("resultMessage", resultMessage);
            model.addAttribute("shopId", shopId);
            String htmlContent = renderHtmlTemplate("buyProductResult", model);
            return ResponseEntity.ok(htmlContent);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
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
    public ResponseEntity<String> sellProduct(Model model, @RequestParam Long shopId, @RequestParam Long productId, @RequestParam Integer count) {
        log.info("Создан запрос на продажу продукта(-ов) из магазина с id: {}", shopId);
        try {
            String resultMessage = invService.removeProductFromInventory(shopId, productId, count);
            model.addAttribute("resultMessage", resultMessage);
            model.addAttribute("shopId", shopId);
            String htmlContent = renderHtmlTemplate("sellProductResult", model);
            return ResponseEntity.ok(htmlContent);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
    }

    private String renderHtmlTemplate(String templateName, Model model) {
        Context context = new Context();
        model.asMap().forEach(context::setVariable);
        return thymeleafTemplateEngine.process(templateName, context);
    }
}