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

(ns org.domaindrivenarchitecture.pallet.crate.config.node
  (:require
    [pallet.api :as api])
   (:gen-class :main true))

(defrecord Node
  [host-name 
   domain-name 
   pallet-cm-user-name 
   pallet-cm-user-password 
   additional-config])

(defn new-node
  ""
  [& {:keys [host-name 
             domain-name 
             pallet-cm-user-name 
             pallet-cm-user-password 
             additional-config]
      :or {pallet-cm-user-password nil 
           additional-config nil}}]
    (Node. host-name domain-name pallet-cm-user-name pallet-cm-user-password additional-config)
  )