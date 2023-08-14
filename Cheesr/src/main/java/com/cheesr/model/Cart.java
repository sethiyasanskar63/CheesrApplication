package com.cheesr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<Cheese, Integer> cartItems;
	private Address address;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Cart() {
		cartItems = new HashMap<>();
	}

	public void clear() {
		cartItems.clear();
	}

	public void addToCart(Cheese cheese, int quantity) {
		cartItems.put(cheese, cartItems.getOrDefault(cheese, 0) + quantity);
	}

	public void updateQuantity(Cheese cheese, int newQuantity) {
		if (newQuantity <= 0) {
			removeFromCart(cheese);
		} else {
			cartItems.put(cheese, newQuantity);
		}
	}

	public void removeFromCart(Cheese cheese) {
		cartItems.remove(cheese);
	}

	public List<CartItem> getCartItems() {
		List<CartItem> items = new ArrayList<>();
		for (Map.Entry<Cheese, Integer> entry : cartItems.entrySet()) {
			items.add(new CartItem(entry.getKey(), entry.getValue()));
		}
		return items;
	}

	public double getTotalAmount() {
		double total = 0;
		for (Map.Entry<Cheese, Integer> entry : cartItems.entrySet()) {
			Cheese cheese = entry.getKey();
			int quantity = entry.getValue();
			total += cheese.getPrice() * quantity;
		}
		return total;
	}

	public static class CartItem implements Serializable {

		private static final long serialVersionUID = 1L;
		private Cheese cheese;
		private int quantity;

		public CartItem(Cheese cheese, int quantity) {
			this.cheese = cheese;
			this.quantity = quantity;
		}

		public Cheese getCheese() {
			return cheese;
		}

		public int getQuantity() {
			return quantity;
		}
	}
}
