package ghumover2

class AuthenticationToken {
	String username
	String token
    static constraints = {
		cache:true
		username index:'userToken'
		token index:'userToken'
    }
}
