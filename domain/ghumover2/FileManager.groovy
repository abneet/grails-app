package ghumover2

import ghumover2.MyChildFile;
import ghumover2.User;
import java.sql.Timestamp

class FileManager {

	String fileGroupType//school album,personal files
	String fileGroupName
	String albumCoverUrl
	String fileCount
	Date postedDate
	User user
	static hasMany = [files:MyChildFile]
	
    static constraints = {
    }
}
