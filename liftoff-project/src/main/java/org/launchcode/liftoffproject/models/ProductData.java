package org.launchcode.liftoffproject.models;

import java.util.ArrayList;

public class ProductData {


    public static String getFieldValue(Product product, String fieldName){
        String theValue;

        if (fieldName.equals("type")){
            theValue = product.getType();
        } if (fieldName.equals("vendor")){
            theValue = product.getVendor().toString();
        } if (fieldName.equals("location")) {
            theValue = product.getVendor().getName();
        }else{
            theValue = product.toString();
        }

        return theValue;
    }

    //    /**
//     * Search all Product fields for the given term.
//     *
//     * @param value The search term to look for.
//     * @param allProducts The list of products to search.
//     * @return      List of all products with at least one field containing the value.
//     */

    public static ArrayList<Product> findByValue(String value, Iterable<Product> allProducts) {
        String lower_val = value.toLowerCase();

        ArrayList<Product> results = new ArrayList<>();

        for (Product product : allProducts) {

            if (product.getType().toLowerCase().contains(lower_val)) {
                results.add(product);
            } else if (product.getName().toLowerCase().contains(lower_val)) {
                results.add(product);
            } else if (product.getDescription().toLowerCase().contains(lower_val)) {
                results.add(product);
            } else if (product.getVendor().getLocation().toLowerCase().contains(lower_val)) {
                results.add(product);
            }else if (product.getVendor().getName().toLowerCase().contains(lower_val)) {
                results.add(product);
            }
        }

        return results;
    }

    public static ArrayList<Product> findByColumnAndValue(String column, String value, Iterable<Product> allProducts) {
        ArrayList<Product> results = new ArrayList<>();

        if (value.toLowerCase().equals("all")){
            return (ArrayList<Product>) allProducts;
        }

        if (column.equals("all")){
            results = findByValue(value, allProducts);
            return results;
        }
        for (Product product : allProducts) {

            String aValue = getFieldValue(product, column);

            if (aValue != null && aValue.toLowerCase().contains(value.toLowerCase())) {
                results.add(product);
            }
        }

        return results;
    }
//    public static void remove(int id) {
//
//        products.remove(id);
//    }
}
