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

(ns org.domaindrivenarchitecture.pallet.core.dda-crate
  (:require 
    [schema.core :as s]
    [pallet.actions :as actions]
    [pallet.api :as api]
    [clojure.tools.logging :as logging]
    [org.domaindrivenarchitecture.pallet.core.dda-crate.versioned-plan :as vp]
    [org.domaindrivenarchitecture.config.commons.version-model :as version-model]
    [org.domaindrivenarchitecture.pallet.core.dda-crate.config :as config]))

(defprotocol DdaCratePalletSpecification
  "Protocol for pallet-related crate functions"
  (create-server-spec [dda-crate] 
                      "Creates a pallet server-spec from the dda-crate") 
  )

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
  (app-rollout-raw [dda-crate dda-pallet-runtime]
                   "Raw implementation of app-rollout phase"))

(s/defrecord DdaCrate 
  [facility :- s/Keyword
   version :- version-model/Version
   config-default :- {s/Keyword s/Any}
   config-schema :- {s/Any s/Any}]
  Object
  (toString [_] (str "DdaCrate[facility=" (:facility _) 
                     " ver=" (:version _)"]")))

(defn dispatch-by-crate-facility
  "Dispatcher for phase multimethods by facility. Also does a 
   schema validation of arguments."
  [dda-crate ;:- DdaCrate
   effective-configuration]
  (:facility dda-crate))

(defmulti dda-settings
  "Multimethod for settings phase of a DdaCrate."
  dispatch-by-crate-facility)
(defmethod dda-settings :default [dda-crate effective-configuration]
  (actions/as-action
    (logging/info 
      "No dda-settings phase of" (str dda-crate) "(Doing nothing).")))

(defmulti dda-init
  "Multimethod for init phase of a DdaCrate."
  dispatch-by-crate-facility)
(defmethod dda-init :default [dda-crate effective-configuration]
  (actions/as-action
    (logging/info 
      "No dda-init phase of" (str dda-crate) "(Doing nothing).")))

(defmulti dda-configure
  "Multimethod for configure phase of a DdaCrate."
  dispatch-by-crate-facility)
(defmethod dda-configure :default [dda-crate effective-configuration]
  (actions/as-action
    (logging/info 
      "No dda-configure phase of" (str dda-crate) "(Doing nothing).")))

(defmulti dda-install
  "Multimethod for install phase of a DdaCrate."
  dispatch-by-crate-facility)
(defmethod dda-install :default [dda-crate effective-configuration]
  (actions/as-action
    (logging/info 
      "No dda-install phase of" (str dda-crate) "(Doing nothing).")))

(defmulti dda-app-rollout
  "Multimethod for app-rollout phase of a DdaCrate."
  dispatch-by-crate-facility)
(defmethod dda-app-rollout :default [dda-crate effective-configuration]
  (actions/as-action
    (logging/info 
      "No dda-app-rollout phase of" (str dda-crate) "(Doing nothing).")))

(extend-type DdaCrate
  DdaCratePhasesSpecification
  (settings-raw [dda-crate dda-pallet-runtime]
    (let [effective-config 
          (config/get-nodespecific-additional-config (get-in dda-crate [:facility]))]
      (vp/node-read-state dda-crate)
      (dda-settings dda-crate effective-config)))
  (init-raw [dda-crate dda-pallet-runtime]
    (let [effective-config 
          (config/get-nodespecific-additional-config (get-in dda-crate [:facility]))]
    (dda-init dda-crate effective-config)))
  (configure-raw [dda-crate dda-pallet-runtime]
    (let [effective-config 
          (config/get-nodespecific-additional-config (get-in dda-crate [:facility]))]
      (dda-configure dda-crate effective-config)))
  (install-raw [dda-crate dda-pallet-runtime]
    (let [effective-config 
          (config/get-nodespecific-additional-config (get-in dda-crate [:facility]))]
      (actions/as-action 
        (logging/info "Nodeversion of" (str dda-crate)
                      "is" (vp/node-get-nv-state dda-crate)))
      (dda-install dda-crate effective-config)
      (vp/node-write-state dda-crate)))
  (app-rollout-raw [dda-crate dda-pallet-runtime]
    (let [effective-config 
          (config/get-nodespecific-additional-config (get-in dda-crate [:facility]))]
      (dda-app-rollout dda-crate effective-config)))
  
  DdaCratePalletSpecification
  (create-server-spec [dda-crate] 
    (api/server-spec
      :phases 
      {:settings (api/plan-fn (settings-raw dda-crate nil))
       :init (api/plan-fn (init-raw dda-crate nil))
       :configure (api/plan-fn (configure-raw dda-crate nil))
       :install (api/plan-fn (install-raw dda-crate nil))
       :app-rollout (api/plan-fn (app-rollout-raw dda-crate nil))
       }))
  )

(defn make-dda-crate
  "Creates a DdaCrate. (Wrapper for ->DdaCrate with validation.)"
  [& {:keys [facility version config-default config-schema]
      :or {config-default {} 
           config-schema {s/Any s/Any}}
      }]
  (s/validate
    DdaCrate
    (->DdaCrate facility version config-default config-schema)))
