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

(ns org.domaindrivenarchitecture.pallet.dda-crate
  (:require 
    [schema.core :as s]
    [pallet.actions :as actions]
    [pallet.api :as api]
    [clojure.tools.logging :as logging]
    [org.domaindrivenarchitecture.pallet.dda-crate.internals :as internals]
    [org.domaindrivenarchitecture.pallet.dda-crate.versions :as versions]))

(defprotocol DdaCratePalletSpecification
  "Protocol for pallet-related crate functions"
  (create-server-spec [dda-crate] 
                      "Creates a pallet server-spec from the dda-crate") 
  )

(defprotocol DdaCratePhasesSpecification
  "Protocol for pallet-related crate functions"
  (settings-raw [dda-crate dda-pallet-runtime] 
                "Raw implementation of settings phase")
  (install-raw [dda-crate dda-pallet-runtime]
               "Raw implementation of install phase"))

(s/defrecord DdaCrate 
  [facility :- s/Keyword
   version :- versions/VersionSchema
   config-default :- {s/Keyword s/Any}
   config-schema :- {s/Any s/Any}]
  Object
  (toString [_] (str "DdaCrate[facility=" (:facility _) 
                     " ver=" (:version _)"]")))

(s/defn dda-phase-dispatcher
  "Dispatcher for multimethods of phases by facility. Also does a 
   schema validation of arguments."
  [dda-crate ; TODO:- DdaCrate
   effective-configuration :- {s/Keyword s/Any}]
  (:facility dda-crate))

(defmulti dda-settings
  "Multimethod for settings phase of a DdaCrate."
  dda-phase-dispatcher)
(defmethod dda-settings :default [dda-crate effective-configuration]
  (actions/as-action
    (logging/info 
      "No dda-settings phase of" (str dda-crate) "(Doing nothing).")))

(defmulti dda-install
  "Multimethod for install phase of a DdaCrate."
  dda-phase-dispatcher)
(defmethod dda-install :default [dda-crate effective-configuration]
  (actions/as-action
    (logging/info 
      "No dda-install phase of" (str dda-crate) "(Doing nothing).")))

(extend-type DdaCrate
  DdaCratePhasesSpecification
  (settings-raw [dda-crate dda-pallet-runtime] 
    (internals/node-read-state dda-crate)
    (dda-settings dda-crate {}))
  (install-raw [dda-crate dda-pallet-runtime] 
    (actions/as-action 
      (logging/info "Nodeversion of" (str dda-crate) 
                    "is" (internals/node-get-nv-state dda-crate)))
    (dda-install dda-crate {})
    (internals/node-write-state dda-crate))
  
  DdaCratePalletSpecification
  (create-server-spec [dda-crate] 
    (api/server-spec
      :phases 
      {:settings (api/plan-fn (settings-raw dda-crate nil))
       :install (api/plan-fn (install-raw dda-crate nil))
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

(ns-name *ns*)
