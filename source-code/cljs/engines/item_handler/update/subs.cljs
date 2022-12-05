
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.update.subs
    (:require [engines.engine-handler.update.subs :as update.subs]
              [engines.item-handler.body.subs     :as body.subs]
              [engines.item-handler.core.subs     :as core.subs]
              [engines.item-handler.transfer.subs :as transfer.subs]
              [keyword.api                        :as keyword]
              [map.api                            :as map]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.update.subs
(def get-mutation-name   update.subs/get-mutation-name)
(def get-mutation-answer update.subs/get-mutation-answer)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-on-saved-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @return (metamorphic-event)
  [db [_ handler-id server-response]]
  ; XXX#6077
  (if-let [on-saved (r body.subs/get-body-prop db handler-id :on-saved)]
          (let [new-item?  (r core.subs/new-item? db handler-id)
                saved-item (r get-mutation-answer db handler-id (if new-item? :add-item! :save-item!) server-response)]
               (r/metamorphic-event<-params on-saved (map/remove-namespace saved-item)))))



;; -- Save item subscriptions -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-saved-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @example
  ; (r get-saved-item-id :my-handler {my-handler/save-item! {:my-type/id "my-item"}})
  ; =>
  ; "my-item"
  ;
  ; @return (string)
  [db [_ handler-id server-response]]
  (let [new-item?      (r core.subs/new-item? db handler-id)
        saved-item     (r get-mutation-answer db handler-id (if new-item? :add-item! :save-item!) server-response)
        item-namespace (r transfer.subs/get-transfer-item db handler-id :item-namespace)
        id-key         (keyword/add-namespace item-namespace :id)]
       (id-key saved-item)))



;; -- Delete item subscriptions -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-deleted-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @example
  ; (r get-deleted-item-id :my-handler {my-handler/delete-item! "my-item"})
  ; =>
  ; "my-item"
  ;
  ; @return (string)
  [db [_ handler-id server-response]]
  (r update.subs/get-mutation-answer db handler-id :delete-item! server-response))



;; -- Duplicate item subscriptions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-duplicated-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @example
  ; (r get-duplicated-item-id :my-handler {my-handler/duplicate-item! {:my-type/id "my-item"}})
  ; =>
  ; "my-item"
  ;
  ; @return (string)
  [db [_ handler-id server-response]]
  (let [duplicated-item (r update.subs/get-mutation-answer db handler-id :duplicate-item! server-response)
        item-namespace  (r transfer.subs/get-transfer-item db handler-id :item-namespace)
        id-key         (keyword/add-namespace item-namespace :id)]
       (id-key duplicated-item)))
