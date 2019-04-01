package com.example.project.model;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
	private Long productid;
    
    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "stock")
    private int stock;

	public Long getId() {
        return productid;
    }

    public void setId(Long productid) {
        this.productid = productid;
	}
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Product Name(String name){
		this.name = name;
		return this;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Product Price(Double price){
		this.price = price;
		return this;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public Product Stock(int stock){
		this.stock = stock;
		return this;
    }

    @Override
    public String toString() {return "Geldi";}
}