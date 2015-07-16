package ghumover2

class User {

	transient springSecurityService

	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	String tags 
	boolean tagRegister=false
	boolean usersetupemialSent=false
	boolean istagupdate =true
	String deviceToken
	String platform
	long school_id
	String sesEmailResponse
	String sesEmailStatus
	
	static hasMany = [conversations : Conversation,fileManager:FileManager]
//	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true
		password blank: false
		tags(nullable: true)
		deviceToken(nullable: true)
		platform(nullable: true)
		school_id(nullable: true)
		sesEmailResponse(nullable: true)
		sesEmailStatus(nullable: true)
	}
	


	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role }
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
}
