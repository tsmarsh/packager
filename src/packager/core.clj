(ns packager.core
  (:require [schema.core :as s]))

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

(ns packager.shelf
  (:require [packager.box :as b]
            [schema.core :as s]))

(def Shelf {:boxes [b/Box]
            :dimensions b/Box})

(s/defn remaining :- b/Box
  [{boxes :boxes
    [sw sh] :dimensions :as shelf} :- Shelf]
  (let [used_width (apply + (map first boxes))]
    [(- sw used_width) sh]))

(s/defn best-shelf-pred
  [[_ h :as box] :- b/Box
   index :- s/Int
   shelf :- Shelf]
  (when (and (>= (second (:dimensions shelf)) h)
             (b/fit (remaining shelf) box))
    index))

(s/defn best-shelf :- (s/maybe  s/Int)
  [shelves :- [Shelf]
   [w h :as box] :- b/Box]
  (first (keep-indexed (partial best-shelf-pred box) shelves)))


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
