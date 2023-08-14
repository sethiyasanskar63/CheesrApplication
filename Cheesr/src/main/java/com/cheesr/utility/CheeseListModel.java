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

import com.cheesr.model.Cheese;

public class CheeseListModel extends LoadableDetachableModel<List<Cheese>> {

	private static final long serialVersionUID = 1L;

	private List<Cheese> cheeses = new ArrayList<Cheese>();

	@Override
	protected List<Cheese> load() {
		try (Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml")) {
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

			try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

				String namespace = "com.cheesr.mapper.CheeseMapper";
				String statementId = "getAllCheese";
				cheeses = sqlSession.selectList(namespace + "." + statementId);

				System.out.println(cheeses);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return cheeses;
	}
}
