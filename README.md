# SpeedCode

## Introduce
SpeedCode 是一个在线，面对竞赛选手的单文件 ide

## Installation

1. Download `docker` and `docker-compose`
2. Open your shell and execute command:

```bash
# Download judge0
# Also you can change judge0's version
# But be careful, maybe some different version's language id is different
# Judge0's GitHub: https://github.com/judge0/judge0
wget https://github.com/judge0/judge0/releases/download/v1.13.0/judge0-v1.13.0.zip
unzip judge0-v1.13.0.zip

# Setup judge0
cd judge0-v1.13.0
docker-compose up -d db redis
docker-compose up -d
```

3. Download SpeedCode source code from [GitHub](https://github.com/LittleSheep2010/SpeedCode) / [Gitee](https://gitee.com/LittleSheeper/SpeedCode)
4. Open your shell and execute command to install:

```bash
cd path/to/speedcode
docker-compose up -d
```

5. Look Backend subproject's `README.md` to learn how to configure backend project.
6. After configure, please use `docker-compose up -d` restart project!

## Update

1. Pull the source code from origin

```bash
git pull
```

2. Using `docker-compose` up image

```bash
docker-compose up -d
```