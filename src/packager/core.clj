(ns packager.core
  (:require [schema.core :as s]))


(def Box [(s/one Double "x")
             (s/one Double "y")])

(def Shelf {:boxes [Box]
            :root Box
            :dimensions Box})

(def Container {:shelves [Shelf]
                :dimensions Box})

(s/defn fit :- s/Bool
  [container :- Box
   object :- Box]
  (= 0 (->> (map - container object)
            (filter (partial > 0))
            count)))

(s/defn remaining :- Box
  [Shelf])
