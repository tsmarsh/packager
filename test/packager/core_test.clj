(ns packager.core-test
  (:require [clojure.test :refer :all]
            [packager.core :as c]
            [schema.test :as st]))

()
(deftest fitting
  (testing "does fit"
    (let [machine [20.0 20.0]]
      (is (c/fit machine [20.0 20.0]))))
  
  (testing "doesn't fit"
    (let [machine [19.0 20.0]]
      (is (not (c/fit machine [20.0 20.0]))))))

(deftest add
  (testing "an empty container gets a shelf on which to place a box"
    (let [c {:shelves []
             :dimensions [20.0 20.0]}
          box [19 19]
          expected {:selves [{:boxes [box]
                              :root [0.0 0.0]
                              :dimensions [19.0 20.0]}]}]
      (is (= expected  (c/add c box))))))
