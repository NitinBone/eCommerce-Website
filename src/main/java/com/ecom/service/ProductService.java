package com.ecom.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Product;

public interface ProductService {

	public Product saveProduct(Product product);

	public List<Product> getAllProduct();
	
	public Boolean deleteProduct(Integer id);

	public Product getProductById(int id);
	
	public Product updateProduct(Product product,MultipartFile file);
	
	public List<Product> getAllActiveProducts(String category);
	
	public List<Product> searchProduct(String ch);
	
	public Page<Product> getAllActiveProductPagination(Integer pageNo,Integer pageSize,String category);
	
}
