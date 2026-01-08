# ☕ Coffee Shop Simulation System

A Java-based coffee shop simulation system featuring a Swing GUI, multithreaded order processing, and core software engineering design patterns. The application models real-world customer–staff interactions using synchronized queues, file-based persistence, and extensible discount strategies.

---

## 📌 Project Overview

This project simulates the operation of a coffee shop where customers place orders, staff process them concurrently, and the system maintains real-time updates through a graphical user interface.  

The system was developed in two stages:
- **Stage 1** focused on core functionality, GUI design, file I/O, discount handling, and reporting.
- **Stage 2** extended the system into a **multithreaded simulation**, introducing real-time order processing, staff coordination, and event logging.

The project demonstrates strong software engineering principles including modular design, concurrency control, design patterns, and test-driven development.

---

## ✨ Key Features

- Java Swing GUI for browsing menu items and placing orders  
- Multithreaded producer–consumer simulation (customers & staff)  
- Real-time order queue visualisation  
- File-based persistence for menu, orders, and logs  
- Strategy Pattern for flexible discount handling  
- Singleton-based centralized logging system  
- MVC-inspired architecture  
- Custom exception handling for robust input validation  
- JUnit test coverage for core business logic  

---

## 🏗 Architecture & Design Patterns

The system applies several well-known software design patterns:

- **MVC (Model–View–Controller)**  
  Separates GUI, business logic, and data models for maintainability.

- **Singleton**  
  Ensures a single shared instance for core components such as the CoffeeShop controller and Logger.

- **Strategy**  
  Enables flexible discount logic (e.g., Student, Senior) without modifying billing code.

- **Observer**  
  Keeps the GUI synchronized with real-time queue and order state changes.

- **Producer–Consumer**  
  Models customer arrivals and staff order processing using synchronized queues.

---

## 🖥 Screenshots

### GUI Overview
![GUI Overview](images/gui-overview.png)

### Thread Activity & Queue Processing
![Thread Activity](images/thread-activity.png)

### End-of-Day Report
![Report](images/report-sample.png)

---

## ▶️ How to Run the Project

### Prerequisites
- Java 11 or later
- IntelliJ IDEA / Eclipse (recommended)

### Steps
1. Clone the repository:
```bash
git clone https://github.com/yourusername/CoffeeShop-Simulation.git
