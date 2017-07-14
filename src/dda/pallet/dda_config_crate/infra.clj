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

(ns dda.pallet.dda-config-crate.infra
  (:require
    [pallet.api :as api]
    [pallet.crate :as crate]
    [dda.pallet.core.dda-crate :as dda-crate]))


(defmethod dda-crate/dda-settings :dda-config [dda-crate effective-config]
  (let [node-id (crate/target-id)
        group-name (crate/group-name)
        config (get-in dda-crate [:config-default])
        group-specific-config (get-in config [:group-specific-config group-name])
        node-specific-config (get-in config [:node-specific-config node-id])]
    (crate/assoc-settings
      (get-in dda-crate [:facility])
      {:global-config config
       :group-specific-config group-specific-config
       :node-specific-config node-specific-config})))


(defn with-config
  ""
  [config]
  (let
    [config-crate (dda-crate/make-dda-crate
                    :facility :dda-config
                    :version [1 4 0]
                    :config-default config)]
    (dda-crate/create-server-spec config-crate)))
