package com.cheesr.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.cheesr.model.Cart;
import com.cheesr.model.Cart.CartItem;
import com.cheesr.model.Cheese;
import com.cheesr.session.CheesrSession;
import com.cheesr.utility.CheeseListModel;

public class Index extends WebPage {

	private static final long serialVersionUID = 1L;
	private Cart cart = CheesrSession.get().getCart();
	private Label totalAmountLabel;
	private ModalWindow orderModel;

	public Index() {

		orderModel = new ModalWindow("orderModel");
		add(orderModel);

		add(new AjaxLink<Void>("orderButton") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				orderModel.show(target);
			}

		});

		add(new AjaxLink<Void>("logoutButton") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				CheesrSession.get().invalidate();
				setResponsePage(LoginPage.class);
			}

		});

		OrderModelContent orderModelContent = new OrderModelContent(orderModel.getContentId());
		orderModel.setContent(orderModelContent);
		orderModel.setInitialWidth(900);
		orderModel.setInitialHeight(450);

		CheeseListModel cheeseListModel = new CheeseListModel();

		PageableListView<Cheese> cheeses = new PageableListView<Cheese>("cheeses", cheeseListModel, 5) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Cheese> item) {
				Cheese cheese = (Cheese) item.getModelObject();
				item.add(new Label("id", cheese.getId()));
				item.add(new Label("name", cheese.getName()));
				item.add(new Label("description", cheese.getDescription()));
				item.add(new Label("price", cheese.getPrice()));
				item.add(new Link<Void>("add") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						Cheese selected = cheese;
						cart.addToCart(selected, 1);
					}
				});
			}

		};

		add(cheeses);
		add(new PagingNavigator("navigator", cheeses));

		add(new ListView<CartItem>("cart-row", new PropertyModel<>(this, "cart.cartItems")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<CartItem> item) {
				CartItem cartItem = item.getModelObject();
				item.add(new Label("cheeseName", cartItem.getCheese().getName()));
				item.add(new Label("cheesePrice", cartItem.getCheese().getPrice()));
				Model<Integer> quantityModel = Model.of(cartItem.getQuantity());
				Label quantityLabel = new Label("quantity", quantityModel);
				quantityLabel.setOutputMarkupId(true);
				item.add(quantityLabel);
				item.add(new AjaxLink<Void>("increment") {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Cheese selected = cartItem.getCheese();
						int quantity = quantityModel.getObject();
						quantityModel.setObject(quantity + 1);
						cart.updateQuantity(selected, quantity + 1);
						target.add(quantityLabel);
						target.add(totalAmountLabel);
					}
				}.add(new Label("label", "+")));

				item.add(new AjaxLink<Void>("decrement") {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Cheese selected = cartItem.getCheese();
						int quantity = quantityModel.getObject();
						if (quantity > 0) {
							quantityModel.setObject(quantity - 1);
							cart.updateQuantity(selected, quantity - 1);
						}
						if (quantity == 0) {
							CartItem selectedCartItem = cartItem;
							cart.removeFromCart(selectedCartItem.getCheese());
						}

						target.add(quantityLabel);
						target.add(totalAmountLabel);
					}
				}.add(new Label("label", "-")));

				item.add(new Link<Void>("remove") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						CartItem selected = cartItem;
						cart.removeFromCart(selected.getCheese());
					}
				});
			}
		});

		totalAmountLabel = new Label("totalAmount", new PropertyModel<>(cart, "totalAmount"));
		totalAmountLabel.setOutputMarkupId(true);

		add(totalAmountLabel);

		add(new Link<Void>("checkout") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new CheckOut());
			}

			@Override
			public boolean isVisible() {
				return !cart.getCartItems().isEmpty();
			}

		});

	}
}
