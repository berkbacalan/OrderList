package com.example.project.model;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
	private Long orderid;
    
    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "price")
    private Double price;

	public Long getId() {
        return orderid;
    }

    public void setId(Long orderid) {
        this.orderid = orderid;
	}
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Order Name(String name){
		this.name = name;
		return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public Order Address(String address){
		this.address = address;
		return this;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Order Price(Double price){
		this.price = price;
		return this;
    }


    @Override
    public String toString() {return "Geldi";}
}