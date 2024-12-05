FROM mysql:8.0

ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=library_db
ENV MYSQL_USER=username
ENV MYSQL_PASSWORD=password

COPY scheme.sql /docker-entrypoint-initdb.d/
