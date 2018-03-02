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
(ns dda.pallet.core.summary
  (:require
   [schema.core :as s]
   [dda.config.commons.styled-output :as styled]))

(defn run-results [session]
  (:runs (pallet.core.data-api/session-data session)))

(defn node-results [session]
  (filter some? (:action-results (second (run-results session)))))

(defn summarize-tests [test-results indent verbose]
  (doseq [test-result (clojure.string/split-lines test-results)]
    (println (str "    " test-result))))

(defn summarize-node-tests [action-results indent verbose]
  (doseq [action-result action-results]
    (let [{:keys [context summary-text out result]} action-result]
      (println (str "  " context " - ")
        (if (:test-passed result)
          (styled/styled summary-text :green)
          (styled/styled summary-text :red)))
      (when (> verbose 0)
        (summarize-tests out (+ 2 indent) verbose)))))

(defn tests-passed? [action-result]
  (get-in action-result [:result :test-passed]))

(defn run-passed?
  [run]
  (let [action-results (filter some? (:action-results run))]
    (every? tests-passed? action-results)))

(defn summarize-test-session
  [session & {:keys [verbose] :or {verbose 0}}]
  (let [runs (run-results session)]
    (doseq [run runs]
      (let [action-results (filter some? (:action-results run))]
        (println
          (str
            (get-in run [:node :primary-ip]) " - "
            (if (run-passed? run)
              (styled/styled "PASSED" :green)
              (styled/styled "FAILED" :red))))
        (summarize-node-tests action-results 2 verbose)))))

(defn session-passed?
  [session]
  (let [runs (run-results session)]
    (every? run-passed? runs)))
