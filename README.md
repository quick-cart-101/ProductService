# ProductService

## Description
ProductService is a Spring Boot application that provides RESTful APIs for managing products and categories. It includes features such as product creation, retrieval, and category management.

## Technologies Used
- Java 17
- Spring Boot 3.4.2
- Spring Data JPA
- Spring Security
- Spring Cloud Netflix Eureka
- Redis
- MySQL
- JJWT (JSON Web Token)

## Prerequisites
- Java 17
- Maven
- MySQL

## Setup
1. Clone the repository:
   ```sh
   git clone https://github.com/MayankShivhare999/ProductService.git
   cd ProductService

## API Endpoints

### Product APIs
- `POST /products` - Create a new product
- `GET /products/{id}` - Retrieve a product by ID
- `PUT /products/{id}` - Update a product by ID
- `DELETE /products/{id}` - Delete a product by ID

### Category APIs
- `POST /categories` - Create a new category
- `GET /categories/{id}` - Retrieve a category by ID
- `PUT /categories/{id}` - Update a category by ID
- `DELETE /categories/{id}` - Delete a category by ID
- `GET /categories/{id}/products` - Retrieve products by category ID