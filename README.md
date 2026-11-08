# Kindergarten Graduation Decision Support System

A web-based Decision Support System (DSS) designed to evaluate and determine the graduation readiness of kindergarten students. This project implements the **Profile Matching** algorithm to assess students objectively based on standard early childhood developmental achievements.

## Key Features
*   **Algorithmic Evaluation:** Automated calculation of the gap between a student's actual competencies and the ideal target profile using the Profile Matching method.
*   **Dynamic Criteria Management:** Administrators can easily adjust assessment criteria, target values, and weights to adapt to changing curriculum standards.
*   **Core & Secondary Factor Analysis:** Precise weighting system prioritizing core competencies (60%) over secondary traits (40%) for accurate, data-driven decision-making.
*   **Comprehensive Reporting:** Generates detailed evaluation reports and final rankings for teachers and parents.

## Technology Stack
*   **Backend:** Java 17, Spring Boot, Maven
*   **Frontend:** HTML, CSS, JavaScript 
*   **Database:** MySQL
*   **Architecture:** MVC (Model-View-Controller)

## Algorithm Workflow (Profile Matching)
1.  **Gap Mapping:** Calculates the difference (gap) between the student's actual assessment score and the ideal profile score for each predefined criterion.
2.  **Weight Conversion:** Converts the calculated gap value into a standardized weight based on the standard Profile Matching gap table.
3.  **Factor Grouping:** Computes the average scores for Core Factors (essential criteria) and Secondary Factors (supporting criteria).
4.  **Total Score & Ranking:** Calculates the final total score to rank the students, providing a clear threshold for graduation eligibility.

## Local Setup & Installation

Follow these steps to run the project locally on your machine:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/nurul1953/undergraduate-final-project.git
   cd undergraduate-final-project
   ```

2. **Database Configuration:**
   Create a MySQL database (e.g., `spk_graduation`). Update the `src/main/resources/application.properties` file with your local database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/spk_graduation
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Run the Application:**
   Build and run the application using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the App:**
   Open your web browser and navigate to `http://localhost:8080`.

## About the Author
Developed as a final thesis project for a Bachelor's Degree in Computer Science/Informatics. This project demonstrates practical implementation of algorithmic decision-making in web applications.
