package com.ovh.milestone.Util;

public class Category {

    /**
     * Classifies all the products
     */

    public String productUniverse;
    public String productRange;
    public String productLine;
    public String productCategory;
    public String productSubCategory;
    public String productBrand;
    public String productBu; // Business Unit
    public String productPu; // Product Unit
    public String productType;
    public String refPerfect;
    public String refProcessed;
    public String dataCenter;
    public Double unitPrice;



    public Category(String productUniverse, String productRange, String productLine,
                    String productCategory, String productSubCategory, String productBrand,
                    String productBu, String productPu, String productType, String refPerfect,
                    String refProcessed, String dataCenter, Double unitPrice) {
        this.productUniverse = productUniverse;
        this.productRange = productRange;
        this.productLine = productLine;
        this.productCategory = productCategory;
        this.productSubCategory = productSubCategory;
        this.productBrand = productBrand;
        this.productBu = productBu;
        this.productPu = productPu;
        this.productType = productType;
        this.refPerfect = refPerfect;
        this.refProcessed = refProcessed;
        this.dataCenter = dataCenter;
        this.unitPrice = unitPrice;
    }



    public Category(Category pp) {
        this.productUniverse = pp.productUniverse;
        this.productRange = pp.productRange;
        this.productLine = pp.productLine;
        this.productCategory = pp.productCategory;
        this.productSubCategory = pp.productSubCategory;
        this.productBrand = pp.productBrand;
        this.productBu = pp.productBu;
        this.productPu = pp.productPu;
        this.productType = pp.productType;
        this.refPerfect = pp.refPerfect;
        this.refProcessed = pp.refProcessed;
        this.dataCenter = pp.dataCenter;
        this.unitPrice = pp.unitPrice;
    }
}
