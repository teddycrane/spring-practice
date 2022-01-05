# Spring Practice

[![CircleCI](https://circleci.com/gh/teddycrane/spring-practice/tree/main.svg?style=svg)](https://circleci.com/gh/teddycrane/spring-practice/tree/main)
[![codecov](https://codecov.io/gh/teddycrane/spring-practice/branch/main/graph/badge.svg?token=XITOHMWVDH)](https://codecov.io/gh/teddycrane/spring-practice)

This is a spring boot practice application that I'm using to get better at doing things

## Local Development

Local development requires some setup due to security keys being set as environment variables. To generate a valid signing key for local use, do the following.

1. Create a `.env` file in the root directory of this repository. This file is gitignored and should not be commited.

2. Generate a 512 bit signing key.

```
openssl rand -base64 64
```

3. Set the `SECRET_KEY` environment variable in `.env`.

```
SECRET_KEY=YOUR_SECRET_KEY_HERE
```
