(ns packager.core
  (:require [schema.core :as s]))


(def Box [(s/one Number "width")
             (s/one Number "height")])

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
    [sw sh] :dimensions :as shelf} :- Shelf]
  (let [used_width (apply + (map first boxes))]
    [(- sw used_width) sh]))

(s/defn best-shelf-pred
  [[_ h :as box] :- Box
   index :- s/Int
   shelf :- Shelf]
  (when (and (>= (second (:dimensions shelf)) h)
             (fit (remaining shelf) box))
    index))
 
(s/defn best-shelf :- (s/maybe  s/Int)
  [shelves :- [Shelf]
   [w h :as box] :- Box]
  (first (keep-indexed (partial best-shelf-pred box) shelves)))

(s/defn add :- Container
  [{shelves :shelves :as c} :- Container
   [w h :as box] :- Box]
  (let [bs (best-shelf shelves box)]
    (if bs
      (let [{boxen :boxes :as shelf} (nth shelves bs)
            shelf' (assoc shelf :boxes (conj boxen box))]
        (assoc c :shelves (assoc shelves bs shelf')))
      (assoc c :shelves (conj shelves {:boxes [box]
                                       :dimensions [(first (:dimensions c))
                                                    h]})))))
