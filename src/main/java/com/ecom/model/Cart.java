package com.ecom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	private UserDtls user;
	
	@ManyToOne
	private Product product;
	
	private Integer quantity;
	
	@Transient
	private Double totalPrice;
	
	@Transient
	private Double totalOrderAmount;

	public Cart() {
		super();
		// TODO Auto-generated constructor stub
	}

	




	public Cart(Integer id, UserDtls user, Product product, Integer quantity, Double totalPrice,
			Double totalOrderAmount) {
		super();
		this.id = id;
		this.user = user;
		this.product = product;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.totalOrderAmount = totalOrderAmount;
	}






	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserDtls getUser() {
		return user;
	}

	public void setUser(UserDtls user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}



	public Double getTotalPrice() {
		return totalPrice;
	}



	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}






	public Double getTotalOrderAmount() {
		return totalOrderAmount;
	}






	public void setTotalOrderAmount(Double totalOrderAmount) {
		this.totalOrderAmount = totalOrderAmount;
	}






	@Override
	public String toString() {
		return "Cart [id=" + id + ", user=" + user + ", product=" + product + ", quantity=" + quantity + ", totalPrice="
				+ totalPrice + ", totalOrderAmount=" + totalOrderAmount + "]";
	}




	
	
	
	
	
}
