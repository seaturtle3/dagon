#!/bin/bash

# Ubuntu/WSL 환경에서 Python, pip, pymysql, 크롬, selenium, webdriver-manager 설치 스크립트
# (Windows 네이티브는 pip만 설치되어 있으면 됨)

set -e

# 1. Python3, pip 설치 (Ubuntu/WSL)
if command -v apt-get >/dev/null 2>&1; then
    echo "[INFO] apt 환경 감지됨. Python3, pip 설치..."
    sudo apt-get update
    sudo apt-get install -y python3 python3-pip
fi

# 2. pip 최신화
python3 -m pip install --upgrade pip

# 3. pymysql 설치
python3 -m pip install pymysql

# 4. selenium, webdriver-manager 설치
python3 -m pip install selenium webdriver-manager

# 5. 크롬 브라우저 설치 (Ubuntu/WSL)
if command -v apt-get >/dev/null 2>&1; then
    if ! command -v google-chrome >/dev/null 2>&1; then
        echo "[INFO] 크롬 브라우저 설치..."
        wget -O google-chrome.deb https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
        sudo apt-get install -y ./google-chrome.deb
        rm google-chrome.deb
    else
        echo "[INFO] 크롬 브라우저가 이미 설치되어 있습니다."
    fi
fi

# 6. 설치 확인
python3 -c "import pymysql, selenium, webdriver_manager; print('pymysql/selenium/webdriver-manager 설치 확인 완료!')"

echo "[완료] Python, pip, pymysql, selenium, webdriver-manager, 크롬 설치가 끝났습니다." 