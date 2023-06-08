# SAM2N

## Backend

### Up and run

1. Download image by using the following command:
   `docker pull nikolaiischenko/sam2n`
2. Run it using: `docker run -p 8668:8668 -p 9669:9669 nikolaiischenko/sam2n`

**FYI:** Application is deployed on the port `8668`  
Administration is on the port: `9669`

### Build and run on local machine

1. `gradle bootBuildImage -Plocal --imageName=nikolaiischenko/sam2n`
2. `docker push nikolaiischenko/sam2n`

### OpenAPI Documentation

http://localhost:9669/actuator/swagger-ui/index.html

### Health check

http://localhost:9669/actuator/health

### Implemented endpoints

- **GET** all users: `http://localhost:8668/api/v1/users`
- **POST** new activity: `http://localhost:8668/api/v1/users`

  Body type: `raw json` E.g.: `"http://google.com"`

