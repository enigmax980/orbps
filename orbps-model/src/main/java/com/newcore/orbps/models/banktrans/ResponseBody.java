package com.newcore.orbps.models.banktrans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlType(name = "BODY", namespace = "RespBody")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseBody implements Serializable {
	@XmlElement(name = "RESPONSE")
	private ProcScRes response;

	public ProcScRes getResponse() {
		return response;
	}

	public void setResponse(ProcScRes response) {
		this.response = response;
	}

}
