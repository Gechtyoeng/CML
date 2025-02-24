package javaCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Medicine {
    private int medicineId;
    private String medicineName;
    private String dosage;
    private double price;
    private int stock;
    private LocalDate expiryDate;
    private String description;

    // Constructor
    public Medicine(int medicineId, String medicineName, String dosage, double price, int stock, LocalDate expiryDate, String description) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.price = price;
        this.stock = stock;
        this.expiryDate = expiryDate;
        this.description = description;
    }

    private static List<Medicine> medList = new ArrayList<>();

    // getter
    public int getMedicineID(){
        return medicineId;
    }
    public String getMedicineName(){
        return medicineName;
    }
    public String getDosage(){
        return dosage;
    }
    public double getPrice(){
        return price;
    }
    public int getStock(){
        return stock;
    }
    public LocalDate getExpiryDate(){
        return expiryDate;
    }
    public String getDescription() {
        return description;
    }

    //setter
    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }
    public void setName(String medicineName) {
        this.medicineName = medicineName;
    }
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    //method
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public void veiwMedicinInfoByID(int medicineId){
        for(Medicine med : medList){
            if(med.getMedicineID() == medicineId){
                med.veiwMedicinInfo();
                return;
            }
        }
        System.out.println("Medicine with ID " + medicineId + " not found.");
    }

    public void veiwMedicinInfoByName(String medicineName){
        for(Medicine med : medList){
            if(med.getMedicineName().equalsIgnoreCase(medicineName)){
                med.veiwMedicinInfo();
                return;
            }
        }
        System.out.println("Medicine with ID " + medicineId + " not found.");
    }

    public void veiwMedicinInfo(){
        System.out.println("Medicine ID: " + medicineId);
        System.out.println("Name: " + medicineName);
        System.out.println("Dosage: " + dosage);
        System.out.println("Price: $" + price);
        System.out.println("Stock: " + stock);
        System.out.println("Expiry Date: " + expiryDate);
        System.out.println("Expired: " + (isExpired() ? "Yes" : "No"));
        System.out.println("Description: " + description);
    }

    public void addMedicine(Medicine med){
        medList.add(med);
        System.out.println("Medicine " + med.getMedicineName() + " added sucessfully!");
    }

    public void removeMedicine(int medicineId){
        Iterator <Medicine> iterator = medList.iterator();

        while (iterator.hasNext()) {
            Medicine med = iterator.next();
            if(med.getMedicineID() == medicineId){
                iterator.remove();
                System.out.println("Medicine ID " + med.getMedicineID() + " removed sucessfully!");
                return;
            }
        }
        System.out.println("Medicine with ID " + medicineId + " not found.");
    }

    public void updateMedicine(int medicineId, double newPrice, int newStock, LocalDate newExpiryDate, String newDescription){
        for(Medicine med : medList){
            if(med.getMedicineID() == medicineId){
                med.setPrice(newPrice);
                med.setStock(newStock);
                med.setExpiryDate(newExpiryDate);
                med.setDescription(newDescription);
                return;
            }
        }
        System.out.println("Medicine with ID " + medicineId + " not found.");
    }

    public void displayAllTheMedicine(){
        if(medList.isEmpty()){
            System.out.println("Database is empty!");
            return;
        }
        for(Medicine med : medList){
            med.veiwMedicinInfo();
        }
    }

    public void medStockLevel(){
        int medLowStock = 5;

        for(Medicine med : medList){
            if(med.getStock() <= medLowStock){
                System.out.println(med.getMedicineName() + ": Low stock (only " + med.getStock() + " left)");
            }else{
                System.out.println(med.getMedicineName() + ": Sufficient Stock (" + med.getStock() + " left)");
            }
        }
    }

}
