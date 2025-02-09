package app.services;

import app.entities.Shop;
import app.handlers.NotFoundException;
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
public class ShopService {

    private static final Logger log = LoggerFactory.getLogger(ShopService.class);
    private final ShopRepository shopRepo;

    @Autowired
    public ShopService(ShopRepository shopRepo) {
        this.shopRepo = shopRepo;
    }

    @Transactional
    public String createShop(String name) {
        if (name.isEmpty()) {
            log.warn("Попытка создать магазин с пустым названием");
            return "Название магазине не может быть пустым";
        }
        if (shopRepo.existsByName(name)) {
            log.warn("Магазин с названием '{}' уже существует", name);
            return "Магазин с таким названием уже существует";
        }
        Shop shop = new Shop(name);
        shop.setBalance(new BigDecimal(1000));
        shopRepo.save(shop);
        log.info("Магазин '{}' создан", shop.getName());
        return "Новый магазин успешно создан";
    }

    @Transactional
    public String deleteShop(Long shopId) {
        Shop shop = shopRepo.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Такого магазине не существует"));
        shopRepo.delete(shop);
        log.info("Магазин '{}' удален", shop.getName());
        return "Магазин удален";
    }
}
