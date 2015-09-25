(ns packager.distributor
  (:require [schema.core :as s]
            [packager.box :as b]
            [packager.container :as c]))

(def Distribution {b/Box [b/Box]})

(s/defn process-boxes :- Distribution
  [boxes :- [b/Box]
   h :- Number
   d :- Distribution]
  (if (seq boxes)
    (loop [[[bw _ :as b] & bs] boxes
           w 0.0
           d d]
      (let [d' (if (contains? d b)
                 (merge-with conj d {b [w h]})
                 (assoc d b [[w h]]))]
        (if (seq? bs)
          (recur bs (+ w bw) d')
          d')))
    d))

(s/defn collect :- Distribution
  [{shelves :shelves} :- c/Container]
  (loop [[{[_ sh] :dimensions
           boxes :boxes} & ss] shelves
         h 0.0
         d {}]
    (let [pb (process-boxes boxes h d)]
      (if (seq ss)
        (recur ss (+ h sh) pb)
        pb))))

(s/defn distribute :- Distribution
  [c :- c/Container]
  (collect c))
