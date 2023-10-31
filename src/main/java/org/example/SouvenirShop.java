package org.example;

import org.example.database.DatabaseHandler;
import org.example.entity.Manufacturer;
import org.example.entity.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class SouvenirShop {

    public void start() {

        try (Scanner scanner = new Scanner(System.in)) {

            final DatabaseHandler handler
                    = new DatabaseHandler("manufacturers.json", "products.json");

            System.out.println("\nDatabase Program");

            boolean continueProgram = true;
            while (continueProgram) {
                System.out.println("""
                        
                        Enter commands to perform operations on db.
                        Enter command 'commands' to list all commands.
                        To exit the program, type: 'exit'.""");
                System.out.print(" Input: ");

                String command = scanner.nextLine();
                switch (command.toLowerCase()) {
                    case "exit" -> {
                        continueProgram = false;
                    }
                    case "commands" -> {
                        commands();
                    }
                    case "list mf" -> {
                        listManufacturers(handler);
                    }
                    case "create mf" -> {
                        createManufacturer(scanner, handler);
                    }
                    case "update mf" -> {
                        updateManufacturer(scanner, handler);
                    }
                    case "delete mf" -> {
                        deleteManufacturer(scanner, handler);
                    }
                    case "list pd" -> {
                        listProducts(handler);
                    }
                    case "create pd" -> {
                        createProduct(scanner, handler);
                    }
                    case "update pd" -> {
                        updateProduct(scanner, handler);
                    }
                    case "delete pd" -> {
                        deleteProduct(scanner, handler);
                    }
                    case "list mf pd" -> {
                        listManufacturerProducts(scanner, handler);
                    }
                    case "list country pd" -> {
                        listProductsByCountry(scanner, handler);
                    }
                    case "list mf price" -> {
                        listManufacturersWherePriceLessThan(scanner, handler);
                    }
                    case "list all" -> {
                        listAll(handler);
                    }
                    case "list mf pd_name year" -> {
                        listAllManufacturersByProductNameAndYear(scanner, handler);
                    }
                    case "list year pd" -> {
                        listProductsByYear(scanner, handler);
                    }
                    case "delete mf and pd" -> {
                        deleteManufacturerAndAllProducts(scanner, handler);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void commands() {
        System.out.print("""
                
                List of all commands:
                - 'list mf' for all manufacturers;
                - 'create mf' to create manufacturer;
                - 'update mf' to update manufacturer;
                - 'delete mf' to delete manufacturer;
                - 'list pd' for all products;
                - 'create pd' to create product;
                - 'update pd' to update product;
                - 'delete pd' to delete product;
                - 'list mf pd' to list manufacturer products;
                - 'list country pd' to list products by country;
                - 'list mf price' to list manufacturers which products
                  prices are less than specified;
                - 'list all' to list all manufacturers and respective
                  products;
                - 'list mf pd_name year' to list manufacturers of
                  product with product name equal to pd_name in a
                  specified year;
                - 'list year pd' to list all products by year;
                - 'delete mf and pd' to delete manufacturer and all
                  all related products;
                """);
    }

    private void deleteManufacturerAndAllProducts(Scanner scanner, DatabaseHandler handler) {

        System.out.println("\nEnter manufacturer id:");
        System.out.print(" Input: ");
        long id = 0L;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Incorrect id format.");
            return;
        }
        Manufacturer manufacturer = handler.readManufacturerById(id);
        if (manufacturer == null) {
            System.out.println("Manufacturer not found.");
            return;
        }
        handler.deleteManufacturerAndAllProducts(manufacturer);
    }

    private void listProductsByYear(Scanner scanner, DatabaseHandler handler) {

        System.out.println("\nEnter year:");
        System.out.print(" Input: ");
        int year = 0;
        try {
            String yearString = scanner.nextLine();
            if (yearString.length() != 4) {
                System.out.println("Year length doesn't equal to 4 digits.");
                return;
            }
            year = Integer.parseInt(yearString);
        } catch (NumberFormatException e) {
            System.out.println("Incorrect integer format.");
            return;
        }

        handler.readAllProductsByYear(Integer.toString(year)).forEach(System.out::println);
    }

    private void listAllManufacturersByProductNameAndYear(Scanner scanner, DatabaseHandler handler) {

        System.out.println("\nEnter product name:");
        System.out.print(" Input: ");
        String name = scanner.nextLine();

        System.out.println("Enter product year");
        System.out.print(" Input:");
        int year = 0;
        try {
            String yearString = scanner.nextLine();
            if (yearString.length() != 4) {
                System.out.println("Year length doesn't equal to 4 digits.");
                return;
            }
            year = Integer.parseInt(yearString);
        } catch (NumberFormatException e) {
            System.out.println("Incorrect integer format.");
            return;
        }

        handler.readAllManufacturersByProductNameAndYear(name, Integer.toString(year))
                .forEach(System.out::println);

    }

    private void listAll(DatabaseHandler handler) {
        Map<Manufacturer, List<Product>> map = handler.readAllManufacturerProductsMap();
        for (Map.Entry<Manufacturer, List<Product>> set : map.entrySet()) {
            System.out.println(set.getKey());
            for (Product product: set.getValue())
                System.out.println("\t" + product);
        }
    }

    private void listManufacturersWherePriceLessThan(Scanner scanner, DatabaseHandler handler) {
        System.out.println("\nEnter price:");
        System.out.print(" Input: ");
        double price = 0.0;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Incorrect double format.");
            return;
        }

        handler.readAllManufacturersWherePriceLessThan(price).forEach(System.out::println);
    }

    private void listProductsByCountry(Scanner scanner, DatabaseHandler handler) {
        System.out.println("\nEnter country:");
        System.out.print(" Input: ");
        String country = scanner.nextLine();
        handler.readAllProductsByCountry(country).forEach(System.out::println);
    }

    private void listManufacturerProducts(Scanner scanner, DatabaseHandler handler) {
        System.out.println("\nEnter manufacturer id:");
        System.out.print(" Input: ");
        long id = 0L;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Incorrect id format.");
            return;
        }
        Manufacturer manufacturer = handler.readManufacturerById(id);
        if (manufacturer == null) {
            System.out.println("Manufacturer not found.");
            return;
        }
        handler.readAllManufacturerProducts(manufacturer).forEach(System.out::println);
    }

    private void deleteProduct(Scanner scanner, DatabaseHandler handler) {
        System.out.println("\nEnter product id:");
        System.out.print(" Input: ");
        long productId = 0L;
        try {
            productId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Incorrect id format.");
            return;
        }
        Product product = handler.readProductById(productId);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        handler.delete(product);
    }

    private void updateProduct(Scanner scanner, DatabaseHandler handler) {
        System.out.println("\nEnter product id:");
        System.out.print(" Input: ");
        long productId = 0L;
        try {
            productId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Incorrect id format.");
            return;
        }
        Product product = handler.readProductById(productId);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        System.out.println("Current -> " + product);
        System.out.println("\nEnter new manufacturer id:");
        System.out.print(" Input: ");
        long id = 0L;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Incorrect id format.");
            return;
        }
        Manufacturer manufacturer = handler.readManufacturerById(id);
        if (manufacturer == null) {
            System.out.println("Manufacturer not found.");
            return;
        }
        System.out.println("Enter new product name:");
        System.out.print(" Input: ");
        String name = scanner.nextLine();

        System.out.println("Enter new product date format 'dd-MM-yyyy':");
        System.out.print(" Input: ");
        String date = scanner.nextLine();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
            date = LocalDate.parse(date, formatter).toString();
        }catch (DateTimeParseException e) {
            System.out.println("Incorrect date format.");
            return;
        }

        System.out.println("Enter new product price:");
        System.out.print(" Input: ");
        double price = 0.0;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Incorrect double format.");
            return;
        }

        product.setName(name);
        product.setManufacturerId(manufacturer.getId());
        product.setDate(date);
        product.setPrice(price);

        handler.update(product);
    }

    private void createProduct(Scanner scanner, DatabaseHandler handler) {
        System.out.println("\nEnter manufacturer id:");
        System.out.print(" Input: ");
        long id = 0L;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Incorrect id format.");
            return;
        }
        Manufacturer manufacturer = handler.readManufacturerById(id);
        if (manufacturer == null) {
            System.out.println("Manufacturer not found.");
            return;
        }
        System.out.println("Enter product name:");
        System.out.print(" Input: ");
        String name = scanner.nextLine();


        System.out.println("Enter product date format 'dd-MM-yyyy':");
        System.out.print(" Input: ");
        String date = scanner.nextLine();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
            date = LocalDate.parse(date, formatter).toString();
        }catch (DateTimeParseException e) {
            System.out.println("Incorrect date format.");
            return;
        }

        System.out.println("Enter product price:");
        System.out.print(" Input: ");
        double price = 0.0;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Incorrect double format.");
            return;
        }
        Product product = new Product.Builder()
                .setName(name)
                .setManufacturerId(manufacturer.getId())
                .setDate(date)
                .setPrice(price)
                .build();
        handler.create(product);
    }

    private void listProducts(DatabaseHandler handler) {
        handler.readAllProducts().forEach(System.out::println);
    }

    private void deleteManufacturer(Scanner scanner, DatabaseHandler handler) {
        System.out.println("\nEnter manufacturer id:");
        System.out.print(" Input: ");
        long id = 0L;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Incorrect id format.");
            return;
        }
        Manufacturer manufacturer = handler.readManufacturerById(id);
        if (manufacturer == null) {
            System.out.println("Manufacturer not found.");
            return;
        }
        handler.delete(manufacturer);
    }

    private void updateManufacturer(Scanner scanner, DatabaseHandler handler) {
        System.out.println("\nEnter manufacturer id:");
        System.out.print(" Input: ");
        long id = 0L;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Incorrect id format.");
            return;
        }
        Manufacturer manufacturer = handler.readManufacturerById(id);
        if (manufacturer == null) {
            System.out.println("Manufacturer not found.");
            return;
        }
        System.out.println("Current -> " + manufacturer);
        System.out.println("Enter new name:");
        System.out.print(" Input: ");
        String name = scanner.nextLine();
        System.out.println("Enter new country:");
        System.out.print(" Input: ");
        String country = scanner.nextLine();
        manufacturer.setName(name);
        manufacturer.setCountry(country);
        handler.update(manufacturer);
    }

    private void createManufacturer(Scanner scanner, DatabaseHandler handler) {
        System.out.println("\nEnter manufacturer name:");
        System.out.print(" Input: ");
        String name = scanner.nextLine();
        System.out.println("\nEnter manufacturer country:");
        System.out.print(" Input: ");
        String country = scanner.nextLine();
        Manufacturer manufacturer = new Manufacturer.Builder()
                .setName(name)
                .setCountry(country)
                .build();
        handler.create(manufacturer);
    }

    private void listManufacturers(DatabaseHandler handler) {
        handler.readAllManufacturers().forEach(System.out::println);
    }

}
