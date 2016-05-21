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
    [clojure.tools.cli :as cli]
    [clojure.string :as string]
    [pallet.api :as api]
    [pallet.repl :as repl]
    [org.domaindrivenarchitecture.pallet.core.user-for-transport :as cm-user]
    [org.domaindrivenarchitecture.pallet.crate.config.node :as node-type]))
 
(def cli-options
  [["-h" "--help"]])

(defn usage [options-summary]
  (string/join
   \newline
   ["meissa-managed-ide installs and configures the whole ide to localhost."
    ""
    "Usage: program-name [options] action"
    ""
    "Options:"
    options-summary
    ""
    "Actions:"
    "  init      initialized localhost"
    "  install   installs software to localhost - is done only once."
    "  configure adjust configuration - is done everytime."
    ""
    "Please refer to the manual page for more information."]))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

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

(defn main
  "Main function takes a String as Argument to decide what function to call - needed when deploying standalone jar files."
  [node-id group-spec node-list config & args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary))
      (not= (count arguments) 1) (exit 1 (usage summary))
      errors (exit 1 (error-msg errors)))
    (case (first arguments)
      "init" 
      (execute-init-node 
        :id node-id 
        :group-spec group-spec
        :config config
        :provider node-list)
      "install" (execute-main group-spec node-list :phase '(:settings :install))
      "configure" (execute-main group-spec node-list :phase '(:settings :configure))
      (exit 1 (usage summary)))
    ))