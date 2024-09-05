FROM mcr.microsoft.com/devcontainers/java:17

# Instalar herramientas adicionales si es necesario
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
    curl \
    git

# Configurar variables de entorno si es necesario
ENV JAVA_HOME /usr/local/openjdk-17
ENV PATH $PATH:$JAVA_HOME/bin

# Copiar archivos de configuración si es necesario
# COPY settings.xml /home/vscode/.m2/

# Instalar SDKMan para manejar múltiples versiones de Java por si en un furuto es necesario
RUN curl -s "https://get.sdkman.io" | bash