FROM nginx:stable

ENV TZ Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone 

COPY ./dist /usr/share/nginx/html
COPY ./devops/jenkins/vue-nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
ENTRYPOINT [ "sh", "-c", "echo 3.36.248.102 ktwiz.api.ktds.amazonaws.com >> /etc/hosts" ]
