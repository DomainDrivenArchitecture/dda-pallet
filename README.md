# dda-pallet
[![Clojars Project](https://img.shields.io/clojars/v/dda/dda-pallet.svg)](https://clojars.org/dda/dda-pallet)
[![Build Status](https://travis-ci.org/DomainDrivenArchitecture/dda-pallet.svg?branch=master)](https://travis-ci.org/DomainDrivenArchitecture/dda-pallet)

[![Slack](https://img.shields.io/badge/chat-clojurians-green.svg?style=flat)](https://clojurians.slack.com/messages/#dda-pallet/) | [<img src="https://domaindrivenarchitecture.org/img/meetup.svg" width=50 alt="DevOps Hacking with Clojure Meetup"> DevOps Hacking with Clojure](https://www.meetup.com/de-DE/preview/dda-pallet-DevOps-Hacking-with-Clojure) | [Website & Blog](https://domaindrivenarchitecture.org)

Anyone who has dealt with Infrastructure as Code so far, has to deal with a mix of Ruby, Python and Go. If you want to get more from the Java world and if you always wanted to try functional programming, there is now a worthy & fresh alternative: "dda-pallet". Dda-pallet is open-source, written in Clojure and runs on the proven Java Virtual Machine.
Dda-pallet provides the whole tool-chain from one hand, is really modular, testable and well-prepared for the handling of configuration data.

## Compatibility
Dda-pallet is compatible with the following versions
 * pallet 0.8
 * clojure 1.7
 * (x)ubuntu14.04 / 16.04

## Principles
 * Distinction between installation and configuration: We distinguish between seldom installation and continuous configuration.
 * Explicit state: We collect all the system state information at a defined place.
 * Full modularization: We separate our modules into common usable system adapters and company specific convention modules. Beside this general principle we are using the full tool stack provided by Clojure.
 * Configuration is data: In order to keep the interface simple, clean & isolated, we handle all configuration as data. There is no hidden magic.
 * Test-driven DevOps: Configuration as data makes it easy, to do unit tests. For the system adapters we are heading towards state-of-the-art integration tests - comparable to Kitchen / Vagrant / Server-spec.

## TechDebt Roadmap

Find closer description to the design decisions outlined here: https://dda.gitbooks.io/domaindrivenarchitecture/content/v/6aa12e6226098abf944b87485e0f45a9dcef8395/en/80_devops/40_architecture/Decisions.html

| | Version | Docker based Integration Tests | Unit Tests for Domain | Use app layer | DDD ns layout | CI | fat-folder | sozial links | SecretResolving | core-app |
| --- | --- |  ----------------------------- | --------------------- | ------------- | ------------- | --- | --------- | ------------ | --------------- | -------- |
| dda-config-commons  | 1.2.0 | - | - | - | - | x | x | x | x | - |
| dda-pallet-commons  | 1.3.0 | - | - | - | - | x | x | x | x | - |
| dda-pallet          | 2.0.1 |   | x | x | x | x | x | x | x | x |
| dda-user-crate      | 1.0.2 |   | x | x | x | x | x | x | x | x |
| dda-backup-crate    | 0.7.1 |   | x | x | x | x | x | x |   |   |
| dda-git-crate       | 1.0.1 |   | x | x | x | x | x | x | x | x |
| dda-hardening-crate | 0.3.0 |   |   |   | x | x |   |   |   |   |
| dda-httpd-crate     | 1.0.0 |   | x | x | x | x |   | x |   |   |
| dda-liferay-crate   | 1.0.0 |   | x | x | x | x | x | x | x |   |
| dda-managed-ide     | 1.0.2 |   | x | x | x | x | x | x | x | x |
| dda-managed-vm      | 1.0.1 |   | x | x | x | x | x | x | x | x |
| dda-mariadb-crate   | 1.0.0 |   |   | x | x | x | x | x | x |   |
| dda-serverspec-crate| 0.4.0 |   | x | x | x | x | x | x | x | x |
| dda-tomcat-crate    | 1.0.0 |   | x | x | x | x | x | x |   |   |

# License
Published under [apache2.0 license](LICENSE.md)
