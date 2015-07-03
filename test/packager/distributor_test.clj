(ns packager.distributor-test
  (:require [packager.distributor :as d]
            [clojure.test :refer :all]
            [schema.test :as st]))

(use-fixtures :once st/validate-schemas)

(deftest distribution
  (testing "an empty container returns an empty distribution"
    (let [c {:shelves []
             :dimensions [0.0 0.0]}]
      (is (= {} (d/distribute c)))))

  (testing "a container with one box returns an identity transformation"
    (let [c {:shelves [{:boxes [[10.0 10.0]]
                        :dimensions [10.0 10.0]}]
             :dimensions [10.0 10.0]}]
      (is (= {[10.0 10.0] [[0.0 0.0]]} (d/distribute c))))))


