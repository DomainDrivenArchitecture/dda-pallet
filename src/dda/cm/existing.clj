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

(ns dda.cm.existing
  (:require
    [pallet.compute.node-list :as node-list]
    [pallet.compute :as compute]))

(defn remote-node [provisioning-ip]
  (node-list/make-node
    "meissa-managed-vm-integration"
    "meissa-managed-vm-group"
    provisioning-ip
    :ubuntu))

(defn provider [provisioning-ip]
  (compute/instantiate-provider
    "node-list"
    :node-list [(remote-node provisioning-ip)]))

(defn node-spec [provisioning-user]
  {:image
   {:login-user provisioning-user}})
