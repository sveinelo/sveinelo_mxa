package no.mxa.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO class which handles database mappings related to key values
 */
public class KeyValuesDTO implements Serializable {

	/**
	 * Unique generated version number
	 */
	private static final long serialVersionUID = 2L;

	/**
	 * Private class variables
	 */
	private Long id;
	private String key;
	private Date dateValue;
	private Integer numericValue;
	private String stringValue;
	private String description;

	/**
	 * Default constructor
	 */
	public KeyValuesDTO() {

	}

	/**
	 * Constructor which takes key value content as parameters
	 * 
	 * @param key
	 *            Key value key, describes the data type of the key value
	 * @param dateValue
	 *            The key value if it is date type
	 * @param numericValue
	 *            The key value if it is numeric type
	 * @param stringValue
	 *            The key value if it is string type
	 * @param description
	 *            The key value description
	 */
	public KeyValuesDTO(String key, Date dateValue, Integer numericValue, String stringValue, String description) {
		this.key = key;
		this.dateValue = dateValue;
		this.numericValue = numericValue;
		this.stringValue = stringValue;
		this.description = description;
	}

	/**
	 * Getter method for id
	 * 
	 * @return: id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter method for id
	 * 
	 * @param id
	 *            : The id value to set on the key value
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter method for key
	 * 
	 * @return: key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Setter method for key
	 * 
	 * @param key
	 *            : The key of the key value
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Getter method for dateValue
	 * 
	 * @return: dateValue
	 */
	public Date getDateValue() {
		return dateValue;
	}

	/**
	 * Setter method for dateValue
	 * 
	 * @param dateValue
	 *            : The date type key value
	 */
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	/**
	 * Getter method for numericValue
	 * 
	 * @return: numericValue
	 */
	public Integer getNumericValue() {
		return numericValue;
	}

	/**
	 * Setter method for numericValue
	 * 
	 * @param numericValue
	 *            : The numeric type key value
	 */
	public void setNumericValue(Integer numericValue) {
		this.numericValue = numericValue;
	}

	/**
	 * Getter method for stringValue
	 * 
	 * @return: stringValue
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * Setter method for stringValue
	 * 
	 * @param stringValue
	 *            : The string type key value
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * Getter method for description
	 * 
	 * @return: description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter method for description
	 * 
	 * @param description
	 *            : The description of the key value
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
