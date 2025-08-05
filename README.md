# Spring MQTT Test

Aplicación de Spring Boot creada para probar la funcionalidad de MQTT en Spring, con el objetivo de integrarlo en un proyecto universitario.

---

## Tecnologías utilizadas

- Java 17  
- Spring Boot 3.5.4  
- Spring Integration MQTT 6.5.1  
- Eclipse Paho Client MQTTV3 1.2.6  

---

## Setup

Se necesita tener un **broker MQTT** corriendo localmente en la misma máquina donde se ejecuta esta aplicación. En este caso, se utilizó **Mosquitto** sobre Ubuntu Linux.

### Instalación de Mosquitto:

```bash
sudo apt install mosquitto
```

### Ejecución del broker en el puerto 2025:

```bash
mosquitto -v -p 2025
```

- La opción `-v` permite obtener logs detallados del servidor.  
- La opción `-p 2025` indica que el broker MQTT debe correr en el **puerto 2025**.

Con Mosquitto en ejecución, ya se puede iniciar la aplicación Spring Boot.

---

## Flujo de la aplicación

El funcionamiento de la aplicación se organiza en torno a dos clases de configuración ubicadas en el paquete `config`:

- `MQTTInboundConfig`
- `MQTTOutboundConfig`

---

## `MQTTInboundConfig`

Esta clase se encarga de **escuchar mensajes** de un tópico MQTT específico.

El método `inbound()` permite configurar:

- La URL del servidor Mosquitto  
- El ID de cliente que identificará a la aplicación  
- El tópico al que se suscribirá

![inbound config](https://github.com/user-attachments/assets/19a81abd-1ea7-4cbb-9f61-c9d1d52741ec)

Los mensajes recibidos se manejan con el siguiente método:

![message handler](https://github.com/user-attachments/assets/20e587be-1757-4483-bbab-6a59604a4c23)

Actualmente, los mensajes se **loggean por consola**, aunque se podría extender fácilmente para realizar otro tipo de procesamiento.

Este método se activa mediante la anotación:

```java
@ServiceActivator(inputChannel = "mqttInputChannel")
```

donde `mqttInputChannel` es el canal configurado previamente:

![input channel](https://github.com/user-attachments/assets/752c91d0-9fd0-4bcd-bed0-1e159c3f7f41)

---

## `MQTTOutboundConfig`

Esta clase permite **enviar mensajes** a un tópico MQTT.

Primero se configura una factory de clientes con la URL del servidor:

![client factory](https://github.com/user-attachments/assets/58ed6243-fc8b-4b3a-99d1-4990939361cc)

Luego, se utiliza esa factory para crear un cliente publicador, indicando también el tópico al que se enviarán los mensajes:

![outbound config](https://github.com/user-attachments/assets/345ea456-9d87-4d16-b46a-4dd5551893b3)

Finalmente, se define una interfaz que actúa como gateway para publicar mensajes:

![gateway](https://github.com/user-attachments/assets/70ceb340-28cb-46d4-9633-d55c3002fb33)

---

## Uso final

Con ambas clases configuradas, se puede utilizar la interfaz `MyGateway` (de `MQTTOutboundConfig`) para enviar mensajes al broker Mosquitto.

### 1. Ejecutar el broker MQTT:

![start broker](https://github.com/user-attachments/assets/ff9b6b1e-2d00-4290-84f7-9b7f72c58f50)

### 2. Iniciar la aplicación Spring Boot:

Ambos clientes MQTT (tanto el que escucha como el que envía) se conectarán automáticamente:

![clients connected](https://github.com/user-attachments/assets/534998ca-cf78-4bdd-9370-5681a86dfbfd)

### 3. Enviar un mensaje desde el cliente publicador:

![send message](https://github.com/user-attachments/assets/f52a6a49-a159-4585-b387-96bce227c2f3)

### 4. Ver el mensaje recibido y loggeado por el cliente suscriptor:

![receive message](https://github.com/user-attachments/assets/2a27d1f8-33a5-41f5-be94-4614419b7dab)
```
