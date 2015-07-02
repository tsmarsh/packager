(ns packager.distributor-test
  (:require [packager.distributor :as d]
            [clojure.test :refer :all]
            [schema.test :as st]))

(use-fixtures :once st/validate-schemas)

(deftest distribution
  (testing "an empty container returns an empty distribution"
    (let [c {:shelves []
             :dimensions [0.0 0.0]}]
      (is (= {} (d/distribute c))))))


