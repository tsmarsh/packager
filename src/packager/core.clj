(ns packager.core
  (:require [schema.core :as s]))


(def Box [(s/one Double "width")
             (s/one Double "height")])

(def Shelf {:boxes [Box]
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
  [{boxes :boxes
    dims :dimensions} shelf :- Shelf]
  (let [used_width (apply + (map first boxes))]
    [(- (first dims) used_width) (second dims)]))

(s/defn best-shelf-pred
  [[_ h :as box] :- Box
   index :- s/Int
   [_ shelf_height :as shelf] :- Shelf]
  (when (and (> (second (:dimensions shelf_height)) h)
             (fit (remaining shelf) box)) index))
 
(s/defn best-shelf :- (s/maybe  s/Int)
  [shelves :- [Shelf]
   [w h :as box] :- Box]
  (first (keep-indexed (partial best-shelf-pred box) shelves)))

(s/defn add :- Container
  [{shelves :shelves :as c} :- Container
   box :- Box]
  (let [bs (best-shelf shelves box)]
    (if bs
      (let [shelf (nth shelves bs)
            shelf' (conj shelf box)]
        (assoc c :shelves (assoc shelves bs shelf')))
      (assoc c :shelves (conj shelves {:boxes [box]
                                       :dimensions [(first (:dimensions c))
                                                    (second box)]})))))
