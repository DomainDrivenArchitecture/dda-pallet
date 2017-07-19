# dda-pallet
Anyone who has so far dealt with Infrastructure as Code has to deal with a mix of Ruby, Python and go. If you want to get more from the Java world and always wanted to try functional programming, there is now a worthy & fresh alternative: "dda-pallet". Dda-pallet is OpenSource, written in clojure, running on the proven Java Virtual Machine.
dda-pallet provides the whole tool-chain from one hand, is real modular, testable and well prepared for configuration data handling.

## compatability
dda-pallet is compatible to the following versions
 * pallet 0.8
 * clojure 1.7
 * (x)ubunutu14.04 / 16.04

## Principles
 * Distinction between installation and configuration: We distinguish between seldom installation and continuous configuration.
 * Explicit State: We collect all the system state information to a defined place.
 * Full Modularization: We separate our modules into common usable system adapters and company specific convention modules. Beside this general principle we are using the full tool stack provided by clojure.
 * Configuration is data: In order to keep the interface simple, clean & isolated, we handle all configuration as data. There is no magic hidden in.
 * Test Driven DevOps: Configuration as data makes it easy, to do unit tests. For the system adapters we are heading towards to state of the art integration tests - comparable to kitchen / vagrant / server-spec.

##More information
* dda-pallet is a clojure based config management system build upon the great config management framework [pallet](https://github.com/pallet/pallet) build by to Hugo Duncan.
* Website & Blog: [DomanDrivenArchitecture.org](https://domaindrivenarchitecture.org)
* Commercial: [meissa-gmbh.de](https://meissa-gmbh.de)
* Slack-Channel: [dda-pallet@clojurians.slack.com](https://clojurians.slack.com/messages/C5GDWDF28/)
* [Requirements & Architecture](https://dda.gitbooks.io/domaindrivenarchitecture/content/en/80_config_management/index.html)

## TechDebt Roadmap

Find closer description to the DesignDecisions outlined here: https://dda.gitbooks.io/domaindrivenarchitecture/content/v/027ddde7df59bc3a6ccabc97b23e401462fafb6a/en/80_devops/40_architecture/Decisions.html

| | Version | Separate Domain from Infrastructure | Integration Folder | Docker based Integration Tests | Unit Tests for Domain | Boundaries | Input / Output Spec | Short Package | Composition over API | Group-based Configuration | Use dda-pallet aws/existing | Use app layer | DDD ns layout |
| --- | --- |  --- |--- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| dda-backup-crate | 0.4.2-SNAPSHOT |  |  |  | | x |  |  | |  |  |  |  |
| dda-config-commons| 0.2.0-SNAPSHOT | n.a. | n.a. | n.a. | n.a. |  |  | x | n.a | n.a | n.a. | n.a. | n.a. |
| dda-git-crate   | 0.1.0-SNAPSHOT | x | x |  | x | x | x | x | x | x | x | x | x |
| dda-hardening-crate| 0.2.0-SNAPSHOT | x | x |  |  | x | x | x | x | x | x |  |  |
| dda-httpd-crate| 0.2.2-SNAPSHOT | x | x | x |  | x | x | x | x | x | x | x | x |
| dda-init-crate|  |  |  |  | |  |  |  | ||  |  |  |
| dda-liferay-crate|  |  |  |  | |  |  |  | ||  |  |  |
| dda-linkeddata-crate|  |  |  |  | |  |  |  || |  |  |  |
| dda-managed-ide| 0.1.3-SNAPSHOT | x | x |  | x | x |  | x | x | x |  |  |  |
| dda-managed-vm| 0.2.2-SNAPSHOT | x | x |  |  | x | x | x | partial | x |  |  |  |
| dda-mysql-crate|  |  |  |  | |  |  |  | ||  |  |  |
| dda-pallet           | 0.5.0 | x | n.a. |  | x |  | x |  | x | x | x | x | x |
| dda-pallet-commons| n.a. | n.a. | n.a |  |  |  |  |  |  | n.a. |  | n.a. |  |
| dda-provider-crate|  |  |  |  | |  |  ||  | |  |  |  |  |
| dda-servertest-crate| 0.2.0-SNAPSHOT | x | x |  | x | x | x | x | x | x | x | x | x |
| dda-tomcat-crate| 0.1.6-SNAPSHOT |  |  |  | | x |  |  | ||  |  |  |
| dda-user-crate| 0.5.0 | x | x |  | x | x | x | x | x | x | x | x | x |
| dda-basic-crate | deprecated |  |  |  | |  |  |  | ||  |  |  |
| dda-config-crate| deprecated |  |  |  |  |  |  |  |  |  |  |  |  |
| dda-collected-crate| deprecated |  |  |  | |  |  |  |  |  |  |  |  |
| dda-iptables-crate| deprecated |  |  |  |  |  |  | x |  |  |  |  |  |

# License
Published under [apache2.0 license](LICENSE.md)
