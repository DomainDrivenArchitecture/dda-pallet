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

(ns org.domaindrivenarchitecture.pallet.test-crate
  (:require 
    [pallet.actions :as actions]
    [pallet.api]
    [pallet.compute]
    [pallet.compute.node-list]
    [pallet.crate :as crate]
    [pallet.core.session :as session]
    [pallet.node-value :as nv]
    [pallet.repl]
    [clojure.tools.logging :as logging]
    [org.domaindrivenarchitecture.pallet.core.dda-crate :refer :all]
    [org.domaindrivenarchitecture.config.commons.version-model :refer :all]
    [org.domaindrivenarchitecture.pallet.core.dda-crate.versioned-plan :refer :all]))

(schema.core/set-fn-validation! true)

(def TestCrate 
  (make-dda-crate
    :facility :testcrate 
    :version [1 3 0]))

(defmethod dda-install :testcrate [dda-crate config]  
  (plan-when-cleaninstall dda-crate
    (pallet.actions/as-action (clojure.tools.logging/info "Oh, it's clean install!")))
  (plan-when-verlessthan dda-crate [1 2]
    (pallet.actions/as-action (clojure.tools.logging/info "Oh, it's less than [1 2]")))
  (plan-when-verlessthan dda-crate [1 3]
    (pallet.actions/as-action (clojure.tools.logging/info "Oh, it's less than [1 3]")))
  )

(def with-test (create-server-spec TestCrate))

;;; Do a small local test

(def mygroup
  (pallet.api/group-spec
    "mygroup" :extends [with-test]))
(def localhost-node
  (pallet.compute.node-list/make-localhost-node
    :group-name "mygroup"))
(def node-list
  (pallet.compute/instantiate-provider
    "node-list" :node-list [localhost-node]))

(pallet.repl/explain-session 
    (pallet.api/lift
           mygroup
           :user (pallet.api/make-user "pallet")
           :compute node-list
           :phase '(:settings :install))
    :show-detail false)