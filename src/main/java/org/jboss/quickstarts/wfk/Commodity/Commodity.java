package org.jboss.quickstarts.wfk.Commodity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.quickstarts.wfk.contact.Contact;

@Entity
@XmlRootElement
@Table(name = "commodity")
public class Commodity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@NotNull
    @Size(min = 1, max = 25)
    @Column(name = "commodity_type")
	private CommodityType commodityType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CommodityType getCommodityType() {
		return commodityType;
	}

	public void setCommodityType(CommodityType commodityType) {
		this.commodityType = commodityType;
	}
	
	
}
