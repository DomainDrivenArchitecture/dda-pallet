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

(ns org.domaindrivenarchitecture.pallet.ddacrate.internals-test
  (:require
    [clojure.test :refer :all]
    [schema.core :as s]
    [org.domaindrivenarchitecture.pallet.ddacrate.internals :as sut]))

(deftest version-conversions
  (testing "ver_fromstr: string to vector conversion" 
    (is (= nil (sut/ver_fromstr nil))) 
    (is (= [42] (sut/ver_fromstr "42")))
    (is (= [1 2] (sut/ver_fromstr "1.2")))
    (is (= [1 2] (sut/ver_fromstr "1.2...")))
    (is (= [1 2 3] (sut/ver_fromstr "1.2.3")))
    (is (= [1 0 3] (sut/ver_fromstr "1.ignorethis.3")))
    (is (= [1000 2000 3000] (sut/ver_fromstr "1000.2000.3000")))
    )
  (testing "ver_str: vector to string conversion" 
    (is (= "" (sut/ver_str nil)))
    (is (= "42" (sut/ver_str [42])))
    (is (= "1.2" (sut/ver_str [1 2])))
    (is (= "1.2.3" (sut/ver_str [1 2 3])))
    (is (= "1000.2000.3000" (sut/ver_str [1000 2000 3000])))
    )  
  )

(deftest version-compare
  (testing "ver_less"
    (is (= true (sut/ver_less [1 2 3] [1 2 9]))  "differ at 3. place")
    (is (= true (sut/ver_less [1 2 3] [1 9 0]))  "differ at 2. place")
    (is (= true (sut/ver_less [1 2 3] [9 0 0]))  "differ at 1. place")
    (is (= true (sut/ver_less [1 2] [1 2 3]))    "different length")
    (is (= false (sut/ver_less [1 2 3] [1 2]))   "different length swaped")
    (is (= false (sut/ver_less [1 2 3] [1 2 3])) "equal")
    )
  (testing "ver_lesseq"
    (is (= true (sut/ver_lesseq [1 2 3] [1 2 9])) "differ at 3. place")
    (is (= true (sut/ver_lesseq [1 2 3] [1 9 0])) "differ at 2. place")
    (is (= true (sut/ver_lesseq [1 2 3] [9 0 0])) "differ at 1. place")
    (is (= true (sut/ver_lesseq [1 2] [1 2 3]))   "different length")
    (is (= false (sut/ver_lesseq [1 2 3] [1 2]))  "different length swaped")
    (is (= true (sut/ver_lesseq [1 2 3] [1 2 3])) "equal")
    )
  (testing "ver_eq"         
    (is (= false (sut/ver_eq [1 2 3] [1 2 9])) "differ at 3. place")
    (is (= true (sut/ver_eq [1 2 3] [1 2 3]))  "equal")
    (is (= true (sut/ver_eq [1 2 0 0] [1 2]))  "equal with fill")
    )
  )

(deftest version-schema
  (testing "schema validation of VersionSchema"
    (is (= [1 2 3] (s/validate sut/VersionSchema [1 2 3])))
    (is (thrown? clojure.lang.ExceptionInfo (s/validate sut/VersionSchema [1 2 3.1])))
    (is (thrown? clojure.lang.ExceptionInfo (s/validate sut/VersionSchema [1 2 "3"])))
    )
  )

(deftest install-marker
  (testing "path of install-marker"
    (is (= "/home/pallet/state/mycrate" 
           (sut/install-marker-path {:facility :mycrate})))
    )
  )
