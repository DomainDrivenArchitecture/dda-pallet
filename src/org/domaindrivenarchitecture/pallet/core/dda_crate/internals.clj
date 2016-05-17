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

(ns org.domaindrivenarchitecture.pallet.core.dda-crate.internals
  (:require 
    [schema.core :as s]
    [clojure.tools.logging :as logging]
    [pallet.actions :as actions]
    [pallet.crate :as crate]
    [pallet.node-value :as nv]
    [pallet.stevedore :as stevedore]
    [pallet.core.session :refer [session session! *session*]]
    ))

; TODO: review mje 2016.05.17: use config-commons version instead
(def VersionSchema [s/Int])

; TODO: review mje 2016.05.17: move to config-commons version
(s/defn ver_str2int :- s/Int
  "Converts string to integer, ignores all characters but 0-9. 
   Returns 0 if string is empty."
  [str :- s/Str]
  (if (= 0 (count (re-find  #"\d+" str )))
    0
    (Integer. (re-find  #"\d+" str ))))

; TODO: review mje 2016.05.17: move to config-commons version
(s/defn ver_fromstr :- VersionSchema
  "Converts formated version string (e.g. 1.2.3.4) to version vector [1 2 3 4],
   nil values are preserved"
  [str :- (s/maybe s/Str)]
  (if (nil? str)
    nil
    (into [] (map ver_str2int (clojure.string/split str #"\.")))))

; TODO: review mje 2016.05.17: move to config-commons version
(s/defn ver_str :- s/Str
 "Converts verstion vector to point seperated string.
   Example (= (ver_str [2 1 4]) \"2.1.4\") "
 [version :- VersionSchema]
 (clojure.string/join "." version))

; TODO: review mje 2016.05.17: move to config-commons version
(defn ver_fill [ver length]
  "Fills up a version vector with trailing zeros.
   Example (= (verfill [2 1] 4) [2 1 4 4]) "
  (if (> length (count ver))
    (concat ver (repeat (- length (count ver)) 0))
    ver
  ))

; TODO: review mje 2016.05.17: move to config-commons version
(defn ver_comp [v1 v2]
  "Compares two version vectors and return the difference of the first postion they differ.
   Returns nil if they are the same version."
  (let [len (max (count v1) (count v2))]
   (first (drop-while #(= % 0) (mapv - (ver_fill v1 len) (ver_fill v2 len))))
  ))

; TODO: review mje 2016.05.17: move to config-commons version
(defn ver_less [v1 v2]
  "Returns v1 < v2"
  (let [comp (ver_comp v1 v2)]
    (if (nil? comp) false (< comp 0))))
; TODO: review mje 2016.05.17: move to config-commons version
(defn ver_lesseq [v1 v2]
  "Returns v1 <= v2"
  (let [comp (ver_comp v1 v2)]
    (if (nil? comp) true (< comp 0))))
; TODO: review mje 2016.05.17: move to config-commons version
(defn ver_eq [v1 v2]
  "Returns v1 == v2"
  (let [comp (ver_comp v1 v2)]
    (nil? comp)))

; TODO: review mje 2016.05.17: move to versions
(defn install-marker-path [dda-crate]
  "Gets the path of state marker."
  (str "/home/pallet/state/" (name (:facility dda-crate))))

; TODO: review mje 2016.05.17: move to config-commons versions
(defn node-write-state [dda-crate]
  "Creates an actions that writes a state file to the node."
   (actions/remote-file
      (install-marker-path dda-crate) 
      :overwrite-changes true
      :literal true
      :content (ver_str (:version dda-crate))))

; TODO: review mje 2016.05.17: move to config-commons version
(defn node-get-nv-state [dda-crate]
  "Read the node version as node-value from settings after it was set by 
   node-read-state."
  (-> dda-crate :facility crate/get-settings :node-version ver_fromstr))

; TODO: review mje 2016.05.17: move to config-commons version
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