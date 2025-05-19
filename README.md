# PokeApi & JDBC

## General Description

This console application allows you to manage and view Pokémon information from three different sources:

1. Local SQLite database
2. PokeAPI (pokeapi.co)
3. Local JSON files

The user can query detailed information about Pokemon, abilities, types, moves, generations, locations, and regions.

## Requirements

- Java JDK 17 or higher
- Internet connection (for API queries)
- SQLite
- GSON library

## Installation

1. Clone the repository
2. Make sure you have the necessary dependencies (GSON, JDBC driver for SQLite)
3. Compile the project
4. Run the `Main` class

## Directory Structure
```plaintext
db/
├── dia/                    # DB design diagrams
└── pokeapiDB.db            # SQLite database file
src/
├── api/                    # Client for consuming the API
├── controller/             # Controllers for business logic
│   ├── apicontroller/      # Controller to display API data
│   └── json/               # Controller to handle JSON files
├── Exceptions/             # Custom exceptions
├── model/                  # Data models
│   ├── constructor/        # Constructor classes
│   ├── dao/                # DAO interfaces
│   ├── dbconnection/       # Database connection
│   └── SQLite/             # DAO implementations for SQLite
├── view/                   # Views for user interface
└── json/                   # Directory where JSON files are stored
```

## User Guide

When running the application, a main menu is presented with several options:

1. **Show local DB content**: Query information stored in the SQLite database.
2. **Show Endpoint content**: Retrieve and display information directly from the PokeAPI API.
3. **Modify Pokemon according to Endpoint**: Update the database with information from the API.
4. **Copy data obtained from Endpoint**: Copy information from the API to the local database.
5. **Show JSON content**: Query information from local JSON files.
6. **Modify Pokemon according to JSON**: Update the database with information from JSON files.
7. **Copy data obtained from JSON**: Copy information from JSON files to the local database.
0. **Exit**: Exit the application.

### JSON File Format

JSON files must follow a specific structure and be located in the `src/json/` folder with the following naming formats:

- `pokemon_ID.json`: Pokemon information
- `ability_ID.json`: Ability information
- `type_ID.json`: Type information
- `move_ID.json`: Move information
- `generation_ID.json`: Generation information
- `location_ID.json`: Location information
- `region_ID.json`: Region information

## Technical Implementation

### Architecture

The application follows the MVC (Model-View-Controller) design pattern:

- **Model**: Classes representing entities such as Pokemon, abilities, types, etc., and DAO classes for data access.
- **View**: Classes that display information to the user (menus, listings).
- **Controller**: Classes that manage business logic and coordinate the flow between model and view.

### Main Components

#### 1. Data Access (DAO)

The DAO pattern is implemented through interfaces and specific implementations for SQLite:

```java
public interface DAO<T, K> {
    void insertTable(T t);
    void readTable(K k);
    void updateTable(T t);
    void deleteTable(K k);
    void readAll();
}
```

Each entity (Pokemon, ability, etc.) has its own DAO implementation.

#### 2. API Client

The API client (`PokeApiClient`) uses Java's `HttpClient` to make requests to the PokeAPI API and processes JSON responses with GSON.

#### 3. JSON File Reader

The `JsonFileReader` class handles reading local JSON files and converts them to `JsonObject` objects using GSON.

#### 4. Display Controllers

There are three types of controllers for displaying information:
- `Controller`: For local database data.
- `DisplayFromApi`: For API data.
- `DisplayFromJson`: For data from local JSON files.

#### 5. Menu Handling

The `HandleMenu` class manages user interaction through menus and submenus that redirect to the corresponding functionalities.

### Exception Handling

Custom exceptions are used:
- `DBNotFound`: For database connection problems.
- `PropertyNotFound`: For when a property or record is not found.
- `DataAccessException`: For general data access issues.
- `DuplicateEntryException`: For duplicate entries in the database.
- 'EmptyResultSetException': For empty result sets from queries.
- `ForeignKeyConstraintException`: For foreign key constraint violations.
- `InvalidDataException`: For invalid data formats or values.

## Main Features

- Multi-source querying of Pokemon data (DB, API, JSON)
- Detailed visualization of Pokemon-related entities
- Database updates from different sources
- Intuitive console interface with menu system

## Made by
- Arnau Aumedes
- Alex Ruiz