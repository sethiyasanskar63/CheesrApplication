package com.cheesr.page;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

import com.cheesr.model.User;
import com.cheesr.session.CheesrSession;

public class LoginPage extends WebPage {

	private static final long serialVersionUID = 1L;
	private User userValidation = new User();
	private User sessionUser = CheesrSession.get().getUser();
	private ModalWindow signupModal;

	public LoginPage() {

		signupModal = new ModalWindow("signupModal");
		add(signupModal);

		add(new AjaxLink<Void>("signUpButton") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				signupModal.show(target);
			}

		});
		
		SignUp signup = new SignUp(signupModal.getContentId());
		signupModal.setContent(signup);
		signupModal.setInitialWidth(900);
		signupModal.setInitialHeight(450);

		Form<LoginPage> form = new Form<>("loginForm", new CompoundPropertyModel<>(this));

		form.add(new RequiredTextField<>("username", new PropertyModel<>(userValidation, "username")));
		form.add(new PasswordTextField("password", new PropertyModel<>(userValidation, "password")));

		form.add(new AjaxButton("loginButton") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				if (authenticate()) {
					setResponsePage(Index.class);
				} else {
					error("Invalid credentials");
				}
			}
		});

		add(form);
	}

	private boolean authenticate() {
		User loginUser = new User();
		try (Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml")) {
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

			try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

				String namespace = "com.cheesr.mapper.UserMapper";
				String statementId = "getUserByUsername";
				loginUser = sqlSession.selectOne(namespace + "." + statementId, userValidation.getUsername());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if ((loginUser != null) && (loginUser.getPassword().equals(userValidation.getPassword()))) {
			sessionUser.setId(loginUser.getId());
			sessionUser.setUsername(loginUser.getUsername());
			sessionUser.setPassword(loginUser.getPassword());
			return true;
		} else {
			return false;
		}
	}
}