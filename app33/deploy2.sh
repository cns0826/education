rm ./logs/my-web*.log
rm nohup.out
nohup java -jar -Xms1024m -Xmx1024m -Djava.security.egd=file:/dev/./urandom ./target/spring-boot-journal-0.0.1-SNAPSHOT.war &
