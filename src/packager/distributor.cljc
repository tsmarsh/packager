(ns packager.distributor
  (:require [schema.core :as s]
            [packager.box :as b]
            [packager.container :as c]))

(def Distribution {b/Box [b/Box]})

(s/defn distribute :- Distribution
  [container :- c/Container]
  {})
