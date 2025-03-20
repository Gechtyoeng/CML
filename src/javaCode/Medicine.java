package javaCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Attributes.Name;

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

    public void addMedicineByID(Medicine med){
        boolean found = false;
        for(Medicine existingMed : medList){
            if(existingMed.getMedicineID() == med.getMedicineID()){
                existingMed.setStock(existingMed.getStock() + med.getStock());
                found = true;
                System.out.println("Medicine " + med.getMedicineName() + " added sucessfully!");   
                break;             
            }

            if(!found){
                medList.add(med);
                System.out.println("Medicine " + med.getMedicineName() + " added sucessfully!");
            }
        }

    }
    public void addMedicineByName(Medicine med){
        boolean found = false;
        for(Medicine existingMed : medList){
            if(existingMed.getMedicineName().equalsIgnoreCase(med.getMedicineName())){
                existingMed.setStock(existingMed.getStock() + med.getStock());
                found = true;
                System.out.println("Medicine " + med.getMedicineName() + " added sucessfully!");   
                break;             
            }

            if (!found) {
                medList.add(med);
                System.out.println("Medicine " + med.getMedicineName() + " added successfully!");
            }
        }
    }

    public void removeMedicineByID(int medicineId){
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

    public void removeMedicineByName(String medicineName){
        Iterator <Medicine> iterator = medList.iterator();

        while (iterator.hasNext()) {
            Medicine med = iterator.next();
            if(med.getMedicineName().equalsIgnoreCase(medicineName)){
                iterator.remove();
                System.out.println("Medicine Name " + med.getMedicineName() + " removed sucessfully!");
                return;
            }
        }
        System.out.println("Medicine with Name " + medicineName + " not found.");
    }

    public void updateMedicineByID(int medicineId, double newPrice, int newStock, LocalDate newExpiryDate, String newDescription){
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

    public void updateMedicineByName(String medicineName, double newPrice, int newStock, LocalDate newExpiryDate, String newDescription){
        for(Medicine med : medList){
            if(med.getMedicineName().equalsIgnoreCase(medicineName)){
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

    public void searchMedByID(int medicineId){
        for(Medicine med : medList){
            if(med.getMedicineID() == medicineId){
                System.out.println("Item found: " + med.getMedicineName());
            }
        }
        System.out.println("Medicine with ID " + medicineId + " not found.");
    }

    public void searchMedByName(String medicineName){
        for(Medicine med : medList){
            if(med.getMedicineName().equalsIgnoreCase(medicineName)){
                System.out.println("Item found: " + med.getMedicineName());
            }
        }
        System.out.println("Medicine with name " + medicineName + " not found.");
    }

}
