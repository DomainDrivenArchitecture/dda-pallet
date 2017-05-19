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

(ns dda.cm.aws
  (:require
    [pallet.api :as api]
    [pallet.compute :as compute]
    [org.domaindrivenarchitecture.pallet.commons.encrypted-credentials :as crypto]))

(defn provider
  ([]
   (let
     [aws-decrypted-credentials (get-in (pallet.configure/pallet-config) [:services :aws])]
     (compute/instantiate-provider
      :pallet-ec2
      :identity (get-in aws-decrypted-credentials [:account])
      :credential (get-in aws-decrypted-credentials [:secret])
      :endpoint "eu-central-1"
      :subnet-ids ["subnet-f929df91"])))
  ([key-id key-passphrase]
   (let
     [aws-encrypted-credentials (get-in (pallet.configure/pallet-config) [:services :aws])
      aws-decrypted-credentials (crypto/decrypt
                                  (crypto/get-secret-key
                                    {:user-home (str (System/getenv "HOME") "/")
                                     :key-id key-id})
                                  aws-encrypted-credentials
                                  key-passphrase)]
     (compute/instantiate-provider
      :pallet-ec2
      :identity (get-in aws-decrypted-credentials [:account])
      :credential (get-in aws-decrypted-credentials [:secret])
      :endpoint "eu-central-1"
      :subnet-ids ["subnet-f929df91"]))))

(defn node-spec [key-name]
  (api/node-spec
    :location {:location-id "eu-central-1a"}
               ;:location-id "eu-west-1b"
               ;:location-id "us-east-1a"

    :image {:os-family :ubuntu
            ;eu-central-1 16-04 LTS hvm
            :image-id "ami-82cf0aed"
            ;eu-west1 16-04 LTS hvm :image-id "ami-07174474"
            ;us-east-1 16-04 LTS hvm :image-id "ami-45b69e52"
            :os-version "16.04"
            :key-name key-name
            :login-user "ubuntu"}
    :hardware {:hardware-id "t2.micro"}
    :provider {:pallet-ec2 {:network-interfaces [{:device-index 0
                                                  :groups ["sg-0606b16e"]
                                                  :subnet-id "subnet-f929df91"
                                                  :associate-public-ip-address true
                                                  :delete-on-termination true}]}}))
