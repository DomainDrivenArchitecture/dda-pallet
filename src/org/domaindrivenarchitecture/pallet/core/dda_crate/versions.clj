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

; TODO: review mje 2016.05.17: rename to singular "versioned-plan"
(ns org.domaindrivenarchitecture.pallet.core.dda-crate.versions
  (:require 
    [schema.core :as s]
    [pallet.actions :as actions]
    [pallet.core.session :as session]
    [pallet.node-value :as nv]
    [org.domaindrivenarchitecture.pallet.core.dda-crate.internals :as internals]
    ))

; TODO: review mje 2016.05.17: use config-commons version instead
(def VersionSchema internals/VersionSchema)

(defmacro plan-when-cleaninstall [dda-crate & crate-fns-or-actions]
  "Performs actions only if no statefile is found on the node"
  `(actions/plan-when 
     (nil? (internals/node-get-nv-state ~dda-crate))
     ~@crate-fns-or-actions))

(defmacro plan-when-verlessthan [dda-crate version & crate-fns-or-actions]
  "Performs actions only if node version is less than given version"
  `(actions/plan-when 
     (internals/ver_less (internals/node-get-nv-state ~dda-crate) ~version)
     ~@crate-fns-or-actions))

(ns-name *ns*)