(ns packager.box-test
  (:require [clojure.test :refer :all]
            [packager.box :as c]
            [schema.test :as st]))

(use-fixtures :once st/validate-schemas)

(deftest fitting
  (testing "does fit"
    (let [machine [20.0 20.0]]
      (is (c/fit machine [20.0 20.0]))))
  
  (testing "doesn't fit"
    (let [machine [19.0 20.0]]
      (is (not (c/fit machine [20.0 20.0]))))))


