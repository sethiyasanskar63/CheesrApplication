package com.cheesr.session;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

import com.cheesr.model.Cart;
import com.cheesr.model.User;

public class CheesrSession extends WebSession {

	private static final long serialVersionUID = 1L;
	private Cart cart;
	private User user;

	public CheesrSession(Request request) {
		super(request);
		cart = new Cart();
		user = new User();
	}

	public static CheesrSession get() {
		return (CheesrSession) Session.get();
	}

	public Cart getCart() {
		return cart;
	}

	public User getUser() {
		return user;
	}
}
