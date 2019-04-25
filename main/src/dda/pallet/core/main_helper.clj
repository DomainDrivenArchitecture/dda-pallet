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
(ns dda.pallet.core.main-helper
  (:require
   [clojure.string :as str]
   [schema.core :as s]
   [dda.config.commons.styled-output :as styled]))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn exit-test-passed []
  (exit 0 (styled/styled "ALL TESTS PASSED" :green)))

(defn exit-test-failed []
  (exit 2 (styled/styled "SOME TESTS FAILED" :red)))

(defn exit-default-success []
  (exit 0 (styled/styled "SUCESSFUL" :green)))

(defn exit-default-error []
  (exit 2 (styled/styled "ERROR" :red)))