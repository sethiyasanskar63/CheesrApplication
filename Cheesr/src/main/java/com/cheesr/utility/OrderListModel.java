package com.cheesr.utility;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.wicket.model.LoadableDetachableModel;

import com.cheesr.model.Order;
import com.cheesr.session.CheesrSession;

public class OrderListModel extends LoadableDetachableModel<List<Order>> {

	private static final long serialVersionUID = 1L;
	private List<Order> orders = new ArrayList<Order>();

	@Override
	protected List<Order> load() {

		try (Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml")) {
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

			try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

				String namespace = "com.cheesr.mapper.OrderMapper";
				String statementId = "getAllOrdersByUserId";
				orders = sqlSession.selectList(namespace + "." + statementId, CheesrSession.get().getUser().getId());
				System.out.println(orders);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return orders;
	}

}
