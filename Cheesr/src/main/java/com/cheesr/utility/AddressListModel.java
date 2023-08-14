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

import com.cheesr.model.Address;
import com.cheesr.model.User;
import com.cheesr.session.CheesrSession;

public class AddressListModel extends LoadableDetachableModel<List<Address>> {

	private User user = CheesrSession.get().getUser();

	private static final long serialVersionUID = 1L;

	@Override
	protected List<Address> load() {
		List<Address> addresses = new ArrayList<>();
		try (Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml")) {
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

			try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

				String namespace = "com.cheesr.mapper.AddressMapper";
				String statementId = "getAllAddressByUserId";
				addresses = sqlSession.selectList(namespace + "." + statementId, user.getId());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return addresses;
	}
}
