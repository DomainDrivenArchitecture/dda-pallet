(defproject dda/dda-pallet "0.6.1-SNAPSHOT"
  :description "The dda-crate"
  :url "https://www.domaindrivenarchitecture.org"
  :pallet {:source-paths ["src"]}
  :license {:name "Apache License, Version 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [commons-codec "1.11"]
                 [prismatic/schema "1.1.7"]
                 [com.palletops/pallet "0.8.12"]
                 [com.palletops/stevedore "0.8.0-beta.7"]
                 [dda/dda-config-commons "0.2.3"]
                 [dda/dda-pallet-commons "0.5.0"]]
  :profiles {:dev
             {:dependencies
              [[org.clojure/test.check "0.10.0-alpha2"]
               [ch.qos.logback/logback-classic "1.2.3"]
               [com.palletops/pallet "0.8.12" :classifier "tests"]]
              :plugins
              [[lein-sub "0.3.0"]]}
             :leiningen/reply
             {:dependencies [[org.slf4j/jcl-over-slf4j "1.8.0-beta0"]]
              :exclusions [commons-logging]}}
  :local-repo-classpath true
  :repositories [["snapshots" :clojars]
                 ["releases" :clojars]]
  :deploy-repositories [["snapshots" :clojars]
                        ["releases" :clojars]]
  :classifiers {:tests {:source-paths ^:replace ["test"]
                        :resource-paths ^:replace []}})
