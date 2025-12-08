Deployment (temporary)
===

- Ubuntu VM από Digital Ocean
- Postgres από Render
- nginx
- Basic Firewall
- Cloud Provider Firewall
- DNS & Cloud Firewall από Cloudflare

Δημιουργία Postgres Database

Σύνδεση Postgres Database

```yaml
datasource:
url: jdbc:postgresql://dpg-d4rgvhu3jp1c7390h6og-a.frankfurt-postgres.render.com:5432/officehoursdb
username: officehoursusr
password: h6jJm9qQvD50RCatTBtq7jCe2PS6c3nq
driver-class-name: org.postgresql.Driver

hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect
```

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

Δημιουργία Ubuntu VM

[Initial Server Setup with Ubuntu](https://www.digitalocean.com/community/tutorials/initial-server-setup-with-ubuntu#step-5-enabling-external-access-for-your-regular-user)

[How to Install Java with Apt on Ubuntu (JRE & JDK)](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-on-ubuntu-22-04)

Clone DS-Lab-NOC

Clone DS-Lab-OfficeHours

Έλεγχος εκτελεσιμότητας

```
ufw allow 8080/tcp
ufw allow 8081/tcp
```

Build NOC

Install NOC

Build OfficeHours

Install OfficeHours

```shell
./mvnw clean package -DskipTests
```

```shell
sudo mkdir -p /opt/DS-Lab-OfficeHours
sudo cp target/*.jar /opt/DS-Lab-OfficeHours/app.jar
```

```shell
sudo nano /etc/systemd/system/ds_lab_officehours.service
```

```
[Unit]
Description=DS Lab OfficeHours
After=network.target

[Service]
User=sammy
WorkingDirectory=/opt/DS-Lab-OfficeHours
ExecStart=java -jar /opt/DS-Lab-OfficeHours/app.jar
Restart=always
RestartSec=10
SuccessExitStatus=143

# OPTIONAL
# Environment=SPRING_PROFILES_ACTIVE=prod

[Install]
WantedBy=multi-user.target
```

```shell
sudo mkdir -p /opt/DS-Lab-OfficeHours/LOCAL_DATA
sudo chown -R sammy:sammy /opt/DS-Lab-OfficeHours
sudo chmod -R 755 /opt/DS-Lab-OfficeHours

```

```shell
sudo systemctl daemon-reload
sudo systemctl start ds_lab_officehours.service
sudo systemctl status ds_lab_officehours.service
sudo systemctl enable ds_lab_officehours.service
sudo journalctl -u ds_lab_officehours.service -f
```

```shell
sudo chmod + x /opt/DS-Lab-OfficeHours/app.jar
```

[How To Install Nginx on Ubuntu](https://www.digitalocean.com/community/tutorials/how-to-install-nginx-on-ubuntu-22-04)

```shell
sudo apt update
sudo apt install nginx -y
sudo systemctl status nginx
```

```shell
sudo nano /etc/nginx/sites-available/ds_lab_officehours
```

```
server {
    listen 80;
    server_name _;

    location / {
        proxy_pass http://127.0.0.1:8080;

        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        proxy_read_timeout 300;
        proxy_connect_timeout 300;
    }
}
```

```shell
sudo ln -s /etc/nginx/sites-available/ds_lab_officehours /etc/nginx/sites-enabled/
```

```shell
sudo rm /etc/nginx/sites-enabled/default
```

```shell
sudo nginx -t
```

```shell
sudo systemctl restart nginx
```
