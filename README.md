# dda-pallet
[![Clojars Project](https://img.shields.io/clojars/v/dda/dda-pallet.svg)](https://clojars.org/dda/dda-pallet)
[![Build Status](https://travis-ci.org/DomainDrivenArchitecture/dda-pallet.svg?branch=master)](https://travis-ci.org/DomainDrivenArchitecture/dda-pallet)

[![Slack](https://img.shields.io/badge/chat-clojurians-green.svg?style=flat)](https://clojurians.slack.com/messages/#dda-pallet/) | [<img src="https://domaindrivenarchitecture.org/img/meetup.svg" width=50 alt="DevOps Hacking with Clojure Meetup"> DevOps Hacking with Clojure](https://www.meetup.com/de-DE/preview/dda-pallet-DevOps-Hacking-with-Clojure) | [Website & Blog](https://domaindrivenarchitecture.org)

dda-pallet is a DevOps system for cloud image provisioning, test driven DevOps and continuous server configuration. dda-pallet allows to build your own abstraction layer very easily as pure data to data transformation while providing a stable low level API. It is modular, testable and well-prepared for the handling of configuration data. dda-pallet is open-source, written in Clojure and runs on the proven Java Virtual Machine. Anyone who has dealt with Infrastructure as Code so far, has to deal with a mix of Ruby, Python and Go. If you want to get more from the Java world and if you always wanted to try functional programming, there is now a worthy & fresh alternative: "dda-pallet".

## Compatibility
Dda-pallet is compatible with the following versions
 * pallet 0.8
 * clojure 1.7
 * (x)ubuntu14.04 / 16.04

## Principles
  * Adaptable architectural Abstraction Layer: Every Module provides two levels of API. First the low level API oriented on linux installation artifacts is named **Infrastructure API**. The  high level API represents every kind of abstraction and it is named **Domain API**. So the Infrastructure API is the place for collaboration and knowldege sharing across the whole community. The Domain API is the place to encapsulate your own defaults and architectural decisions. Even if we provide our Domain API as example, it is very easy and intended for you to build your own abstractions aside.
 * Imutable infrastructure: We do most of our installations as one shoot installation. This helps to keep things simple and aligns to the clouds immutable infrastructure model.
 * Distinction between installation and configuration: We distinguish between one shoot installation and continuous configuration. Sometimes immutability does not work for infrastructure, sometimes a rapid configuration change has to be applied to stateful targets. For these cases our modules use the configuration stage.
 * Facts: We collect all the system state information needed at a defined place and point in time. We call them facts. We use facts for running our tests and sometimes to decide how to do continuous configuration. But to keep things simple, we try to use facts very rarely.
 * Full modularization: We separate our modules into common usable system adapters and company specific convention modules. Beside this general principle we are using the full tool stack provided by Clojure. Every pice of code may be factored out as cloujure jar, named and versioned.
 * Configuration is data: In order to keep the interface simple, clean & isolated, we handle all configuration as data. There is no hidden magic.
 * Test-driven DevOps: Domain API as data to data transformation makes it easy, to do unit tests. For the Infrastructure API we are heading towards state-of-the-art integration tests - comparable to Kitchen / Vagrant / Server-spec or packer.

dda-pallet is the core library. If you are looking for ready to provision modules you will find many dda-pallet modules named dda-\*-crate aside of this project. Most of them provide a fat-jar for instant usage.

## TechDebt Roadmap

Find closer description to the design decisions outlined here: https://dda.gitbooks.io/domaindrivenarchitecture/content/v/6aa12e6226098abf944b87485e0f45a9dcef8395/en/80_devops/40_architecture/Decisions.html

| | Version | Docker based Integration Tests | Unit Tests for Domain | Use app layer | DDD ns layout | CI | fat-folder | sozial links | SecretResolving | core-app |
| --- | --- |  ----------------------------- | --------------------- | ------------- | ------------- | --- | --------- | ------------ | --------------- | -------- |
| dda-config-commons  | 1.3.0 | - | - | - | - | x | x | x | x | - |
| dda-pallet-commons  | 1.3.0 | - | - | - | - | x | x | x | x | - |
| dda-pallet          | 2.1.2 |   | x | x | x | x | x | x | x | x |
| dda-user-crate      | 1.0.3 |   | x | x | x | x | x | x | x | x |
| dda-backup-crate    | 1.0.1 |   | x | x | x | x | x | x | x | x |
| dda-git-crate       | 1.0.1 |   | x | x | x | x | x | x |   | x |
| dda-hardening-crate | 1.0.2 |   | x | x | x | x | x | x | - | x |
| dda-httpd-crate     | 2.0.4 |   | x | x | x | x | x | x | x | x |
| dda-liferay-crate   | 1.0.0 |   | x | x | x | x | x | x | x |   |
| dda-managed-ide     | 1.0.2 |   | x | x | x | x | x | x | x | x |
| dda-managed-vm      | 1.0.1 |   | x | x | x | x | x | x | x | x |
| dda-mariadb-crate   | 1.0.1 |   |   | x | x | x | x | x | x | x |
| dda-serverspec-crate| 1.0.6 |   | x | x | x | x | x | x | x | x |
| dda-tomcat-crate    | 2.0.0 |   | x | x | x | x | x | x | - | x |

# License
Published under [apache2.0 license](LICENSE.md)
