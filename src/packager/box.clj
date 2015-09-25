(ns packager.box
  (:require [schema.core :as s]))

(def Box [(s/one Number "width")
          (s/one Number "height")])

(s/defn fit :- s/Bool
  [container :- Box
   object :- Box]
  (= 0 (->> (map - container object)
            (filter (partial > 0))
            count)))

