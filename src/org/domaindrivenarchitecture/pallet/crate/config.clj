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

(ns org.domaindrivenarchitecture.pallet.crate.config
  (:require
    [pallet.api :as api]
    [pallet.crate :as crate]
    ))

(def facility :dda-config)

(defn get-global-config
  ""
  []
  (-> (crate/get-settings facility) 
    :global-config)
  )

(defn get-nodespecific-config
  ""
  ([]
    (-> (crate/get-settings facility) 
      :node-specific-config))
  ([config]
    (-> config
      :node-specific-config))
  )

(defn get-nodespecific-additional-config
  ""
  [custom-facility]
  (-> (get-nodespecific-config) 
    :additional-config 
    custom-facility)
  )

(defn with-config
  ""
  [config]
  (api/server-spec
    :phases 
    {:settings
     (api/plan-fn
       (let [id (crate/target-id)
             node-specific-config (-> config :node-specific-config id)]
         (crate/assoc-settings 
           facility    
           {:global-config config
            :node-specific-config node-specific-config}
           )
       ))
    })
  )