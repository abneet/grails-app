package ghumover2

import java.sql.Timestamp

class FileAccessMap {

	String fileId
	String accessibleToId
	String accessType
	String accessLevel
	Timestamp accessStart
	Timestamp accessEnd
	String isActive
	String createdByUserId
	Timestamp createdDatetime
	Timestamp lastModifiedDatetime
	String lastModifiedBy
	
    static constraints = {
    }
}
