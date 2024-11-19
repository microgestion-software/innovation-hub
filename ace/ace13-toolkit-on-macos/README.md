# ACE 13 Toolkit MacOS Setup with Containerized IBM MQ

This repository provides configuration files and guidance for setting up IBM App Connect Enterprise (ACE) 13 Toolkit on MacOS with IBM MQ running in a Docker container. This setup is particularly useful for integration developers who are:
- New to the product
- Migrating from IIB 10
- Unfamiliar with containers and policies
- Looking for a quick and straightforward Toolkit configuration on MacOS

## Software Prerequisites

- Docker Desktop 4.35.1
- IBM ACE 13.0.1.0
- IBM MQ 9.4.0.1

## Repository Structure

```
├── mq_image/                  # MQ Container configuration files
│   ├── Dockerfile            # Instructions for building custom MQ image
│   └── mq4ace13-config.mqsc  # MQSC script for MQ resources
│
├── policy_project/           # MQ connectivity policies
│   └── PPContainerizedMQ/    # Policy project for MQ connection
│       └── PContainerizedMQEndpoint.policyxml
│
├── test_api/                # Sample API for testing
│   └── MQTestAPI/           # Test API project files
│
└── README.md
```

## Setup Instructions

### 1. IBM MQ Container Setup

1. Pull the base IBM MQ image:
```bash
docker image pull icr.io/ibm-messaging/mq:9.4.1.0-r1
```

2. Move to the directory mq_image, next build the custom MQ image:
```bash
docker build -t mq4ace .
```

3. Create and start the MQ container:
```bash
docker run \
--env LICENSE=accept \
--env MQ_QMGR_NAME=QM.MacOS.Local.IS \
--env MQ_APP_USER=app \
--env MQ_APP_PASSWORD=ace13lpmqlp \
--env MQ_ADMIN_USER=admin \
--env MQ_ADMIN_PASSWORD=ace13lpmqlp \
--volume qm-macos-local-is-vol:/mnt/mqm \
--publish 1414:1414 \
--publish 9443:9443 \
--name QM.MacOS.Local.IS \
--detach \
mq4ace
```

### 2. ACE Toolkit Configuration

1. Create a local Integration Server named `MacOS.Local.IS`
2. Configure the vault for credential storage
3. Deploy the provided Policy Project (`PPContainerizedMQ`)
4. Add the following line to `server.conf.yaml` in the overrides directory:
```yaml
remoteDefaultQueueManager: '{PPContainerizedMQ}:PContainerizedMQEndpoint'
```

5. Set Queue Manager credentials:
```bash
mqsisetdbparms -w <workDir> -n mq::QMGR::QM.MacOS.Local.IS -u admin -p ace13lpmqlp
```

## Verification

1. Access the MQ Web Console:
   - URL: https://localhost:9443/ibmmq/console
   - Credentials: admin/ace13lpmqlp

2. Verify the Integration Server connection:
   - Check the MQ Web Console for an active connection on the DEV.ADMIN.SVRCONN channel
   - Use the provided test API to send messages to MQ queues

## MQ Resources

The `mq4ace13-config.mqsc` file creates several system queues required for ACE functionality, including:
- Event-Driven Architecture (EDA) queues
- Timeout queues
- Collection queues
- Aggregation queues
- Sequence queues

## Important Notes

- The setup uses default development configuration with DEV.* prefixed queues and channels
- MQ data persists across container restarts using Docker volume
- Default ports used:
  - 1414: MQ Listener
  - 9443: MQ Web Console

## References

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [IBM MQ Container - Default Developer Configuration](https://github.com/ibm-messaging/mq-container/blob/master/docs/developer-config.md)
- [IBM App Connect Enterprise Evaluation Edition](https://www.ibm.com/resources/mrs/assets?source=swg-wmbfd)
- [ACE Documentation - Remote Default Queue Manager Configuration](https://www.ibm.com/docs/en/app-connect/13.0?topic=ccm-configuring-integration-server-use-remote-default-queue-manager)

## License

Please ensure you accept the IBM license terms when using the software components.
