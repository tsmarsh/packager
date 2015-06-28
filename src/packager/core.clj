(ns packager.core
  (:require [schema.core :as s]))

(def Vertex [(s/one Double "x")
             (s/one Double "y")
             (s/one Double "z")])

(s/defn fit :- s/Bool
  [container :- Vertex
   object :- Vertex]
  (= 0 (->> (map - container object)
            (filter (partial > 0))
            count)))
