package ghumover2


import java.sql.Timestamp;

class MyChildFile {

	String fileId
	String fileName
	String filePath
	String fileType
	String description
	Timestamp accessStart
	Timestamp archiveDatetime
	Timestamp accessEnd
	String statusCode
	User createdByUserId
	String isActive
	Timestamp creationdate
	Timestamp lastModifiedDatetime
	String lastModifiedBy
	
	static hasMany = [tags:Tag]
	static belongsTo = [fileManager:FileManager]
	
    static constraints = {
    }
}
