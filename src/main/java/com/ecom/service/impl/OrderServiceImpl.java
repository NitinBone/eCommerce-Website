package com.ecom.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.model.Cart;
import com.ecom.model.OrderAddress;
import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;
import com.ecom.repository.CartRepository;
import com.ecom.repository.ProductOrderRepository;
import com.ecom.service.OrderService;
import com.ecom.util.CommonUtil;
import com.ecom.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductOrderRepository orderRepository;
	
	@Autowired
	private CommonUtil commonUtil;
	

	@Override
	public void saveOrder(Integer userId, OrderRequest orderRequest) {
    
		List<Cart> carts=cartRepository.findByUserId(userId);
		
		for(Cart cart:carts) {
			ProductOrder order=new ProductOrder();
			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(new Date());
			order.setProduct(cart.getProduct());
			order.setPrice(cart.getProduct().getDiscountPrice());

			order.setQuantity(cart.getQuantity());
			order.setUser(cart.getUser());
			
			order.setStatus(OrderStatus.IN_PROGRESS.getName());
			order.setPaymentType(orderRequest.getPaymentType());
			
			OrderAddress address = new OrderAddress();
			address.setFirstName(orderRequest.getFirstName());
			address.setLastName(orderRequest.getLastName());
			address.setEmail(orderRequest.getEmail());
			address.setMobileNo(orderRequest.getMobileNo());
			address.setAddress(orderRequest.getAddress());
			address.setCity(orderRequest.getCity());
			address.setState(orderRequest.getState());
			address.setPincode(orderRequest.getPincode());

			order.setOrderAddress(address);	
			
			ProductOrder saveOrder=orderRepository.save(order);
			try {
				commonUtil.sendMailForProductOrder(saveOrder, "success");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	@Override
	public List<ProductOrder> getOrdersByUser(Integer userId) {
		List<ProductOrder> orders=orderRepository.findByUserId(userId);
		return orders;
	}


	@Override
	public ProductOrder updateOrderStatus(Integer id, String status) {
		Optional<ProductOrder> findById=orderRepository.findById(id);
		if(findById.isPresent()) {
			ProductOrder productOrder=findById.get();
			productOrder.setStatus(status);
			ProductOrder updateOrder=orderRepository.save(productOrder);
			return updateOrder;
		}
		return null;
	}


	@Override
	public List<ProductOrder> getAllOrders() {
		List<ProductOrder> ordersList=orderRepository.findAll();
		return ordersList;
	}


	@Override
	public ProductOrder getOrdersByOrderId(String orderId) {
		return orderRepository.findByOrderId(orderId);
	}

}

