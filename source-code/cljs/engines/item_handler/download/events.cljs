
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.download.events
    (:require [engines.engine-handler.download.events :as download.events]
              [engines.item-handler.backup.events     :as backup.events]
              [engines.item-handler.body.subs         :as body.subs]
              [engines.item-handler.core.events       :as core.events]
              [map.api                                :as map]
              [re-frame.api                           :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.download.events
(def data-received        download.events/data-received)
(def store-received-item! download.events/store-received-item!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-received-suggestions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ handler-id server-response]]
  (let [suggestions-path     (r body.subs/get-body-prop db handler-id :suggestions-path)
        received-suggestions (-> server-response :item-handler/get-item-suggestions map/remove-namespace)]
       (assoc-in db suggestions-path received-suggestions)))

(defn receive-suggestions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ handler-id server-response]]
  (r store-received-suggestions! db handler-id server-response))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ handler-id server-response]]
  (as-> db % (r data-received                      % handler-id)
             (r store-received-item!               % handler-id server-response)
             (r store-received-suggestions!        % handler-id server-response)
             (r core.events/use-initial-item!      % handler-id)
             (r core.events/use-default-item!      % handler-id)
             (r backup.events/backup-current-item! % handler-id)))
