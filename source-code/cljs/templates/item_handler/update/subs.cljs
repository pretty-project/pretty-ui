
(ns templates.item-handler.update.subs
    (:require [engines.item-lister.api :as item-lister]
              [loop.api                :refer [some-indexed]]
              [re-frame.api            :refer [r]]
              [vector.api              :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-next-item-id
  ; @param (keyword) lister-id
  ; @param (string) item-id
  ;
  ; @usage
  ; (r get-next-item-id db :my-lister "my-item")
  ;
  ; @return (string)
  [db [_ lister-id item-id]]
  ; If the given 'item-id' is ...
  ; ... the only ID in the 'item-order' vector, then returns nil.
  ; ... the last ID in the 'item-order' vector, then returns the previous item's ID.
  ; ... not the last ID in the 'item-order' vector, then returns the next item's ID.
  (let [item-order (r item-lister/get-item-order db lister-id)]
       (letfn [(f [dex %] (and (= % item-id)
                               (cond (vector/dex-last? item-order dex) (get item-order (dec dex))
                                     :not-last                         (get item-order (inc dex)))))]
              (and (vector/min?    item-order 2)
                   (some-indexed f item-order)))))
