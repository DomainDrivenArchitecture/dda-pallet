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

(ns org.domaindrivenarchitecture.pallet.core.cli-helper
  (:require
    [pallet.api :as api]
    [pallet.repl :as repl]
    [org.domaindrivenarchitecture.pallet.crate.config.node :as node-type]
    [org.domaindrivenarchitecture.pallet.crate.user.user-for-transport :as cm-user]))

    
(defn execute-init-node
  [& {:keys [id
             group-spec
             config 
             provider]}]
  (repl/explain-session 
    (api/lift
      group-spec
      ;TODO: review jem 2016.05.17: remove direct config access here - use session instead
      :user (cm-user/get-cm-user (get-in config [:node-specific-config id])) 
      :compute provider
      :phase :init)
    :show-detail false))
  
(defn execute-main
  [group-spec 
   provider
   & {:keys [phase]
      :or {phase '(:settings :configure)}}]
  (repl/explain-session 
    (api/lift
           group-spec
           :user (cm-user/pallet-user-for-cm)
           :compute provider
           :phase phase)
    :show-detail false))