
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.download.events
    (:require [engines.engine-handler.download.events :as download.events]
              [engines.item-viewer.body.subs          :as body.subs]
              [engines.item-viewer.core.events        :as core.events]
              [engines.item-viewer.download.subs      :as download.subs]
              [map.api                                :as map]
              [re-frame.api                           :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.download.events
(def data-received download.events/data-received)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  [db [_ viewer-id]]
  ; XXX#3005 (source-code/cljs/engines/item_handler/download/events.cljs)
  (r core.events/reset-downloads! db viewer-id))

(defn store-received-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ viewer-id server-response]]
  ; XXX#3907
  ; A többi engine-nel megegyezően az item-viewer engine is névtér nélkül
  ; tárolja a letöltött dokumentumot.
  (let [received-item (r download.subs/get-resolver-answer db viewer-id :get-item server-response)
        item-path     (r body.subs/get-body-prop           db viewer-id :item-path)]
       (assoc-in db item-path (map/remove-namespace received-item))))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ viewer-id server-response]]
  (as-> db % (r data-received        % viewer-id)
             (r store-received-item! % viewer-id server-response)))
