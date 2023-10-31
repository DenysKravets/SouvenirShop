package org.example.database;

import org.assertj.core.data.Offset;
import org.example.entity.Manufacturer;
import org.example.entity.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseTest {

    private static final String mfFile = "manufacturers_test.json";
    private static final String pFFile = "products_test.json";
    private DatabaseHandler dbHandler;

    @BeforeEach
    public void setupDatabase() throws IllegalAccessException {

        // Clear tables before each run
        deleteTables();

        dbHandler = new DatabaseHandler(mfFile, pFFile);
    }

    @AfterAll
    public static void deleteTables() throws IllegalAccessException {
        // Delete tables after use
        deleteFileIfExists(mfFile);
        deleteFileIfExists(pFFile);
    }

    public static void deleteFileIfExists(String fileStr) throws IllegalAccessException {
        File file = new File(fileStr);
        if (file.exists())
            if (!file.delete())
                throw new IllegalAccessException("Cannot access file: " + mfFile);
    }

    // Manufacturer tests
    @Test
    public void testCreateManufacturer() {

        String name = "Nintendo";
        String country = "Japan";

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName(name)
                .setCountry(country)
                .build();

        dbHandler.create(manufacturer);

        manufacturer = dbHandler.readAllManufacturers().get(0);

        assertThat(manufacturer.getName()).isEqualTo(name);
        assertThat(manufacturer.getCountry()).isEqualTo(country);
    }

    @Test
    public void testCreateUniqueNameManufacturer() {

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName("Panasonic")
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = new Manufacturer.Builder()
                .setName("Panasonic")
                .setCountry("Japan")
                .build();

        dbHandler.create(manufacturer);

        int actualSize = dbHandler.readAllManufacturers().size();
        int expectedSize = 1;

        assertThat(actualSize).isEqualTo(expectedSize);

    }

    @Test
    public void testUpdateManufacturer() {

        String oldName = "Panasonic";
        String newName = "Panasonic_updated";

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName(oldName)
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = dbHandler.readAllManufacturers().get(0);

        manufacturer.setName(newName);
        dbHandler.update(manufacturer);

        manufacturer = dbHandler.readAllManufacturers().get(0);

        String actualName = manufacturer.getName();
        String expectedName = newName;

        assertThat(actualName).isEqualTo(expectedName);
    }

    @Test
    public void testUpdateUniqueNameManufacturer() {

        String oldName = "Panasonic";
        String newName = "Panasonic_updated";

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName(oldName)
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = new Manufacturer.Builder()
                .setName(newName)
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = dbHandler.readAllManufacturers().get(0);

        manufacturer.setName(newName);
        dbHandler.update(manufacturer);

        manufacturer = dbHandler.readAllManufacturers().get(0);

        String actualName = manufacturer.getName();
        String expectedName = oldName;

        assertThat(actualName).isEqualTo(expectedName);
    }

    @Test
    public void testDeleteManufacturer() {

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName("Panasonic")
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = dbHandler.readAllManufacturers().get(0);

        dbHandler.delete(manufacturer);

        int actualSize = dbHandler.readAllManufacturers().size();
        int expectedSize = 0;

        assertThat(actualSize).isEqualTo(expectedSize);
    }

    // Product tests
    @Test
    public void testCreateProduct() {

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName("Panasonic")
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = dbHandler.readManufacturerByName(manufacturer.getName());

        Product product = new Product.Builder()
                .setName("Playstation 2")
                .setManufacturerId(manufacturer.getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        int actualSize = dbHandler.readAllProducts().size();
        int expectedSize = 1;

        assertThat(actualSize).isEqualTo(expectedSize);
    }

    @Test
    public void testUniqueNameProduct() {

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName("Panasonic")
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = dbHandler.readManufacturerByName(manufacturer.getName());

        Product product = new Product.Builder()
                .setName("Playstation 2")
                .setManufacturerId(manufacturer.getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = new Product.Builder()
                .setName("Playstation 2")
                .setManufacturerId(manufacturer.getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        int actualSize = dbHandler.readAllProducts().size();
        int expectedSize = 1;

        assertThat(actualSize).isEqualTo(expectedSize);
    }

    @Test
    public void testUpdateProduct() {

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName("Panasonic")
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = dbHandler.readManufacturerByName(manufacturer.getName());

        Product product = new Product.Builder()
                .setName("Playstation 2")
                .setManufacturerId(manufacturer.getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product.setPrice(599.99);

        dbHandler.update(product);

        product = dbHandler.readAllProducts().get(0);

        double actualPrice = product.getPrice();
        double expectedPrice = 599.99;
        double eps = 10e-3;

        assertThat(actualPrice).isCloseTo(expectedPrice, Offset.offset(eps));

    }

    @Test
    public void testDeleteProduct() {

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName("Panasonic")
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = dbHandler.readManufacturerByName(manufacturer.getName());

        Product product = new Product.Builder()
                .setName("Playstation 2")
                .setManufacturerId(manufacturer.getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = dbHandler.readAllProducts().get(0);

        dbHandler.delete(product);

        int actualSize = dbHandler.readAllProducts().size();
        int expectedSize = 0;


        assertThat(actualSize).isEqualTo(expectedSize);

    }

    // Custom DB methods
    @Test
    public void testReadAllManufacturerProducts() {

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName("Panasonic")
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = dbHandler.readManufacturerByName(manufacturer.getName());

        Product product = new Product.Builder()
                .setName("Playstation 1")
                .setManufacturerId(manufacturer.getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = new Product.Builder()
                .setName("Playstation 2")
                .setManufacturerId(manufacturer.getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = new Product.Builder()
                .setName("Playstation 3")
                .setManufacturerId(manufacturer.getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        int actualSize = dbHandler.readAllManufacturerProducts(manufacturer).size();
        int expectedSize = 3;

        assertThat(actualSize).isEqualTo(expectedSize);

    }

    @Test
    public void testReadAllProductsByCountry() {

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName("Panasonic")
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = new Manufacturer.Builder()
                .setName("Sony")
                .setCountry("Japan")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = new Manufacturer.Builder()
                .setName("Nintendo")
                .setCountry("Japan")
                .build();

        dbHandler.create(manufacturer);

        Product product = new Product.Builder()
                .setName("Playstation 1")
                .setManufacturerId(dbHandler.readManufacturerByName("Sony").getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = new Product.Builder()
                .setName("Playstation 2")
                .setManufacturerId(dbHandler.readManufacturerByName("Sony").getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = new Product.Builder()
                .setName("Playstation 3")
                .setManufacturerId(dbHandler.readManufacturerByName("Nintendo").getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        int actualSize = dbHandler.readAllProductsByCountry("Japan").size();
        int expectedSize = 3;

        assertThat(actualSize).isEqualTo(expectedSize);

    }

    @Test
    public void testReadAllManufacturersWherePriceLessThan() {

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName("Panasonic")
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = new Manufacturer.Builder()
                .setName("Sony")
                .setCountry("Japan")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = new Manufacturer.Builder()
                .setName("Nintendo")
                .setCountry("Japan")
                .build();

        dbHandler.create(manufacturer);

        Product product = new Product.Builder()
                .setName("Playstation 1")
                .setManufacturerId(dbHandler.readManufacturerByName("Sony").getId())
                .setPrice(299.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = new Product.Builder()
                .setName("Playstation 2")
                .setManufacturerId(dbHandler.readManufacturerByName("Sony").getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = new Product.Builder()
                .setName("Playstation 3")
                .setManufacturerId(dbHandler.readManufacturerByName("Nintendo").getId())
                .setPrice(499.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        int actualSize = dbHandler.readAllManufacturersWherePriceLessThan(450.0).size();
        int expectedSize = 2;

        assertThat(actualSize).isEqualTo(expectedSize);

    }

    @Test
    public void testReadAllProductManufacturerMapByYear() {

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName("Panasonic")
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = new Manufacturer.Builder()
                .setName("Sony")
                .setCountry("Japan")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = new Manufacturer.Builder()
                .setName("Nintendo")
                .setCountry("Japan")
                .build();

        dbHandler.create(manufacturer);

        Product product = new Product.Builder()
                .setName("Playstation 1")
                .setManufacturerId(dbHandler.readManufacturerByName("Sony").getId())
                .setPrice(299.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = new Product.Builder()
                .setName("Playstation 2")
                .setManufacturerId(dbHandler.readManufacturerByName("Sony").getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = new Product.Builder()
                .setName("Playstation 3")
                .setManufacturerId(dbHandler.readManufacturerByName("Nintendo").getId())
                .setPrice(499.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);


        Set<Map.Entry<Product, Manufacturer>> entries
                = dbHandler.readAllProductManufacturerMapByYear("2000").entrySet();

        int actualSonyCounter = 0;
        int expectedSonyCounter = 2;

        int actualNintendoCounter = 0;
        int expectedNintendoCounter = 1;

        for (Map.Entry<Product, Manufacturer> entry : entries) {
            if (entry.getValue().getName().equals("Sony"))
                actualSonyCounter++;
            else if (entry.getValue().getName().equals("Nintendo"))
                actualNintendoCounter++;
        }

        assertThat(actualSonyCounter).isEqualTo(expectedSonyCounter);
        assertThat(actualNintendoCounter).isEqualTo(expectedNintendoCounter);
    }

    @Test
    public void testReadAllProductsByYear() {
        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName("Panasonic")
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = dbHandler.readManufacturerByName(manufacturer.getName());

        Product product = new Product.Builder()
                .setName("Playstation 1")
                .setManufacturerId(manufacturer.getId())
                .setPrice(299.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = new Product.Builder()
                .setName("Playstation 2")
                .setManufacturerId(manufacturer.getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = new Product.Builder()
                .setName("Playstation 3")
                .setManufacturerId(manufacturer.getId())
                .setPrice(499.99)
                .setDate("12-12-2002")
                .build();

        dbHandler.create(product);

        int actualSize = dbHandler.readAllProductsByYear("2000").size();
        int expectedSize = 2;

        assertThat(actualSize).isEqualTo(expectedSize);
    }

    @Test
    public void testDeleteManufacturerAndAllProducts() {

        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName("Panasonic")
                .setCountry("Korea")
                .build();

        dbHandler.create(manufacturer);

        manufacturer = dbHandler.readManufacturerByName(manufacturer.getName());

        Product product = new Product.Builder()
                .setName("Playstation 1")
                .setManufacturerId(manufacturer.getId())
                .setPrice(299.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = new Product.Builder()
                .setName("Playstation 2")
                .setManufacturerId(manufacturer.getId())
                .setPrice(399.99)
                .setDate("12-12-2000")
                .build();

        dbHandler.create(product);

        product = new Product.Builder()
                .setName("Playstation 3")
                .setManufacturerId(manufacturer.getId())
                .setPrice(499.99)
                .setDate("12-12-2002")
                .build();

        dbHandler.create(product);

        dbHandler.deleteManufacturerAndAllProducts(manufacturer);

        int actualSize = dbHandler.readAllManufacturerProductsMap().size();
        int expectedSize = 0;

        assertThat(actualSize).isEqualTo(expectedSize);

    }

}
