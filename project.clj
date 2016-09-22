(defproject org.domaindrivenarchitecture/dda-pallet "0.1.1-SNAPSHOT"
  :description "The dda-crate"
  :url "https://www.domaindrivenarchitecture.org"
  :pallet {:source-paths ["src"]}
  :license {:name "Apache License, Version 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [prismatic/schema "1.1.3"]
                 [com.palletops/pallet "0.8.12"]
                 [com.palletops/stevedore "0.8.0-beta.7"]
                 [ch.qos.logback/logback-classic "1.1.7"]
                 [org.domaindrivenarchitecture/dda-config-commons "0.1.6-SNAPSHOT"]
                 [org.domaindrivenarchitecture/dda-pallet-commons "0.2.0-SNAPSHOT"]]
  :profiles {:dev
             {:dependencies
              [[org.clojure/test.check "0.9.0"]
               [com.palletops/pallet "0.8.12" :classifier "tests"]]
              :plugins
              [[lein-sub "0.3.0"]]}
              :leiningen/reply
               {:dependencies [[org.slf4j/jcl-over-slf4j "1.7.21"]]
                :exclusions [commons-logging]}}
  :local-repo-classpath true
  :repositories [["snapshots" :clojars]
                 ["releases" :clojars]]
  :deploy-repositories [["snapshots" :clojars]
                        ["releases" :clojars]]
  :classifiers {:tests {:source-paths ^:replace ["test"]
                        :resource-paths ^:replace []}})