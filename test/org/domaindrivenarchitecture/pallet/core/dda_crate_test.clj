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

(ns org.domaindrivenarchitecture.pallet.core.dda-crate-test
  (:require
    [clojure.test :refer :all]
    [schema.core :as s]
    [org.domaindrivenarchitecture.pallet.core.dda-crate :as sut]))

(deftest make-dda-crate-test
  (testing "crate instantiation"
    (is (= (sut/make-dda-crate 
             :facility :my-test-crate,
             :version [0 1 0],
             :config-default {},
             :config-schema {s/Any s/Any})
           (sut/make-dda-crate 
             :facility :my-test-crate
             :version [0 1 0])))
    ))

(deftest merge-config
  (testing "validation"
    (is (= {:a 1}
           (sut/merge-config 
             (sut/make-dda-crate 
               :facility :my-test-crate
               :version [0 1 0]) {:a 1})
             ))
    (is (= {:a 1}
           (sut/merge-config 
             (sut/make-dda-crate 
               :facility :my-test-crate
               :version [0 1 0]
	             :config-schema {:a s/Num}) {:a 1})
             ))
     (is (thrown? Exception 
           (sut/merge-config 
             (sut/make-dda-crate 
               :facility :my-test-crate
               :version [0 1 0]
	             :config-schema {:a s/Num}) {:b 1})
             ))
    )
  (testing "merge"
    (is (= {:a 1
            :b {:b1 1
                :b2 2}}
           (sut/merge-config 
             (sut/make-dda-crate 
               :facility :my-test-crate
               :version [0 1 0]
               :config-schema {:a s/Num
                               :b {:b1 s/Num
                                   :b2 s/Num}}
               :config-default {:a 1
                                :b {:b2 2}}) 
             {:b {:b1 1}})
             ))
    (is (= {:a 2}
           (sut/merge-config 
             (sut/make-dda-crate 
               :facility :my-test-crate
               :version [0 1 0]
	             :config-schema {:a s/Num}
               :config-default {:a 1}) {:a 2})
             ))
    ))

