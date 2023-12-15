# QueueMusicService

Vote on the next song.

## Architecture

The service is devided into 6 microservice.

![Architecture](https://github.com/TijsGroenendaal/QueueMusicService/blob/main/docs/deployment-architecture.drawio.png)

* IdpService: A service for registring user accounts, managing clients registrations and generating JWT tokens
* QueueMusicFacade: A facade against multiple underlying services. Implemented to remove any possible circular dependencies between micro services.
* SessionService: A service that handles:L the creation of new sessions, adding of new songs, voting on songs.
* SpotifyFacade: A facade against the Spotify API, the facade is the only microservice who will be authorized to send requests to with Spotify access tokens.
* UserEventStreamer: A Typescript Node consumer that sends websockets events to the frontend to immediatly show changes to the Session state.
* AutoPlayConsumer: A consumer that makes sure songs are added to the Spotify Queueu when a song is accepted in the session.

## Structure

I have decided to use a monorepo for faster development (since i work on this on my own :) )

All services and packages are placed in to root directory.

* QuMu alias for QueueMusic is a package tha contains some common classes like Errorcodes
* QuMuSecurity is a package that contains the configuration for JWT support. It also contains classes for encrypting data on de persitence layer.

### k8s/charts

This branch contains the Helm chart for deploying Spring Boot applications

### k8s/argocd-apps

This branch contains the values for argocd applications and the desired state of the argocd apps.
