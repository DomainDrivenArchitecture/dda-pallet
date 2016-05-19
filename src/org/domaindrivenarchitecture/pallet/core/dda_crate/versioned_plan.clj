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

(ns org.domaindrivenarchitecture.pallet.core.dda-crate.versioned-plan
  (:require 
    [schema.core :as s]
    [pallet.actions :as actions]
    [pallet.core.session :as session]
    [pallet.crate :as crate]
    [pallet.node-value :as nv]
    [pallet.stevedore :as stevedore]
    [org.domaindrivenarchitecture.config.commons.version-model :as version-model]
    ))

(defmacro plan-when-cleaninstall [dda-crate & crate-fns-or-actions]
  "Performs actions only if no statefile is found on the node"
  `(actions/plan-when 
     (nil? (node-get-nv-state ~dda-crate))
     ~@crate-fns-or-actions))

(defmacro plan-when-verlessthan [dda-crate version & crate-fns-or-actions]
  "Performs actions only if node version is less than given version"
  `(actions/plan-when 
     (version-model/ver_less (node-get-nv-state ~dda-crate) ~version)
     ~@crate-fns-or-actions))


(defn install-marker-path [dda-crate]
  "Gets the path of state marker."
  (str "/home/pallet/state/" (name (:facility dda-crate))))

(defn node-write-state [dda-crate]
  "Creates an actions that writes a state file to the node."
   (actions/remote-file
      (install-marker-path dda-crate) 
      :overwrite-changes true
      :literal true
      :content (version-model/ver_str (:version dda-crate))))

(defn node-get-nv-state [dda-crate]
  "Read the node version as node-value from settings after it was set by 
   node-read-state."
  (-> dda-crate :facility crate/get-settings :node-version version-model/ver_fromstr))

(defn node-read-state [dda-crate]
  "Read the remote statefile of an app and returns the content as a nodevalue."
  (let [statefile (install-marker-path dda-crate)]
	  ; set version to `nil`, if no state file exists
	  (actions/plan-when-not
	    (stevedore/script (file-exists? ~statefile))
      (actions/assoc-settings (:facility dda-crate) {:node-version nil})
	    )
	  ; set version according to state file, if file exists
	  (actions/plan-when
	    (stevedore/script (file-exists? ~statefile))
      (actions/assoc-settings 
        (:facility dda-crate) 
        {:node-version (actions/remote-file-content statefile)})
      )))

(ns-name *ns*)