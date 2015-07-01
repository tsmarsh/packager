(ns packager.container
  (:require [packager.box :as b]
            [packager.shelf :as sh]
            [schema.core :as s]))

(def Container {:shelves [sh/Shelf]
                :dimensions b/Box})

(s/defn remaining :- b/Box
  [{shelves :shelves
    [cw ch] :dimensions :as container} :- Container]
  (let [used_height (apply + (map (juxt :dimensions second) shelves))]
    [cw (- ch used_height)]))


(s/defn add :- Container
  [{shelves :shelves :as c} :- Container
   [w h :as box] :- b/Box]
  (let [bs (sh/best-shelf shelves box)]
    (if bs
      (let [{boxen :boxes :as shelf} (nth shelves bs)
            shelf' (assoc shelf :boxes (conj boxen box))]
        (assoc c :shelves (assoc shelves bs shelf')))
      (assoc c :shelves (conj shelves {:boxes [box]
                                       :dimensions [(first (:dimensions c))
                                                    h]})))))
