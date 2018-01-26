; Licensed to the Apache Software Foundation (ASF) under one
; or more contributor license agreements. See the NOTICE file
; distributed with this work for additional information
; regarding copyright ownership. The ASF licenses this file
; to you under the Apache License, Version 2.0 (the
; "License"); you may not use this file except in compliance
; with the License. You may obtain a copy of the License at
;
; http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.

(ns dda.pallet.core.dda-crate
  (:require
   [schema.core :as s]
   [pallet.actions :as actions]
   [pallet.api :as api]
   [clojure.tools.logging :as logging]
   [dda.config.commons.map-utils :as map-utils]
   [dda.config.commons.version-model :as version-model]
   [dda.pallet.core.dda-crate.config :as config]))

(defprotocol DdaCratePalletSpecification
  "Protocol for pallet-related crate functions"
  (create-server-spec [dda-crate]
    "Creates a pallet server-spec from the dda-crate"))


(defprotocol DdaCratePhasesSpecification
  "Protocol for pallet-related crate functions"
  (settings-raw [dda-crate dda-pallet-runtime]
                "Raw implementation of settings phase")
  (init-raw [dda-crate dda-pallet-runtime]
            "Raw implementation of configure init")
  (configure-raw [dda-crate dda-pallet-runtime]
                 "Raw implementation of configure phase")
  (install-raw [dda-crate dda-pallet-runtime]
               "Raw implementation of install phase")
  (test-raw [dda-crate dda-pallet-runtime]
            "Raw implementation of install phase")
  (app-rollout-raw [dda-crate dda-pallet-runtime]
                   "Raw implementation of app-rollout phase"))

(s/defrecord DdaCrate
  [facility :- s/Keyword
   version :- version-model/Version
   config-default :- {s/Keyword s/Any}
   config-schema :- {s/Any s/Any}]
  Object
  (toString [_] (str "DdaCrate[facility=" (:facility _)
                     " ver=" (:version _) "]")))

(s/defn dispatch-by-crate-facility :- s/Keyword
  "Dispatcher for phase multimethods by facility. Also does a
   schema validation of arguments."
  [dda-crate :- DdaCrate config]
  (:facility dda-crate))


(defmulti merge-config
  "Merges configuration with the crates default configuration"
  dispatch-by-crate-facility)
(s/defmethod merge-config :default
  [dda-crate :- DdaCrate
   partial-config]
  (let [default (if (nil? (:config-default dda-crate)) {} (:config-default dda-crate))
        partial (if (nil? partial-config) {} partial-config)
        schema (:config-schema dda-crate)]
    (s/validate schema (map-utils/deep-merge default partial))))

(defmulti dda-settings
  "Multimethod for settings phase of a DdaCrate."
  dispatch-by-crate-facility)
(s/defmethod dda-settings :default
  [dda-crate  :- DdaCrate
   effective-configuration]
  (actions/as-action
    (logging/info
      (str dda-crate) ": doing nothing.")))

(defmulti dda-init
  "Multimethod for init phase of a DdaCrate."
  dispatch-by-crate-facility)
(s/defmethod dda-init :default
  [dda-crate  :- DdaCrate
   effective-configuration]
  (actions/as-action
    (logging/info
      (str dda-crate) ": doing nothing.")))

(defmulti dda-configure
  "Multimethod for configure phase of a DdaCrate."
  dispatch-by-crate-facility)
(s/defmethod dda-configure :default
  [dda-crate  :- DdaCrate
   effective-configuration]
  (actions/as-action
    (logging/info
      (str dda-crate) ": doing nothing.")))

(defmulti dda-install
  "Multimethod for install phase of a DdaCrate."
  dispatch-by-crate-facility)
(s/defmethod dda-install :default
  [dda-crate  :- DdaCrate
   effective-configuration]
  (actions/as-action
    (logging/info
      (str dda-crate) ": doing nothing.")))

(defmulti dda-test
  "Multimethod for test phase of a DdaCrate."
  dispatch-by-crate-facility)
(s/defmethod dda-test :default
  [dda-crate  :- DdaCrate
   effective-configuration]
  (actions/as-action
    (logging/info
      (str dda-crate) ": doing nothing.")))

(defmulti dda-app-rollout
  "Multimethod for app-rollout phase of a DdaCrate."
  dispatch-by-crate-facility)
(s/defmethod dda-app-rollout :default
  [dda-crate  :- DdaCrate
   effective-configuration]
  (actions/as-action
    (logging/info
      (str dda-crate) ": doing nothing.")))

(defn get-config
  "get either the node- or the group-specific additional configuration."
  [facility]
  (let [node-add-config (config/get-node-specific-additional-config facility)
        group-add-config (config/get-group-specific-additional-config facility)
        group-config (facility  (config/get-group-specific-config))]
    (cond
      (some? node-add-config) node-add-config
      (some? group-add-config) group-add-config
      :default group-config)))

(extend-type DdaCrate
  DdaCratePhasesSpecification
  (settings-raw [dda-crate dda-pallet-runtime]
    (let [partial-effective-config
          (get-config (get-in dda-crate [:facility]))]
      (actions/as-action (logging/info (str dda-crate) ": settings phase."))
      (dda-settings dda-crate partial-effective-config)))
  (init-raw [dda-crate dda-pallet-runtime]
    (let [partial-effective-config
          (get-config (get-in dda-crate [:facility]))]
      (actions/as-action (logging/info (str dda-crate) ": init phase."))
      (dda-init dda-crate partial-effective-config)))
  (configure-raw [dda-crate dda-pallet-runtime]
    (let [partial-effective-config
          (get-config (get-in dda-crate [:facility]))]
      (actions/set-force-overwrite true)
      (actions/set-install-new-files true)
      (actions/as-action (logging/info (str dda-crate) ": config phase."))
      (dda-configure dda-crate partial-effective-config)))
  (install-raw [dda-crate dda-pallet-runtime]
    (let [partial-effective-config
          (get-config (get-in dda-crate [:facility]))]
      (actions/set-force-overwrite true)
      (actions/set-install-new-files true)
      (actions/as-action (logging/info (str dda-crate) ": install phase."))
      (dda-install dda-crate partial-effective-config)))
  (test-raw [dda-crate dda-pallet-runtime]
    (let [partial-effective-config
          (get-config (get-in dda-crate [:facility]))]
      (actions/as-action (logging/info (str dda-crate) ": test phase."))
      (dda-test dda-crate partial-effective-config)))
  (app-rollout-raw [dda-crate dda-pallet-runtime]
    (let [partial-effective-config
          (get-config (get-in dda-crate [:facility]))]
      (actions/as-action (logging/info (str dda-crate) ": rollout phase."))
      (dda-app-rollout dda-crate partial-effective-config)))

  DdaCratePalletSpecification
  ;; TODO jem 2016_09_26: we are not creating server spec but some kind of phase plan here. rename!
  (create-server-spec [dda-crate]
    (api/server-spec
      :phases
      {:settings (api/plan-fn (settings-raw dda-crate nil))
       :init (api/plan-fn (init-raw dda-crate nil))
       :configure (api/plan-fn (configure-raw dda-crate nil))
       :install (api/plan-fn (install-raw dda-crate nil))
       :test (api/plan-fn (test-raw dda-crate nil))
       :app-rollout (api/plan-fn (app-rollout-raw dda-crate nil))})))

(defn make-dda-crate
  "Creates a DdaCrate. (Wrapper for ->DdaCrate with validation.)"
  [& {:keys [facility version config-default config-schema]
      :or {config-default {}
           config-schema {s/Any s/Any}}}]

  (s/validate
    DdaCrate
    (->DdaCrate facility version config-default config-schema)))
