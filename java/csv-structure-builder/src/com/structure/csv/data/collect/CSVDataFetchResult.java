package src.com.structure.csv.data.collect;

public class CSVDataFetchResult {
	private String recordType;
	private String subRecordOf;
	private String name;
	private String relationship;
	private String dataType;
	private int minLength;
	private int maxLength;
	private String optionalMandatory;
	private String title;
	private String defaultValue;
	private String description;
	private String regEx;
	
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getSubRecordOf() {
		return subRecordOf;
	}
	public void setSubRecordOf(String subRecordOf) {
		this.subRecordOf = subRecordOf;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public int getMinLength() {
		return minLength;
	}
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public String getOptionalMandatory() {
		return optionalMandatory;
	}
	public void setOptionalMandatory(String optionalMandatory) {
		this.optionalMandatory = optionalMandatory;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRegEx() {
		return regEx;
	}
	public void setRegEx(String regEx) {
		this.regEx = regEx;
	}
}
