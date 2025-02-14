package app.dtos;

import java.math.BigDecimal;

public class InventoryOperationResult {

    private String message;
    private BigDecimal remainingBalance;
    private String productName;
    private int addedQuantity;

    public InventoryOperationResult(String message, BigDecimal remainingBalance, String productName, int addedQuantity) {
        this.message = message;
        this.remainingBalance = remainingBalance;
        this.productName = productName;
        this.addedQuantity = addedQuantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAddedQuantity() {
        return addedQuantity;
    }

    public void setAddedQuantity(int addedQuantity) {
        this.addedQuantity = addedQuantity;
    }
}
