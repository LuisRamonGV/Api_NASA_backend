# NASA APOD Backend

This project is the backend service for consuming NASA's **Astronomy Picture of the Day (APOD)** API. It provides a RESTful interface to interact with the APOD API, delivering data to the frontend and handling business logic securely and efficiently.

## 🚀 Features
- **Daily Picture Endpoint:** Retrieves the picture of the day along with its metadata.
- **Search by Date:** Fetches an APOD entry for a specific date.
- **Search by Date Range:** Retrieves multiple entries within a specified range.
- **Error Handling:** Includes detailed error responses for invalid inputs or API failures.
- **Rate Limiting:** Manages external API calls to respect NASA's API limits.

## 🛠 Technologies Used
### **Backend**
- **Language:** Java
- **Framework:** Spring Boot
- **Libraries/Tools:**
  - RestTemplate (for API requests)
  - Lombok (for cleaner Java code)
  - MapStruct (for object mapping)

### **Additional Tools**
- **Testing:** JUnit & Mockito
- **Build Tool:** Maven
- **Logging:** SLF4J

## 📦 Prerequisites
- Java 17+
- Maven 3.6+
- NASA APOD API Key (get yours from [NASA's API Portal](https://api.nasa.gov))

## ⚙️ Setup and Installation
1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/nasa-apod-backend.git
2. Add your NASA API key and alogn with the other necessary variables in `application.properties`
   ```bash
    nasa.api.url=https://api.nasa.gov/planetary/apod
    nasa.api.key=YOUR_API_KEY
    server.compression.enabled=true
    server.compression.mime-types=application/json,application/xml,text/html,text/xml,text/plain
    server.compression.min-response-size=1024
3. Build the project
   ```bash
   mvn clean install

## ▶️ Usage
- Run the application:
  ```bash
  mvn spring-boot:run

## 📝 Notes
- Ensure your NASA API key is kept secure and not shared publicly.
- The API rate limits must be respected; excessive requests may be blocked.
