<h1>Pokémon Database Manager</h1>

  <p>
    A desktop application for editing a Pokémon database, including trainers and their statistics. <br>
  </p>

Author: Jan Čihař

School: SPŠE Ječná
  

## Table of contents
- [Requirements](#requirements)
- [How to run](#how-to-run)
- [Functions](#functions)
- [What's included](#whats-included)
- [Contact](#contact)
- [Copyright and license](#copyright-and-license)

## Requirements
You need to have at least Java 17 installed and MySQL server

## How to run
1. Run the SQL script to create database and tables with test data
2. Put the jar file and folder named res in same folder
3. Edit the config file and put it in res
  ```text
    DatabaseProject.jar
    res/
      └── config.properties
  ```
4. Open console
5. Change directory to where the jar file is located
6. Run the jar file

    ```
    java -jar <name.jar>
    ```
## Functions

### 1. Pokémon Editing

#### Catch New Pokémon
Add a new Pokémon, set its stats (HP, Atk, Def), and assign up to two types 

#### Edit Pokémon 
Modify existing Pokémon data

#### Release Pokémon
Remove Pokémon from the database

#### Import from CSV 
Upload Pokémon data from CSV file

Example:
```text
trainer_id;nickname;rarity;hp;attack;defense;type1;type2
1;Pikachu;Rare;35;55;40;Electric;
2;Charizard;Legendary;78;84;78;Fire;Flying
3;Mewtwo;Legendary;106;110;90;Psychic;
```

### 2. Trainers Editing

#### Add New Trainer
Add a new Trainer, set ther name and xp

#### Edit Pokémon 
Modify existing Trainer data

#### Delete Trainer
Safely remove Trainer from the database

#### Import from CSV 
Upload Trainer data from CSV file

Example:

```text
name;experience_points;is_gym_leader
Red;999.9;true
Blue;850.5;false
```

### 3. Type Editing

#### Add New Type
Add a new Type

#### Delete Type
Remove Type from the database

---

## What's included


```text
DatabaseProject/
├── res/
│   └── config.properties
└── src/
    ├── DatabaseConfig.java
    ├── Main.java
    ├── MySQLPokemonRepository.java
    ├── MySQLTrainerRepository.java
    ├── MySQLTypeRepository.java
    ├── Pokemon.java
    ├── PokemonGUI.java
    ├── PokemonRepository.java
    ├── PokemonStats.java
    ├── Trainer.java
    ├── TrainerRepository.java
    ├── TypeRepository.java
    └── Types.java
```
## Contact 
If you have any questions regarding this project contact me:<br>
 - E-mail: honzacihar@email.cz 

## Copyright and license

Code and documentation copyright 2025 the author. Code released under the [MIT License](https://github.com/Ememple/VideoEditor/blob/master/LICENSE).

