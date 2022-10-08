
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.download.events
    (:require [mid-fruits.map                         :as map]
              [plugins.item-viewer.body.subs          :as body.subs]
              [plugins.item-viewer.core.events        :as core.events]
              [plugins.item-viewer.download.subs      :as download.subs]
              [plugins.plugin-handler.download.events :as download.events]
              [re-frame.api                           :as r :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.download.events
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
  ; XXX#3005 (plugins.item-viewer.download.events)
  ; Ha az [:item-viewer/request-item! ...] esemény megtörténésekor az item-viewer plugin
  ; már használatban van, akkor az adatok letöltése előtt szükséges visszaléptetni a plugint
  ; {:data-received? false} állapotba, hogy a letöltés idejére újra megjelenjen a letöltésjelző.
  ; Pl.: Ha a felhasználó egy elem megtekintése közben duplikálja az elemet, majd a megjelenő
  ;      értesítésen a "Másolat megtekintése" gombra kattint, akkor az item-viewer plugin
  ;      letölti a másolat elemet, és a letöltés idejére szükséges megjeleníteni a letöltésjelzőt!
  (r core.events/reset-downloads! db viewer-id))

(defn store-downloaded-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ viewer-id server-response]]
  ; XXX#3907
  ; A többi pluginnal megegyezően az item-viewer plugin is névtér nélkül
  ; tárolja a letöltött dokumentumot.
  (let [resolver-id (r download.subs/get-resolver-id db viewer-id :get-item)
        item-path   (r body.subs/get-body-prop       db viewer-id :item-path)
        document    (-> server-response resolver-id map/remove-namespace)]
       (assoc-in db item-path document)))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ viewer-id server-response]]
  (as-> db % (r data-received          % viewer-id)
             (r store-downloaded-item! % viewer-id server-response)))
