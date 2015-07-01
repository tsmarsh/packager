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
