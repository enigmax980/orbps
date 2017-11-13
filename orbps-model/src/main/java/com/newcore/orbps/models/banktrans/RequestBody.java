package com.newcore.orbps.models.banktrans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlType(name = "BODY",namespace="RequestBody")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestBody implements Serializable{
	@XmlElement(name="REQUEST")
	private ProcScReq procScReq;

	public ProcScReq getProcScReq() {
		return procScReq;
	}

	public void setProcScReq(ProcScReq procScReq) {
		this.procScReq = procScReq;
	}
}
