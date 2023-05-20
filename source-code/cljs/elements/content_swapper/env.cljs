
(ns elements.content-swapper.env
    (:require [elements.content-swapper.state :as content-swapper.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-first-page?
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ;
  ; @return (boolean)
  [swapper-id]
  (let [page-cursor (get-in @content-swapper.state/SWAPPERS [swapper-id :page-cursor])]
       (= 0 page-cursor)))

(defn on-last-page?
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ;
  ; @return (boolean)
  [swapper-id]
  (let [page-cursor  (get-in @content-swapper.state/SWAPPERS [swapper-id :page-cursor])
        page-history (get-in @content-swapper.state/SWAPPERS [swapper-id :page-history])]
       (= page-cursor (-> page-history count dec))))
