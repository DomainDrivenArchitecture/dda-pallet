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

(ns org.domaindrivenarchitecture.cm.cli-helper
  (:require
    [clojure.tools.cli :as cli]
    [clojure.string :as string]
    [pallet.api :as api]
    [pallet.repl :as repl]))
 
(def cli-options
  [["-h" "--help"]])

(defn usage [options-summary]
  (string/join
   \newline
   ["dda-managed-vm installs and configures the vm to localhost."
    ""
    "Usage: program-name [options] action"
    ""
    "Options:"
    options-summary
    ""
    "Actions:"
    "  install   installs and configures software to localhost. This is intended to run only once."
    "  configure adjust configuration - this can be called often."
    "  test      runs all defined tests against localhost."
    ""
    "Please refer to the manual page for more information."]))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn main
  "Main function takes a String as Argument to decide what function to call - needed when deploying standalone jar files."
  [install-fn configure-fn test-fn & args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary))
      (not= (count arguments) 1) (exit 1 (usage summary))
      errors (exit 1 (error-msg errors)))
    (case (first arguments)
      "install" (apply install-fn)
      "configure" (apply configure-fn)
      "test" (apply test-fn)
      (exit 1 (usage summary)))
    ))