FROM node:argon

# �� ���丮 ����
RUN mkdir -p /app/grosnc
WORKDIR /app/grosnc

#Dockerfile Maintainer
MAINTAINER Junho Yoon <yoonjho79@gmail.com>

# �� �ҽ� �߰�
ADD . /app/grosnc

#�� ������ ��ġ
RUN npm install

#����ӽſ� ��Ʈ ����
EXPOSE 3000

#�����̳ʿ��� ����� ��ɾ� ����
CMD ["npm", "start"]


