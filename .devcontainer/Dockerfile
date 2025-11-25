# .devcontainer/Dockerfile (alternativa)
FROM openjdk:21-slim

ARG USER=vscode
ARG USER_UID=1000
ARG USER_GID=1000

RUN apt-get update \
  && apt-get install -y --no-install-recommends sudo git curl unzip maven procps ca-certificates \
  && rm -rf /var/lib/apt/lists/*

RUN groupadd --gid ${USER_GID} ${USER} \
  && useradd --uid ${USER_UID} --gid ${USER_GID} -m ${USER} \
  && echo "${USER} ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/${USER} \
  && chmod 0440 /etc/sudoers.d/${USER}

ENV JAVA_HOME=/usr/local/openjdk-21
ENV PATH="${JAVA_HOME}/bin:${PATH}"

USER ${USER}
WORKDIR /workspace