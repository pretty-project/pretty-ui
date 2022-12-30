
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

(defn request-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  ; XXX#3005
  ; Ha az [:item-handler/request-item! ...] esemény megtörténésekor az item-handler
  ; engine már használatban van, akkor az adatok letöltése előtt szükséges visszaléptetni
  ; az engine-t {:data-received? false} állapotba, hogy a letöltés idejére újra
  ; megjelenjen a letöltésjelző!
  ; Pl.: Ha a felhasználó egy elem megtekintése közben duplikálja az elemet, majd
  ;      a megjelenő értesítésen a "Másolat megtekintése" gombra kattint, akkor
  ;      az item-handler engine letölti a másolat-elemet és a letöltés idejére
  ;      szükséges újra megjeleníteni a letöltésjelzőt!
  ;(r core.events/reset-downloads! db handler-id))
  db)

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
