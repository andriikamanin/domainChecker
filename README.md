Domain Checker

A project for checking domain name availability via WHOIS.
It includes two implementations:
	•	 Console version - CLI application
	•	Web version - Spring Boot MVC application

⸻

🖥 Console Version

Description

The console Java application:
	•	Reads a list of domains from domains.txt
	•	Checks each domain via WHOIS
	•	Prints results to the console
	•	Supports parallel domain checking and caching of WHOIS servers

Structure

console-domain-checker
 ├── domains.txt
 └── DomainChecker.java

Usage
	1.	Create domains.txt file:

google.com
example.org
someveryrandomname123456.net

	2.	Compile and run:

javac DomainChecker.java
java DomainChecker

Example Output

google.com : TAKEN
example.org : FREE
someveryrandomname123456.net : FREE

Features
	•	WHOIS via TCP (port 43)
	•	TLD to WHOIS server caching
	•	Connection timeouts
	•	Parallel domain checking

⸻

Web Version (Spring MVC)

Description

Web application using Spring Boot + Thymeleaf:
	•	Upload a .txt file with domains
	•	Check their availability via WHOIS
	•	Display results in a web page

Technologies
	•	Java 21
	•	Spring Boot 3.x
	•	Spring MVC
	•	Thymeleaf
	•	Gradle

Structure

domain-checker
 ├── controller
 ├── service
 ├── whois
 └── resources
     └── templates
         └── index.html

Usage

./gradlew bootRun

Open in browser:

http://localhost:8080

Input File Format

One domain per line:

google.com
example.org
someveryrandomname123456.net

Output

Results displayed in the browser:

google.com — TAKEN
example.org — FREE

Features
	•	MVC architecture
	•	File upload (MultipartFile)
	•	WHOIS server caching
	•	Extensible service logic
