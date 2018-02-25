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

(ns dda.pallet.core.infra
  (:require
   [schema.core :as s]
   [pallet.actions :as actions]
   [pallet.api :as api]
   [clojure.tools.logging :as logging]
   [dda.config.commons.map-utils :as map-utils]
   [dda.config.commons.version-model :as version-model]
   [dda.pallet.core.dda-crate.config :as config]))

(defprotocol PalletSpecification
  "Protocol for pallet-related crate functions"
  (create-infra-plan [dda-crate]
    "Creates a pallet server-spec from the infra"))


(defprotocol PhasesSpecification
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

(s/defrecord DdaCrateInfra
  [facility :- s/Keyword
   infra-schema :- {s/Any s/Any}]
  Object
  (toString [_] (str "DdaCrateInfra[facility=" (:facility _)
                     " ver=" (:version _) "]")))

(s/defn dispatch-by-crate-facility :- s/Keyword
  "Dispatcher for phase multimethods by facility. Also does a
   schema validation of arguments."
  [dda-crate :- DdaCrateInfra config]
  (:facility dda-crate))

(defmulti dda-settings
  "Multimethod for settings phase of a DdaCrateInfra."
  dispatch-by-crate-facility)
(s/defmethod dda-settings :default
  [dda-crate  :- DdaCrateInfra
   effective-configuration]
  (actions/as-action
    (logging/info
      (str dda-crate) ": doing nothing.")))

(defmulti dda-init
  "Multimethod for init phase of a DdaCrateInfra."
  dispatch-by-crate-facility)
(s/defmethod dda-init :default
  [dda-crate  :- DdaCrateInfra
   effective-configuration]
  (actions/as-action
    (logging/info
      (str dda-crate) ": doing nothing.")))

(defmulti dda-configure
  "Multimethod for configure phase of a DdaCrateInfra."
  dispatch-by-crate-facility)
(s/defmethod dda-configure :default
  [dda-crate  :- DdaCrateInfra
   effective-configuration]
  (actions/as-action
    (logging/info
      (str dda-crate) ": doing nothing.")))

(defmulti dda-install
  "Multimethod for install phase of a DdaCrateInfra."
  dispatch-by-crate-facility)
(s/defmethod dda-install :default
  [dda-crate  :- DdaCrateInfra
   effective-configuration]
  (actions/as-action
    (logging/info
      (str dda-crate) ": doing nothing.")))

(defmulti dda-test
  "Multimethod for test phase of a DdaCrateInfra."
  dispatch-by-crate-facility)
(s/defmethod dda-test :default
  [dda-crate  :- DdaCrateInfra
   effective-configuration]
  (actions/as-action
    (logging/info
      (str dda-crate) ": doing nothing.")))

(defmulti dda-app-rollout
  "Multimethod for app-rollout phase of a DdaCrateInfra."
  dispatch-by-crate-facility)
(s/defmethod dda-app-rollout :default
  [dda-crate  :- DdaCrateInfra
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

(extend-type DdaCrateInfra
  PhasesSpecification
  (settings-raw [infra dda-pallet-runtime]
    (let [config
          (get-config (get-in infra [:facility]))]
      (actions/as-action (logging/info (str infra) ": settings phase."))
      (dda-settings infra config)))
  (init-raw [infra dda-pallet-runtime]
    (let [config
          (get-config (get-in infra [:facility]))]
      (actions/as-action (logging/info (str infra) ": init phase."))
      (dda-init infra config)))
  (configure-raw [infra dda-pallet-runtime]
    (let [config
          (get-config (get-in infra [:facility]))]
      (actions/set-force-overwrite true)
      (actions/set-install-new-files true)
      (actions/as-action (logging/info (str infra) ": config phase."))
      (dda-configure infra config)))
  (install-raw [infra dda-pallet-runtime]
    (let [config
          (get-config (get-in infra [:facility]))]
      (actions/set-force-overwrite true)
      (actions/set-install-new-files true)
      (actions/as-action (logging/info (str infra) ": install phase."))
      (dda-install infra config)))
  (test-raw [infra dda-pallet-runtime]
    (let [config
          (get-config (get-in infra [:facility]))]
      (actions/as-action (logging/info (str infra) ": test phase."))
      (dda-test infra config)))
  (app-rollout-raw [infra dda-pallet-runtime]
    (let [config
          (get-config (get-in infra [:facility]))]
      (actions/as-action (logging/info (str infra) ": rollout phase."))
      (dda-app-rollout infra config)))

  PalletSpecification
  (create-infra-plan [dda-crate]
    (api/server-spec
      :phases
      {:settings (api/plan-fn (settings-raw dda-crate nil))
       :init (api/plan-fn (init-raw dda-crate nil))
       :configure (api/plan-fn (configure-raw dda-crate nil))
       :install (api/plan-fn (install-raw dda-crate nil))
       :test (api/plan-fn (test-raw dda-crate nil))
       :app-rollout (api/plan-fn (app-rollout-raw dda-crate nil))})))

(defn make-dda-crate-infra
  "Creates a DdaCrate. (Wrapper for ->DdaCrate with validation.)"
  [& {:keys [facility infra-schema]
      :or {infra-schema {s/Any s/Any}}}]
  (s/validate
    DdaCrateInfra
    (->DdaCrateInfra facility infra-schema)))
