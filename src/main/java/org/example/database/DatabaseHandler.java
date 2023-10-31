package org.example.database;

import org.example.entity.Manufacturer;
import org.example.entity.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class DatabaseHandler {

    private final ManufacturerTableHandler mfTable;
    private final ProductTableHandler pdTable;

    public DatabaseHandler(String mfTable, String pdTable) {
        this.mfTable = new ManufacturerTableHandler(mfTable);
        this.pdTable = new ProductTableHandler(pdTable);
    }

    public Product readProductById(long id) {
        return readAllProducts().stream()
                .filter(pd -> pd.getId() == id)
                .findAny()
                .orElse(null);
    }

    public Manufacturer readManufacturerById(long id) {
        return readAllManufacturers().stream()
                .filter(mf -> mf.getId() == id)
                .findAny()
                .orElse(null);
    }

    public Manufacturer readManufacturerByName(String name) {
        return readAllManufacturers().stream()
                .filter(mf -> mf.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    public void deleteManufacturerAndAllProducts(Manufacturer manufacturer) {
        readAllManufacturerProducts(manufacturer).forEach(this::delete);
        delete(manufacturer);
    }

    public List<Product> readAllManufacturerProducts(Manufacturer manufacturer) {
        return readAllProducts().stream()
                .filter(pd -> pd.getManufacturerId() == manufacturer.getId())
                .collect(Collectors.toList());
    }

    public List<Product> readAllProductsByCountry(String country) {
        return readAllProductManufacturerMap().entrySet().stream()
                .filter(set -> set.getValue().getCountry().equals(country))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Manufacturer> readAllManufacturersWherePriceLessThan(double price) {
        return readAllManufacturerProductsMap().entrySet().stream()
                .filter(set -> set.getValue().stream()
                        .map(pd -> pd.getPrice() < price)
                        .reduce(true, (bool1, bool2) -> bool1 && bool2)
                ).map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Map<Product, Manufacturer> readAllProductManufacturerMapByYear(String year) {
        return readAllProductManufacturerMap().entrySet().stream()
                .filter(set -> yearEqualDate(year, set.getKey().getDate()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Manufacturer> readAllManufacturersByProductNameAndYear(String name, String year) {
        return readAllProductManufacturerMap().entrySet().stream()
                .filter(set -> set.getKey().getName().equals(name)
                        && yearEqualDate(year, set.getKey().getDate())
                )
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Product> readAllProductsByYear(String year) {
        return readAllProducts().stream()
                .filter(pd -> yearEqualDate(year, pd.getDate()))
                .collect(Collectors.toList());
    }

    private boolean yearEqualDate(String year, String date) {
        return date.substring(0, 4).equals(year);
    }


    // Product <-> Manufacturer | One <-> One
    public Map<Product, Manufacturer> readAllProductManufacturerMap() {
        // Use one list in order not to load every time from DB
        List<Manufacturer> manufacturerList = readAllManufacturers();
        // Create map
        return readAllProducts().stream()
                .collect(Collectors.toMap(
                        pd -> pd,
                        pd -> manufacturerList.stream()
                                .filter(mf -> mf.getId() == pd.getManufacturerId())
                                .findAny()
                                .get())
                );
    }

    // Manufacturer <-> Product | One <-> Many
    public Map<Manufacturer, List<Product>> readAllManufacturerProductsMap() {
        // Use one list in order not to load every time from DB
        List<Product> productList = readAllProducts();
        // Create map
        return readAllManufacturers().stream()
                .collect(Collectors.toMap(
                        mf -> mf,
                        mf -> productList.stream()
                                .filter(pd -> pd.getManufacturerId() == mf.getId())
                                .collect(Collectors.toList())
                ));
    }

    // Manufacturer related methods
    public void create(Manufacturer entity) {
        // Load all manufacturers from DB
        List<Manufacturer> manufacturerList = mfTable.loadAll();
        // Don't create if matching name exists
        for (Manufacturer manufacturer: manufacturerList)
            if (manufacturer.getName().equals(entity.getName()))
                return;

        // Generate id
        long generatedId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        entity.setId(generatedId);

        // Save to DB
        manufacturerList.add(entity);
        mfTable.saveAll(manufacturerList);
    }

    public List<Manufacturer> readAllManufacturers() {
        // No logic for load
        return mfTable.loadAll();
    }

    public void update(Manufacturer entity) {
        // Load all manufacturers from DB
        List<Manufacturer> manufacturerList = mfTable.loadAll();
        // Don't update if matching name exists
        for (Manufacturer manufacturer: manufacturerList)
            if (manufacturer.getName().equals(entity.getName()))
                return;
        // If same id, replace
        for (int i = 0; i < manufacturerList.size(); i++) {
            Manufacturer manufacturer = manufacturerList.get(i);
            if (manufacturer.getId() == entity.getId())
                manufacturerList.set(i, entity);
        }

        // Save to DB
        manufacturerList.add(entity);
        mfTable.saveAll(manufacturerList);
    }

    public void delete(Manufacturer entity) {
        // Load all manufacturers from DB
        List<Manufacturer> manufacturerList = mfTable.loadAll();
        // Remove entity with matching id
        manufacturerList.removeIf(manufacturer -> manufacturer.getId() == entity.getId());

        // Save to DB
        mfTable.saveAll(manufacturerList);
    }

    // Product related methods
    public void create(Product entity) {

        List<Product> productList = pdTable.loadAll();

        // Don't create if specified manufacturer doesn't exist
        int size = mfTable.loadAll().stream()
                .filter(mf -> mf.getId() == entity.getManufacturerId())
                .toList()
                .size();
        if (size != 1)
            return;

        // Don't create products with same name and manufacturer
        size = productList.stream()
                .filter(prd -> prd.getName().equals(entity.getName()))
                .filter(prd -> prd.getManufacturerId() == entity.getManufacturerId())
                .toList()
                .size();
        if (size > 0)
            return;

        long generatedId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        entity.setId(generatedId);

        // Save to DB
        productList.add(entity);
        pdTable.saveAll(productList);
    }

    public List<Product> readAllProducts() {
        // No logic present
        return pdTable.loadAll();
    }

    public void update(Product entity) {

        List<Product> productList = pdTable.loadAll();

        // Remove to be updated product
        productList.removeIf(prd -> prd.getId() == entity.getId());

        // Don't update if specified manufacturer doesn't exist
        int size = mfTable.loadAll().stream()
                .filter(mf -> mf.getId() == entity.getManufacturerId())
                .toList()
                .size();
        if (size != 1)
            return;

        // Don't update products with same name and manufacturer
        size = productList.stream()
                .filter(prd -> prd.getName().equals(entity.getName()))
                .filter(prd -> prd.getManufacturerId() == entity.getManufacturerId())
                .toList()
                .size();
        if (size > 0)
            return;

        // Save to DB
        productList.add(entity);
        pdTable.saveAll(productList);
    }

    public void delete(Product entity) {

        List<Product> productList = pdTable.loadAll();

        // Remove same product
        productList.removeIf(prd -> prd.getId() == entity.getId());

        // Save to DB
        pdTable.saveAll(productList);
    }

}
