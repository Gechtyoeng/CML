package javaCode;

public class PerscriptionMedicine {
    private Medicine medicine;
    private int quantity;

    // Constructor
    public PerscriptionMedicine(Medicine medicine, int quantity) {
        this.medicine = medicine;
        this.quantity = quantity;
    }

    // Getters
    public Medicine getMedicine() {
        return medicine;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Medicine: " + medicine.getMedicineName() + ", Quantity: " + quantity;
    }
}
