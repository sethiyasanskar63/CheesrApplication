package com.cheesr.configuration;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

import com.cheesr.page.LoginPage;
import com.cheesr.session.CheesrSession;

public class CheesrApplication extends WebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		return LoginPage.class;
	}

	@Override
	protected void init() {
		super.init();
		getFrameworkSettings().getSerializer();
		mountPage("/login", LoginPage.class);
	}

	public static CheesrApplication get() {
		return (CheesrApplication) WebApplication.get();
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new CheesrSession(request);
	}

}
