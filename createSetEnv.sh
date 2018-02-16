
echo '#!/bin/sh'  | sudo tee --append /usr/share/tomcat8/bin/setenv.sh

echo 'JAVA_OPTS="$JAVA_OPTS -Dspring.datasource.url=jdbc:mysql://csye6225-fall2017.celnei7d4k0p.us-east-1.rds.amazonaws.com:3306/csye6225"'  | sudo tee --append /usr/share/tomcat8/bin/setenv.sh

echo 'JAVA_OPTS="$JAVA_OPTS -Dspring.datasource.username=csye6225master"' | sudo tee --append /usr/share/tomcat8/bin/setenv.sh

echo 'JAVA_OPTS="$JAVA_OPTS -Dspring.datasource.password=csye6225password"' | sudo tee --append /usr/share/tomcat8/bin/setenv.sh

chmod +x /usr/share/tomcat8/bin/setenv.sh

chown tomcat8:tomcat8 /usr/share/tomcat8/bin/setenv.sh
