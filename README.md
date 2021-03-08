# SIMILAR WEB HOME ASSIGNMENT

## What I did?
* First, the program will read the input files from resources/inputs/csv folder.
* In the initial processing the program contains two HashMap. 
  The first map is for the sites urls, in each entry the key will be the 'SITE_URL' and the value will be list of sessions.
  In the second map the program will save the sessions by visitorId. 
  The key in this map will be 'SITE_URL#VISITOR_ID' and the value will be list of sessions.
* For each data record the program will try to find a suitable session, otherwise a new session will be created.
* After the initial process the program will be able to receive the queries. 

## How To Execute A Query?

### Application Server
* Run the project as a service
* Send GET HTTP requests:
* 'health' : check if the service is up and running. (http://localhost:8501/health)
* 'help' : for help instructions. (http://localhost:8501/help)
* 'sessionsCounter' : get the number of sessions for site url. (http://localhost:8501/sessionsCounter?siteUrl=www.s_6.com)
* 'sessionsMedian' : get the median of sessions length for site url. (http://localhost:8501/sessionsMedian?siteUrl=www.s_6.com)
* 'numUniqueVisitedSites' : get the number of unique site urls by visitorId. (http://localhost:8501/numUniqueVisitedSites?visitorId=visitor_4297)

#### Complexity
* Space: O(n)
* Init Time: O(n)
* Query Time: O(1)

note: the project has been compiled with JDK-11