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
(ns org.domaindrivenarchitecture.cm.operation
  (:require
    [schema.core :as s]
    [pallet.api :as api]))

(defn login-user [group]
  (-> group :image :login-user))

(defn do-apply-configure
  "applies only the settings and configuration phase to a target.
function awaits the login user set in (-> group :image :login-user)."
  ([provider group]
    (api/lift
      group
      :compute provider
      :phase '(:settings :configure)
      :user (api/make-user (login-user group))))
    )

(defn do-apply-install
    "applies the settings, init, install and configuration to a target.
function awaits the login user set in (-> group :image :login-user)."
  ([provider group]
    (api/lift
      group
      :compute provider
      :phase '(:settings :init :install :configure)
      :user (api/make-user (login-user group))))
    )

(defn do-converge-install
    "Converges [count] nodes and applies the settings, init, install and configuration phase to a target.
function awaits the login user set in (-> group :image :login-user)."
  ([provider group]
    (api/converge
      group
      :compute provider
      :phase '(:settings :init :install :configure)
      :user (api/make-user (login-user group)))
    )
  )

(defn do-vm-test
    "applies only the settings and test (without side effects by convention) phase of group to a target.
function awaits the login user set in (-> group :image :login-user)."
  ([provider group]
    (api/lift
      group
      :compute provider
      :phase '(:settings :test)
      :user (api/make-user (login-user group)))
    )
  )
