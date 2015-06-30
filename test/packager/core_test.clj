(ns packager.core-test
  (:require [clojure.test :refer :all]
            [packager.core :as c]
            [schema.test :as st]))

(use-fixtures :once st/validate-schemas)

(deftest fitting
  (testing "does fit"
    (let [machine [20.0 20.0]]
      (is (c/fit machine [20.0 20.0]))))
  
  (testing "doesn't fit"
    (let [machine [19.0 20.0]]
      (is (not (c/fit machine [20.0 20.0]))))))

(deftest remaining
  (testing "calculates the space remaining on a shelf"
    (is (= [10.0 10.0]
           (c/remaining {:boxes [[10.0 1.0]] :dimensions [20.0 10.0]})))))

(deftest shelf-selection
  (testing "returns nil if no suitable shelf can be found"
    (is (nil? (c/best-shelf [] [20.0 20.0]))))
  
  (testing "returns the index of the shelf that is the best fit"
    (is (= 0 (c/best-shelf [{:boxes  [] :dimensions [21.0 21.0]}] [20.0 20.0]))))
  
  (testing "returns nil if the shelf that is the best fit is full"
    (is (nil? (c/best-shelf [{:boxes  [[20.0 20.0]] :dimensions [21.0 21.0]}] [20.0 20.0]))))

  (testing "returns the index of the next shelf that is the best fit"
    (is (= 1 (c/best-shelf [{:boxes  [[20.0 20.0]]
                             :dimensions [21.0 21.0]}
                            {:boxes []
                             :dimensions [21.0 21.0]}] [20.0 20.0]))))

  (testing "returns the index of the shelf that is the best fit even if its not empty"
    (is (= 0 (c/best-shelf [{:boxes  [[20.0 20.0]]
                             :dimensions [41.0 20.0]}] [20.0 20.0])))))


(deftest add
  (testing "an empty container gets a shelf on which to place a box"
    (let [c {:shelves []
             :dimensions [20.0 20.0]}
          box [19.0 19.0]
          expected {:shelves [{:boxes [box] 
                               :dimensions [20.0 19.0]}]
                    :dimensions [20.0 20.0]}]
      (is (= expected (c/add c box)))))
  (testing "two boxes that fit on the same shelf end up on the same shelf"
    (let [c {:shelves []
             :dimensions [40.0 20.0]}
          box [19.0 19.0]
          expected {:shelves [{:boxes [box box] 
                               :dimensions [40.0 19.0]}]
                    :dimensions [40.0 20.0]}]
      (is (= expected (c/add (c/add c box) box))))))
