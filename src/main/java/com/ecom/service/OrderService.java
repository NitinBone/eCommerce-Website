package com.ecom.service;

import java.util.List;

import com.ecom.model.OrderRequest;

import com.ecom.model.ProductOrder;

public interface OrderService {

	public void saveOrder(Integer userId,OrderRequest orderRequest);
	
	public List<ProductOrder> getOrdersByUser(Integer userId);

	public ProductOrder updateOrderStatus(Integer id, String status);

	public List<ProductOrder> getAllOrders();
	
	public ProductOrder getOrdersByOrderId(String orderId);
}
