
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.download.events
    (:require [candy.api                               :refer [return]]
              [engines.engine-handler.body.subs        :as body.subs]
              [engines.engine-handler.core.subs        :as core.subs]
              [engines.engine-handler.download.helpers :as download.helpers]
              [engines.engine-handler.download.subs    :as download.subs]
              [map.api                                 :as map]
              [re-frame.api                            :refer [r]]
              [vector.api                              :as vector]))



;; -- Data-receiving events ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-received
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (map)
  [db [_ engine-id]]
  (assoc-in db [:engines :engine-handler/meta-items engine-id :data-received?] true))



;; -- Single item events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-received-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ handler-id server-response]]
  ; XXX#3907 (source-code/cljs/engines/engine_handler/README.md)
  (let [received-item     (r download.subs/get-resolver-answer db handler-id :get-item server-response)
        current-item-path (r core.subs/get-current-item-path   db handler-id)]
       (assoc-in db current-item-path (map/remove-namespace received-item))))



;; -- Multiple items events ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-received-item-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ engine-id server-response]]
  ; The all-item-count means how many documents are in the collection what
  ; fit the search conditions.
  ;
  ; BUG#7009
  ; If the received item count is 0, but the all downloaded item's count is less
  ; than the received all-item-count value, that means something was wrong and
  ; somehow there are documents left on the server which fit the conditions
  ; but didn't arrive.
  ; To determine these kind of errors the received-item-count value has to be stored!
  (let [resolver-answer     (r download.subs/get-resolver-answer db engine-id :get-items server-response)
        all-item-count      (:all-item-count resolver-answer)
        received-items      (:items          resolver-answer)
        received-item-count (count received-items)]
       (-> db (assoc-in [:engines :engine-handler/meta-items engine-id :all-item-count]      all-item-count)
              (assoc-in [:engines :engine-handler/meta-items engine-id :received-item-count] received-item-count))))

(defn store-received-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ engine-id server-response]]
  ; XXX#3907 (source-code/cljs/engines/engine_handler/README.md)
  ;
  ; Iterates over the received items, if an item marked as changed, the function
  ; will skip it, and only stores the ones which aren't marked.
  (let [resolver-answer (r download.subs/get-resolver-answer db engine-id :get-items server-response)
        items-path      (r body.subs/get-body-prop           db engine-id :items-path)
        received-items  (:items resolver-answer)]
       (let [items-path (r body.subs/get-body-prop db engine-id :items-path)]
            (letfn [(f [db received-item] (let [{:keys [id] :as received-item} (map/remove-namespace received-item)]
                                               (if (get-in   db (conj items-path id :meta-items :changed?))
                                                   (return   db)
                                                   (assoc-in db (conj items-path id) received-item))))]
                   (reduce f db received-items)))))



;; -- Item order events -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-item-order!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ engine-id server-response]]
  (let [resolver-answer (r download.subs/get-resolver-answer db engine-id :get-items server-response)
        received-items  (:items resolver-answer)]
       (assoc-in db [:engines :engine-handler/meta-items engine-id :item-order]
                    (vector/->items received-items download.helpers/received-item->item-id))))

(defn update-item-order!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ engine-id server-response]]
  (let [resolver-answer (r download.subs/get-resolver-answer db engine-id :get-items server-response)
        received-items  (:items resolver-answer)]
       (update-in db [:engines :engine-handler/meta-items engine-id :item-order] vector/concat-items
                     (vector/->items received-items download.helpers/received-item->item-id))))
