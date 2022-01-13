# Spring Practice

[![CircleCI](https://circleci.com/gh/teddycrane/spring-practice/tree/main.svg?style=svg)](https://circleci.com/gh/teddycrane/spring-practice/tree/main)
[![codecov](https://codecov.io/gh/teddycrane/spring-practice/branch/main/graph/badge.svg?token=XITOHMWVDH)](https://codecov.io/gh/teddycrane/spring-practice)

## Installation and External Dependencies

This project is built using a target of Java11, the most recent LTS release of the JDK. Java11 binaries, sources, and other resources may be installed in multiple ways, preferably using a package manager such as `brew` or your Linux distro's package manager. Windows users can either develop using WSL to utilize the virtualized OS's package manager, or install Java in their preferred way. This repo was originally developed on a Unix-like operating system, and will most likely be more easily updated from one.

This project is using Maven as a build tool (although a Gradle migration is planned at some point). Maven is required to do package management, run tests and coverage, and build the application

## Local Development

Local development requires some setup due to security keys being set as environment variables. To generate a valid signing key for local use, do the following.

1. Create a `.env` file in the root directory of this repository. This file is gitignored and should not be commited.

2. Generate a 512 bit signing key.

```bash
openssl rand -base64 64
```

3. Set the `SECRET_KEY` environment variable in `.env`.

```bash
SECRET_KEY=YOUR_SECRET_KEY_HERE
```

4. Build the application while in the project root directory with

```bash
docker compose build
```

5. Start the application while in the project root directory with

```bash
docker compose up -d
```

6. You are now ready to invoke the application running at `http://localhost:8080`

## Project Structure

Each top-level URI resource is grouped into it's own package. Within this package, there are various sub-packages intended to assist with organization

```text
├── .circleci           <- CI/CD config files
├── containers          <- Docker container definitions (WIP)
├── src                 <- Root Project Directory
│   ├── main
│   │   └── java        <- Root Source File Directory
│   │   └── resources   <- Spring Boot Resources (configuration files, etc)
│   └── test            <- Test Sources
└── target              <- Output directory
```

## Commands

Various cli commands are provided by [Maven](https://maven.apache.org/) to assist with building and testing the application

### Build/Package Application (jar)

```bash
mvn clean package
```

### Run Tests

All tests can be run with:

```bash
mvn test
```

- To run only unit tests, run

```bash
mvn verify -DskipITs
```

- To run integration tests, run

```bash
mvn verify -DskipTests
```
