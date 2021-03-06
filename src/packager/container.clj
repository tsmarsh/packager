(ns packager.container
  (:require [packager.box :as b]
            [packager.shelf :as sh]
            [schema.core :as s]))

(def Container {:shelves [sh/Shelf]
                :dimensions b/Box
                (s/optional-key :rejected) [b/Box]})

(s/defn remaining :- b/Box
  [{shelves :shelves
    [cw ch] :dimensions :as container} :- Container]
  (let [used_height (apply + (map (comp second :dimensions) shelves))
        remaining-height (- ch used_height)
        remaining-width (if (= 0.0 remaining-height) 0.0 cw)]
    [remaining-width remaining-height]))


(s/defn add :- Container
  [{shelves :shelves :as c} :- Container
   [w h :as box] :- b/Box]
  (let [bs (sh/best-shelf shelves box)]
    (if bs
      (let [{boxen :boxes :as shelf} (nth shelves bs)
            shelf' (assoc shelf :boxes (conj boxen box))]
        (assoc c :shelves (assoc shelves bs shelf')))
      (if (b/fit (remaining c) box) 
        (assoc c :shelves (conj shelves {:boxes [box]
                                         :dimensions [(first (:dimensions c))
                                                      h]}))
        (assoc c :rejected (conj (:rejected c) box))))))
