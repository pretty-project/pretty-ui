
(ns plugins.dnd-kit.utils
  (:require
    [mid-fruits.vector :as vector]))

(defn to-clj-map [hash-map]
  (js->clj hash-map :keywordize-keys true))

(defn vec-remove
  "Remove elem in coll by index."
  [coll pos]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(defn vec-add
  "Add elem in coll by index."
  [coll pos el]
  (concat (subvec coll 0 pos) [el] (subvec coll pos)))

(defn vec-move
  "Move elem in coll by index"
  [coll pos1 pos2]
  (let [el (nth coll pos1)]
    (if (= pos1 pos2)
      coll
      (into [] (vec-add (vec-remove coll pos1) pos2 el)))))

(defn reorder
  [list start-idx end-idx]
  (let [list   (vec list)
        result (vec-move list start-idx end-idx)]
    result))
