FROM node:argon

# 앱 디렉토리 생성
RUN mkdir -p /app/grosnc
WORKDIR /app/grosnc

#Dockerfile Maintainer
MAINTAINER Junho Yoon <yoonjho79@gmail.com>

# 앱 소스 추가
ADD . /app/grosnc

#앱 의존성 설치
RUN npm install

#가상머신에 포트 오픈
EXPOSE 3000

#컨테이너에서 실행될 명령어 지정
CMD ["npm", "start"]


