package com.newcore.orbps.service.sff.api;


import javax.jws.WebService;


@WebService(targetNamespace = "http://www.e-chinalife.com/soa/")
@FunctionalInterface
public interface DeleteBankTransService {
	public String delete(String xml);
}
