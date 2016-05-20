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

;Deprercated

(ns org.domaindrivenarchitecture.pallet.core.user-for-transport
  (:require
    [pallet.api :as api]))

(defn get-cm-user
  ([node]
  (let [user-name (:pallet-cm-user-name node)
        user-pwd (:pallet-cm-user-password node)]
    (get-cm-user user-name user-pwd)
    ))
  ([user-name user-pwd]
    (if (nil? user-pwd)
      (api/make-user user-name)
      (api/make-user 
        user-name 
        :password user-pwd 
        :no-sudo (= user-name "root")))
    )  
  )

(defn password-user-for-cm
  "create the user to bootstrap the system"
  [user-name user-password]
  (api/make-user user-name :password user-password :no-sudo (= user-name "root"))
  )

(defn pallet-user-for-cm
  "create the user for regular further configuration"
  []
  (api/make-user "pallet")
  )