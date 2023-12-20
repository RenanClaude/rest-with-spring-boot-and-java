package com.webdevelopment.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

@JsonPropertyOrder({ "id", "title", "author", "launchDate", "price" })
public class BookVO extends RepresentationModel<BookVO> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Mapping("id")
	@JsonProperty("id")
	private Long key;
	private String title;
	private String author;
	private Date launchDate;
	private Double price;

	public BookVO() {
	}

	public BookVO(Long key, String title, String author, Date launchDate, Double price)
			throws ParseException {
		this.key = key;
		this.title = title;
		this.author = author;
		this.launchDate = launchDate;
		this.price = price;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getLaunchDate() {
		return launchDate;
	}

	public void setLaunchDate(Date launchDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(launchDate);
		cal.add(Calendar.HOUR, 3);
    this.launchDate = cal.getTime();
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookVO other = (BookVO) obj;
		return Objects.equals(key, other.key);
	}

}
