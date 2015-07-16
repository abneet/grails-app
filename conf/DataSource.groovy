dataSource {
  pooled = true
  driverClassName = "com.mysql.jdbc.Driver"
  dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
}
hibernate {
  cache.use_second_level_cache = true
  cache.use_query_cache = true
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
  cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
  singleSession = true // configure OSIV singleSession mode
  hibernate.show_sql=true
}



// environment specific settings
environments {
  development {
    dataSource {
        dbCreate = "create-drop"
		//dbCreate = "update"
		url = "jdbc:mysql://localhost/mychild"
        	username = "root"
			password = "qwerty"
			properties {
				validationQuery = "SELECT 1"
				testOnBorrow = true
				testOnReturn = true
				testWhileIdle = true
				timeBetweenEvictionRunsMillis = 1800000
				numTestsPerEvictionRun = 3
				minEvictableIdleTimeMillis = 1800000
				jdbcInterceptors = "ConnectionState"
				  defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_UNCOMMITTED
				
  }
  }}
  test {
    dataSource {
      username = "gimmedev"
      password = "gimmepwd"
      pooled = true
      dbCreate = "create-drop"
      driverClassName = "com.mysql.jdbc.Driver"
      //  url = "jdbc:mysql://aa1bzishuiat2fj.c3m5mgrxcx6j.ap-southeast-1.rds.amazonaws.com:3306/ebdb?user=fusion&password=plp247619"
       //rahul dev url="jdbc:mysql://aaammt9ltjlzk9.cbj9zqqysdxf.ap-southeast-1.rds.amazonaws.com:3306/ebdb?user=gimmedev&password=gimmedev"
	 //ravi main
	   url="jdbc:mysql://mychildmain.cbf4otxzdwmr.ap-southeast-1.rds.amazonaws.com:3306/ebdb?user=gimmedev&password=gimmepwd"
      // sandepani
	 //  url="jdbc:mysql://mychildmain.cbf4otxzdwmr.ap-southeast-1.rds.amazonaws.com:3306/sandeep?user=gimmedev&password=gimmepwd"
	  //    url="jdbc:mysql://prod.cdd6wlpmn6gf.ap-southeast-1.rds.amazonaws.com:3306/prod?user=gimmedev&password=gimmepwd"
	  
	 //admin ravi	
        //url="jdbc:mysql://adminui.c3m5mgrxcx6j.ap-southeast-1.rds.amazonaws.com:3306/ebdb?user=gimmedev&password=gimmepwd"
        dialect = org.hibernate.dialect.MySQL5InnoDBDialect
        properties {
         validationQuery = "SELECT 1"
         testOnBorrow = true
         testOnReturn = true
         testWhileIdle = true
         timeBetweenEvictionRunsMillis = 1800000
         numTestsPerEvictionRun = 3
         minEvictableIdleTimeMillis = 1800000
         jdbcInterceptors = "ConnectionState"
           defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_UNCOMMITTED
         
         }
    }
  }
  production {
    dataSource {
		
      url = "jdbc:mysql://gimmetestdb.cbj9zqqysdxf.ap-southeast-1.rds.amazonaws.com:3306/ebdb?user=gimme&password=gimmepwd"
      username = "gimme"
      password = "gimmepwd"
      pooled = true
      dbCreate = "create"
      driverClassName = "com.mysql.jdbc.Driver"
      
      /*username = "fusion"
      password = "plp247619"
      pooled = true
      dbCreate = "update"
      driverClassName = "com.mysql.jdbc.Driver"
      url = "jdbc:mysql://aa1bzishuiat2fj.c3m5mgrxcx6j.ap-southeast-1.rds.amazonaws.com:3306/ebdb?user=fusion&password=plp247619"
      
      */  dialect = org.hibernate.dialect.MySQL5InnoDBDialect
        properties {
         validationQuery = "SELECT 1"
         testOnBorrow = true
         testOnReturn = true
         testWhileIdle = true
         timeBetweenEvictionRunsMillis = 1800000
         numTestsPerEvictionRun = 3
         minEvictableIdleTimeMillis = 1800000
        }
        
        
        
      
    }
  }
}
