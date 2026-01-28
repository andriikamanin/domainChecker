# Domain Checker

A project to check domain name availability via WHOIS. This repository includes two implementations:

* 🖥 **Console Version** — Command-line Java application
* 🌐 **Web Version** — Spring Boot MVC application

---

## Console Version

### Description

The console Java application:

* Reads a list of domains from a `domains.txt` file
* Checks each domain using WHOIS
* Outputs the results in the console
* Supports parallel checking and caching of WHOIS servers

### Structure

```
console-domain-checker/
 ├── domains.txt
 └── DomainChecker.java
```

### Usage

1. Create a `domains.txt` file with one domain per line:

```
google.com
example.org
someveryrandomname123456.net
```

2. Compile and run the application:

```bash
javac DomainChecker.java
java DomainChecker
```

### Example Output

```
google.com : TAKEN
example.org : FREE
someveryrandomname123456.net : FREE
```

### Features

* WHOIS queries via TCP (port 43)
* Caching of TLD → WHOIS server mappings
* Connection timeouts
* Parallel domain checking

---

## Web Version (Spring MVC)

### Description

The Spring Boot web application allows users to:

* Upload a `.txt` file containing domains
* Check the availability of each domain via WHOIS
* View results directly in the browser

### Technologies

* Java 21
* Spring Boot 3.x
* Spring MVC
* Thymeleaf
* Gradle

### Structure

```
domain-checker/
 ├── controller/
 ├── service/
 ├── whois/
 └── resources/
     └── templates/
         └── index.html
```

### Usage

```bash
./gradlew bootRun
```

Then open in your browser:

```
http://localhost:8080
```

### Input File Format

The `.txt` file should contain one domain per line:

```
google.com
example.org
someveryrandomname123456.net
```

### Output

Results will be displayed in the browser:

```
google.com — TAKEN
example.org — FREE
```

### Features

* MVC architecture with Thymeleaf templates
* File upload handling (MultipartFile)
* WHOIS server caching for faster queries
* Extensible service logic

---

## Possible Improvements

* Asynchronous domain checking for faster results
* Progress bar for file processing
* REST API for programmatic access
* Download results as `.txt`
* Dockerized deployment

---

## License

MIT License
