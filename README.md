# dda-pallet
[![Clojars Project](https://img.shields.io/clojars/v/dda/dda-pallet.svg)](https://clojars.org/dda/dda-pallet)
[![Build Status](https://travis-ci.org/DomainDrivenArchitecture/dda-pallet.svg?branch=master)](https://travis-ci.org/DomainDrivenArchitecture/dda-pallet)

[<img src="https://domaindrivenarchitecture.org/img/delta-chat.svg" width=20 alt="DeltaChat"> chat over e-mail](mailto:buero@meissa-gmbh.de?subject=community-chat) | [<img src="https://meissa-gmbh.de/img/community/Mastodon_Logotype.svg" width=20 alt="team@social.meissa-gmbh.de"> team@social.meissa-gmbh.de](https://social.meissa-gmbh.de/@team) | [Website & Blog](https://domaindrivenarchitecture.org)

dda-pallet is a DevOps system for cloud image provisioning, test driven DevOps and continuous server configuration. dda-pallet allows to build your own abstraction layer very easily as pure data to data transformation while providing a stable low level API. It is modular, testable and well-prepared for the handling of configuration data. dda-pallet is open-source, written in Clojure and runs on the proven Java Virtual Machine. Anyone who has dealt with Infrastructure as Code so far, has to deal with a mix of Ruby, Python and Go. If you want to get more from the Java world and if you always wanted to try functional programming, there is now a worthy & fresh alternative: "dda-pallet".

## Compatibility
Dda-pallet is compatible with the following versions

* pallet 0.9
* clojure 1.10
* (x)ubuntu 18.04 / 20.04

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

| | Version | [Dockerized Integration Tests](https://github.com/DomainDrivenArchitecture/dda-httpd-crate/commit/116d3f8fabcbe9b15eeee65b8d2ada15fe2143f5) | Artefacts by CI | Subcomp. Lic | [Target %s](https://github.com/DomainDrivenArchitecture/dda-managed-vm/commit/0f25a59a46e372ed6fd4a3d1fbbabd920dd9f01e) | [break on error](https://github.com/DomainDrivenArchitecture/dda-serverspec-crate/commit/980968d1544bf1341888d5a2da00c6247e23e88a) | [modern hashes](https://github.com/DomainDrivenArchitecture/dda-serverspec-crate/commit/f8ab1fd966ed596068f02b762f9620574c783cb6) | [use data-test](https://github.com/DomainDrivenArchitecture/dda-serverspec-crate/commit/43abadbdb96afde6b1dc85834e465ee61eb464d2) | [pyb for docker](https://github.com/DomainDrivenArchitecture/dda-git-crate/commit/1b8325b94a6e3ceb77b2651965b1206749dd203f) | [provizionize](https://github.com/DomainDrivenArchitecture/dda-k8s-crate/commit/ef8e9651eeb8bca9f3e6b25b528968792126e300) | [convention instead of domain](https://github.com/DomainDrivenArchitecture/dda-serverspec-crate/commit/d89060509357722a12ca408b6676e3f6eebff1f9) | [infra as primary api] | remove '-crate' |
| --- | --- |  --------------------------- | --------------- | ------------ | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- | ----------- |
| dda-config-commons  | 1.5 | - | - | x | - | - | - | - | - | - |   |   | - |
| dda-povision        | 0.2 | - | - | x | - | - | - | - | - | - |   |   | - |
| dda-pallet-commons  | 1.5 | - | - | x | - | - | - | - | - | - |   |   | - |
| dda-pallet          | 4.0 | - | - | x | - | x | - |   | - | - | x | x |   |
| dda-user-crate      | 3.0 | x | x | x | x | x | x |   | x | x | x |   |   |
| dda-backup          | 0.1 |   |   |   |   |   |   |   |   |   |   |   | x |
| dda-git-crate       | 3.0 | x | x | x | x | x | x | x | x | x | x | x |   |
| dda-hardening-crate | 1.1 |   |   |   |   |   |   |   |   |   |   |   |   |
| dda-k8s-crate       | 1.1 | x | x | x | x | x | x | x | - | x | x |   |   |
| dda-managed-ide     | 3.1 | x | x | x | x | x | x | x |   |   |   |   |   |
| dda-managed-vm      | 2.6 | x | x | x | x | x | x | x | x | x |   |   |   |
| dda-serverspec-crate| 2.0 | x | x | x | x | x | x | x | x | - | x |   |   |
| dda-smeagol-crate   | 0.1 | x | x | x | x |   |   |   |   |   |   |   |   |

## License

Copyright © 2015 - 2021 meissa GmbH
Licensed under the [Apache License, Version 2.0](LICENSE) (the "License")
Pls. find licenses of our subcomponents [here](doc/SUBCOMPONENT_LICENSE)
