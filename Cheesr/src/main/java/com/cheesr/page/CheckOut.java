package com.cheesr.page;

import java.io.IOException;
import java.io.Reader;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.cheesr.model.Address;
import com.cheesr.model.Cart;
import com.cheesr.model.Cart.CartItem;
import com.cheesr.model.Cheese;
import com.cheesr.model.Order;
import com.cheesr.model.User;
import com.cheesr.session.CheesrSession;
import com.cheesr.utility.AddressListModel;

public class CheckOut extends WebPage {

	private static final long serialVersionUID = 1L;
	private Cart cart = CheesrSession.get().getCart();
	private User user = CheesrSession.get().getUser();
	private Order order = new Order();

	public CheckOut() {

		WebMarkupContainer cheeseListContainer = new WebMarkupContainer("cheese-list");
		add(cheeseListContainer);

		RepeatingView repeatingView = new RepeatingView("cheese-row");
		cheeseListContainer.add(repeatingView);

		for (CartItem cartItem : cart.getCartItems()) {
			Cheese cheese = cartItem.getCheese();

			Item<String> item = new Item<>(repeatingView.newChildId(), repeatingView.size());
			repeatingView.add(item);

			item.add(new Label("cheeseName", cheese.getName()));
			item.add(new Label("cheesePrice", String.valueOf(cheese.getPrice())));

			Model<Integer> quantityModel = Model.of(cartItem.getQuantity());
			Label quantityLabel = new Label("quantity", quantityModel);
			item.add(quantityLabel);
			quantityLabel.setOutputMarkupId(true);
		}

		cheeseListContainer.add(new Label("totalAmount", cart.getTotalAmount()));

		WebMarkupContainer addressListContainer = new WebMarkupContainer("address-list");
		add(addressListContainer);

		RadioGroup<Address> radioGroup = new RadioGroup<>("addressRadioGroup", new Model<>());
		AddressListModel addressListModel = new AddressListModel();

		ListView<Address> addressListView = new ListView<Address>("addressListView", addressListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Address> listItem) {
				Address address = listItem.getModelObject();
				Radio<Address> radioButton = new Radio<>("addressRadio", Model.of(address), radioGroup);
				listItem.add(new Label("addressLabel", address.toString()));
				listItem.add(radioButton);
			}
		};

		radioGroup.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				cart.setAddress(radioGroup.getModelObject());
			}
		});

		cart.setAddress(radioGroup.getModelObject());
		radioGroup.add(addressListView);
		addressListContainer.add(radioGroup);

		addressListContainer.setVisible(true);
		addressListContainer.setOutputMarkupId(true);
		addressListView.setOutputMarkupId(true);
		radioGroup.setOutputMarkupId(true);

		List<String> states = getAllStates();
		Form<Address> form = new Form<Address>("form");
		add(form);
		Address newAddress = new Address();
		form.add(new TextField<>("name", new PropertyModel<>(newAddress, "name")));
		form.add(new TextField<>("street", new PropertyModel<>(newAddress, "street")));
		form.add(new TextField<>("zipcode", new PropertyModel<>(newAddress, "zipcode"), Integer.class));
		form.add(new TextField<>("city", new PropertyModel<>(newAddress, "city")));
		DropDownChoice<String> dropdown = new DropDownChoice<>("dropdownField",
				new PropertyModel<>(newAddress, "state"), states);
		form.add(dropdown);
		form.add(new Link<Void>("cancel") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				form.setVisible(false);
			}
		});
		form.add(new AjaxButton("AddAddress") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				newAddress.setUserId(user.getId());
				addAddress(newAddress);
				form.setVisible(false);
				target.add(addressListContainer);
				target.add(form);
			}
		}.setDefaultFormProcessing(true));
		form.setVisible(false);
		form.setOutputMarkupId(true);

		Link<Void> addressLink = new Link<Void>("addAddress") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				form.setVisible(true);
			}

		};

		add(addressLink);

		Form<Void> creditCardForm = new Form<>("creditCardForm");
		TextField<String> cardNumberField = new TextField<>("cardNumber", Model.of(""));
		TextField<String> cardHolderNameField = new TextField<>("cardHolderName", Model.of(""));
		TextField<String> expirationDateField = new TextField<>("expirationDate", Model.of(""));
		creditCardForm.add(cardNumberField);
		creditCardForm.add(cardHolderNameField);
		creditCardForm.add(expirationDateField);
		creditCardForm.add(new CheckBox("confirmation", Model.of()));
		creditCardForm.add(new Link<Void>("cancel") {
			private static final long serialVersionUID = 1L;

			public void onClick() {
				setResponsePage(Index.class);
			}
		});

		creditCardForm.add(new Button("submitButton") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				orderComplete();
				cart.clear();
				CheesrSession.get().replaceSession();;
				setResponsePage(Index.class);
			}
		});
		add(creditCardForm);
	}

	private void orderComplete() {
		order.setOrderAmount(cart.getTotalAmount());
		order.setUserId(user.getId());
		order.setOrderDate(Date.valueOf(LocalDate.now()));
		order.setAddressid(cart.getAddress().getId());
		try (Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml")) {
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

			try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

				String namespace = "com.cheesr.mapper.OrderMapper";
				String statementId = "insertOrder";
				sqlSession.insert(namespace + "." + statementId, order);
				sqlSession.commit();
				sqlSession.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void addAddress(Address newAddress) {
		try (Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml")) {
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

			try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

				String namespace = "com.cheesr.mapper.AddressMapper";
				String statementId = "insertAddress";
				sqlSession.insert(namespace + "." + statementId, newAddress);
				sqlSession.commit();
				sqlSession.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> getAllStates() {
		List<String> states = new ArrayList<>();
		states.add("Andhra Pradesh");
		states.add("Arunachal Pradesh");
		states.add("Assam");
		states.add("Bihar");
		states.add("Chhattisgarh");
		states.add("Goa");
		states.add("Gujarat");
		states.add("Haryana");
		states.add("Himachal Pradesh");
		states.add("Jharkhand");
		states.add("Madhya Pradesh");

		return states;
	}

}
