(ns packager.distributor
  (:require [schema.core :as s]
            [packager.box :as b]
            [packager.container :as c]))

(def Distribution {b/Box [b/Box]})

(s/defn collect :- [[Distribution]]
  [{shelves :shelves} :- c/Container]
  (for [{boxes :boxes :as shelf} shelves]
    (for [box boxes]
      {box [[0.0 0.0]]})))

(s/defn distribute :- Distribution
  [c :- c/Container]
  (let [locations (collect c)]
    (or (->> locations
             flatten
             (apply merge-with vector))
        {})))
