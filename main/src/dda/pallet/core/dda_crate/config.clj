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

(ns dda.pallet.core.dda-crate.config
  (:require
    [pallet.api :as api]
    [pallet.crate :as crate]))

(defn get-global-config
  ""
  []
  (get-in (crate/get-settings :dda-config) [:global-config]))

(defn get-node-specific-config
  "get the node spec. config"
  []
  (get-in (crate/get-settings :dda-config) [:node-specific-config]))

(defn get-nodespecific-config []
  (get-node-specific-config))

(defn get-node-specific-additional-config
  ""
  [custom-facility]
  (get-in (get-node-specific-config) [:additional-config custom-facility]))

(defn get-nodespecific-additional-config
  [custom-facility]
  (get-node-specific-additional-config custom-facility))

(defn get-group-specific-config
  "get the group spec. configuration"
  []
  (get-in (crate/get-settings :dda-config) [:group-specific-config]))

(defn get-group-specific-additional-config
  ""
  [custom-facility]
  (get-in (get-group-specific-config) [:additional-config custom-facility]))
