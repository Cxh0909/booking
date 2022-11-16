package org.jboss.quickstarts.wfk.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.quickstarts.wfk.booking.Booking;

@Entity
@NamedQueries({
		@NamedQuery(name = Customer.FIND_ALL, query = "SELECT c FROM Customer c ORDER BY c.id ASC"),
		@NamedQuery(name = Customer.FIND_BY_EMAIL, query = "SELECT c FROM Customer c WHERE email = :email"),
		@NamedQuery(name = Customer.FIND_BY_PHONE_NUMBER, query = "SELECT c FROM Customer c WHERE phone_number = "
				+ ":phoneNumber")
})
@XmlRootElement
@Table(name = "customer", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "phone_number"}))
public class Customer implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "Customer.findAll";

	public static final String FIND_BY_EMAIL = "Customer.findByEmail";

	public static final String FIND_BY_PHONE_NUMBER = "Customer.findByPhoneNumber";

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z-']+", message = "Please use a name without numbers or specials")
    @Column(name = "first_name")
	private String firstName;
	
	@NotNull
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z-']+", message = "Please use a name without numbers or specials")
    @Column(name = "last_name")
	private String lastName;

	@NotNull
	@NotEmpty
	@Email(message = "The email address must be in the format of name@domain.com")
	private String email;

	@NotNull
	@Pattern(regexp = "^\\([2-9][0-8][0-9]\\)\\s?[0-9]{3}\\-[0-9]{4}$", message = "please input a valid phoneNumber")
	@Column(name = "phone_number")
	private String phoneNumber;

	@JsonIgnore
	@ApiModelProperty(hidden = true)
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<Booking> bookings;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Customer{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", bookings=" + bookings +
				'}';
	}
}
