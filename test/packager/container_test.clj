(ns packager.container-test
  (:require [clojure.test :refer :all]
            [packager.container :as c]
            [schema.test :as st]))

(use-fixtures :once st/validate-schemas)

(deftest remaining
  (testing "it calculates the space remaining in a container"
    (let [c {:shelves [{:boxes [[10.0 10.0]]
                        :dimensions [20.0 10.0]}]
             :dimensions [20.0 20.0]}]
      (is (= [20.0 10.0] (c/remaining c))))))


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
      (is (= expected (c/add (c/add c box) box)))))
  
  (testing "two boxes that don't fit on the same shelf get a shelf each"
    (let [c {:shelves []
             :dimensions [20.0 40.0]}
          box [19.0 19.0]
          expected {:shelves [{:boxes [box] 
                               :dimensions [20.0 19.0]}
                              {:boxes [box] 
                               :dimensions [20.0 19.0]}]
                    :dimensions [20.0 40.0]}]
      (is (= expected (c/add (c/add c box) box)))))

  (testing "that boxes that are two big are ignored"
    (let [c {:shelves []
             :dimensions [10.0 10.0]}
          box [9.0 9.0]
          big-box [100.0 100.0]
          small-box [1.0 1.0]
          
          expected {:shelves [{:boxes [box, small-box] 
                               :dimensions [10.0 9.0]}]
                    :dimensions [10.0 10.0]
                    :rejected   [big-box]}]

      (is (= expected (-> c
                          (c/add box)
                          (c/add big-box)
                          (c/add small-box)))))))
