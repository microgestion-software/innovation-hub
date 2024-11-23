FROM mcr.microsoft.com/devcontainers/java:21

# Install additional packages
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
    curl \
    git \
    postgresql-client \
    && apt-get clean -y \
    && rm -rf /var/lib/apt/lists/*

# Install Maven
ARG MAVEN_VERSION=3.9.9
RUN curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar xzf - -C /usr/share \
    && mv /usr/share/apache-maven-${MAVEN_VERSION} /usr/share/maven \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# Configure Maven
COPY maven-settings.xml /usr/share/maven/conf/settings.xml

# Create non-root user
ARG USERNAME=vscode
ARG USER_UID=1000
ARG USER_GID=$USER_UID

# RUN groupadd --gid $USER_GID $USERNAME \
#     && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME \
#     && apt-get update \
#     && apt-get install -y sudo \
#     && echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
#     && chmod 0440 /etc/sudoers.d/$USERNAME

RUN apt-get update \
    && apt-get install -y sudo \
    && echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME

# Set the default user
USER $USERNAME