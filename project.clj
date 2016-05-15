(defproject org.domaindrivenarchitecture/dda-crate "0.1.0-SNAPSHOT"
  :description "The dda-crate"
  :url "https://www.domaindrivenarchitecture.org"
  :pallet {:source-paths ["src"]}
  :license {:name "Apache License, Version 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [[org.clojure/clojure "1.7.0"] 
                 [prismatic/schema "1.1.0"]
                 [metosin/schema-tools "0.9.0"]
                 [com.palletops/pallet "0.8.12"]
                 [com.palletops/stevedore "0.8.0-beta.7"]
                 [ch.qos.logback/logback-classic "1.1.7"]]
  :profiles {:dev
             {:dependencies
              [[com.palletops/pallet "0.8.12" :classifier "tests"]]
              :plugins
              [[com.palletops/pallet-lein "0.8.0-alpha.1"]]}
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