package com.cheesr.page;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import com.cheesr.model.Address;
import com.cheesr.model.Order;
import com.cheesr.utility.OrderListModel;

public class OrderModelContent extends Panel {

	private static final long serialVersionUID = 1L;

	public OrderModelContent(String id) {
		super(id);
		OrderListModel orderListModel = new OrderListModel();

		add(new ListView<Order>("order-row", orderListModel) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Order> item) {
				Order order = item.getModelObject();
				item.add(new Label("orderId", order.getId()));
				item.add(new Label("orderAmount", order.getOrderAmount()));
				item.add(new Label("orderDate", order.getOrderDate()));
				Address address = getAddressById(order.getAddressid());
				item.add(new Label("orderAddress", address.toString()));
			}

		});
	}

	private Address getAddressById(Integer id) {
		Address address = new Address();
		try (Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml")) {
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

			try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

				String namespace = "com.cheesr.mapper.AddressMapper";
				String statementId = "getAddressById";
				address = sqlSession.selectOne(namespace + "." + statementId, id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return address;
	}

}
