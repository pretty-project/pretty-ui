
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.download.events
    (:require [engines.engine-handler.download.events :as download.events]
              [engines.item-handler.backup.events     :as backup.events]
              [engines.item-handler.body.subs         :as body.subs]
              [engines.item-handler.core.events       :as core.events]
              [engines.item-handler.core.subs         :as core.subs]
              [engines.item-handler.download.subs     :as download.subs]
              [map.api                                :as map]
              [re-frame.api                           :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.download.events
(def data-received download.events/data-received)



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
  (r core.events/reset-downloads! db handler-id))

(defn store-received-suggestions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ handler-id server-response]]
  ; XXX#3907
  (let [suggestions-path     (r body.subs/get-body-prop db handler-id :suggestions-path)
        received-suggestions (-> server-response :item-handler/get-item-suggestions map/remove-namespace)]
       (assoc-in db suggestions-path received-suggestions)))

(defn store-received-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ handler-id server-response]]
  ; XXX#3907
  ; A többi engine-nel megegyezően az item-handler engine is névtér nélkül
  ; tárolja a letöltött dokumentumot.
  ;
  ; XXX#3400
  ; Az elemről letöltéskor másolat készül, hogy a "Visszaállítás (revert)" gomb használatával
  ; az elem letöltéskori állapota visszaállítható legyen.
  (let [received-item (r download.subs/get-resolver-answer db handler-id :get-item server-response)
        item-path     (r body.subs/get-body-prop           db handler-id :item-path)]
       (assoc-in db item-path (map/remove-namespace received-item))))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ handler-id server-response]]
  (cond-> ; Set {:data-received? true} state ...
          (r data-received db handler-id)
          ; If handler downloading item ...
          (r core.subs/download-item? db handler-id)
          (as-> % (r store-received-item! % handler-id server-response))
          ; If handler downloading suggestions ...
          (r core.subs/download-suggestions? db handler-id)
          (as-> % (r store-received-suggestions! % handler-id server-response))
          :use-initial-item!    (as-> % (r core.events/use-initial-item!      % handler-id))
          :use-default-item!    (as-> % (r core.events/use-default-item!      % handler-id))
          :backup-current-item! (as-> % (r backup.events/backup-current-item! % handler-id))))
