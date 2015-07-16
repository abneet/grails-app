import org.apache.log4j.PatternLayout;

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}


grails {
	plugin {
		aws {
			credentials {
				accessKey = "AKIAJKWMXLFILTKJCI7A"
				secretKey = "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"
			}
			s3 {
				acl = "public"
				bucket = "quickstore"
				bucketLocation = "ap-southeast-1"
			}
		}
	}
}

// log4j configuration
log4j = {

    PatternLayout patternLayout = new PatternLayout("%d [%t] %-5p %c %x - %m%n")


    debug   'grails.app.controllers',
            'grails.app.controller',
            'grails.app.domain',
            'grails.app.services',
            'grails.app.filters',
            'com.mycompany'
//            'org.springframework.security'
            'org.hibernate.SQL'

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    appenders {
        appender new org.apache.log4j.ConsoleAppender(name: "console",
                threshold: org.apache.log4j.Level.DEBUG,
                layout: patternLayout
        )
    }
    root {
        error 'stdout'
        additivity = true
    }
}

    

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'ghumover2.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'ghumover2.UserRole'
grails.plugin.springsecurity.authority.className = 'ghumover2.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/':                              ['permitAll'],
	'/index':                         ['permitAll'],
	'/index.gsp':                     ['permitAll'],
	'/assets/**':                     ['permitAll'],
	'/**/js/**':                      ['permitAll'],
	'/**/css/**':                     ['permitAll'],
	'/**/images/**':                  ['permitAll'],
	'/**/favicon.ico':                ['permitAll']
	
]
cors.enabled=true
cors.url.pattern = ['/api/*','/app/*','/Teacher/*','/Parent/*']
cors.headers=[
		'Access-Control-Allow-Origin': '*',
		'Access-Control-Allow-Credentials': true,
		'Access-Control-Allow-Headers': 'origin, authorization, accept, content-type, x-requested-with ,X-Auth-Token',
		'Access-Control-Allow-Methods': 'GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS',
		'Access-Control-Max-Age': 3600
]

grails.plugin.springsecurity.userLookup.userDomainClassName = 'ghumover2.User'
				grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'ghumover2.UserRole'
				grails.plugin.springsecurity.authority.className = 'ghumover2.Role'
				grails.plugin.springsecurity.securityConfigType = 'InterceptUrlMap'
				grails.plugin.springsecurity.interceptUrlMap = [
						'/':                    ['permitAll'],
						'/index':               ['permitAll'],
						'/index.gsp':           ['permitAll'],
						'/assets/**':           ['permitAll'],
						'/partials/**':         ['permitAll'],
						'/api/**':              ['permitAll'],
						'/excelTest/**':          ['permitAll'],
						 '/studentDataExcel/**':          ['permitAll'],
						 '/Test/**':          ['permitAll'],
						'/**':                  ['isFullyAuthenticated()']
				]
				grails.plugin.springsecurity.filterChain.chainMap = [
				
					 '/Teacher/**': 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter', // Stateless chain
					 
					 '/Parent/**': 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter', // Stateless chain
					 '/app/**': 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter', // Stateless chain
					  
					 
						'/api/**': 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter', // Stateless chain
					  '/myapp/**': 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter', // Stateless chain

					 '/api/**': 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter', // Stateless chain
						'/data/**': 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter', // Stateless chain
						'/**': 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter'   // Traditional chain
				]

				grails.plugin.springsecurity.rememberMe.persistent = false
				grails.plugin.springsecurity.rest.login.useJsonCredentials = true
				grails.plugin.springsecurity.rest.login.failureStatusCode = 401
				grails.plugin.springsecurity.rest.token.storage.useGorm = true
				grails.plugin.springsecurity.rest.token.storage.gorm.tokenDomainClassName = 'ghumover2.AuthenticationToken'
				grails.plugin.springsecurity.rest.token.storage.gorm.tokenValuePropertyName = 'token'
				grails.plugin.springsecurity.rest.token.storage.gorm.usernamePropertyName = 'username'

				grails.plugin.springsecurity.rest.token.validation.headerName = 'X-Auth-Token'
				grails.plugin.springsecurity.rest.token.validation.useBearerToken = false

				// Added by the Spring Security Core plugin:
				grails.plugin.springsecurity.userLookup.userDomainClassName = 'ghumover2.User'
				grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'ghumover2.UserRole'
				grails.plugin.springsecurity.authority.className = 'ghumover2.Role'
				grails.plugin.springsecurity.controllerAnnotations.staticRules = [
					'/':                              ['permitAll'],
					'/index':                         ['permitAll'],
					'/index.gsp':                     ['permitAll'],
					'/assets/**':                     ['permitAll'],
					'/**/js/**':                      ['permitAll'],
					'/**/css/**':                     ['permitAll'],
					'/**/images/**':                  ['permitAll'],
					'/**/favicon.ico':                ['permitAll']
				]
				
				