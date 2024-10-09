# Stage 1: Builder
FROM openjdk:22-slim-bullseye AS builder

# Install necessary packages
RUN apt update && apt upgrade -y && apt install -y maven git curl && apt-get clean

# Set the working directory for the build process
WORKDIR /build

# Add application source code to the build context
ADD . .

# Clone the SSLPoke repository
#RUN git clone https://github.com/rksk/SSLPoke.git /tmp/SSLPoke


# Compile SSLPoke.java
WORKDIR /tmp/SSLPoke
COPY SSLPoke.java /tmp/SSLPoke
RUN javac SSLPoke.java

# Copy the existing Zscaler certificate into the container
COPY zscaler.crt /usr/local/share/ca-certificates/zscaler.crt

# Import the Zscaler certificate into Java's trust store
RUN keytool -import -trustcacerts -keystore /usr/local/openjdk-22/lib/security/cacerts \
    -storepass changeit -noprompt -alias zscaler -file /usr/local/share/ca-certificates/zscaler.crt

# Test the certificate setup using SSLPoke
RUN java -cp . SSLPoke google.com 443 && \
    java -cp . SSLPoke airportgap.com 443 || { echo 'javac failed'; exit 1; }

# Back to the build directory to build the application
WORKDIR /build

# Download Maven dependencies and package the application
RUN mvn dependency:go-offline && mvn clean package -DskipTests=true

# Stage 2: Runtime
FROM openjdk:22-slim-bullseye

# Set the working directory for the runtime environment
WORKDIR /app

# Copy the built application JAR from the builder stage
COPY --from=builder /build/target/*.jar ./app.jar

# Expose the application port
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "/app/app.jar", "--spring.datasource.url=jdbc:mariadb://database:3306/air_assist"]
