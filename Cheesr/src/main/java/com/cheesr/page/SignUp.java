package com.cheesr.page;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.cheesr.model.User;
import com.cheesr.session.CheesrSession;

public class SignUp extends Panel {

	private static final long serialVersionUID = 1L;

	public SignUp(String id) {
		super(id);
		
		User newUser = new User();
		Form<User> signUpForm = new Form<User>("signUpForm");
		signUpForm.add(new RequiredTextField<>("username", new PropertyModel<>(newUser, "username")));
		signUpForm.add(new PasswordTextField("password", new PropertyModel<>(newUser, "password")));
		signUpForm.add(new Button("signUpButton") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				addUser(newUser);
				CheesrSession.get().invalidate();
				setResponsePage(LoginPage.class);
			}
		});

		add(signUpForm);
	}

	private void addUser(User newUser) {
		try (Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml")) {
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

			try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

				String namespace = "com.cheesr.mapper.UserMapper";
				String statementId = "insertUser";
				sqlSession.insert(namespace + "." + statementId, newUser);
				sqlSession.commit();
				sqlSession.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
