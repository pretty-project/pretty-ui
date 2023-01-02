
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.download.events
    (:require [engines.engine-handler.download.events :as download.events]
              [engines.file-editor.backup.events      :as backup.events]
              [engines.file-editor.body.subs          :as body.subs]
              [engines.file-editor.core.events        :as core.events]
              [engines.file-editor.download.subs      :as download.subs]
              [map.api                                :as map]
              [re-frame.api                           :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.download.events
(def data-received download.events/data-received)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; XXX#3005 (source-code/cljs/engines/item_handler/download/events.cljs)
  ;
  ; Az eredeti XXX#3005 törlésre került az item-handler engine-ben!
  ; Szükséges felülvizsgálni a szükségességét itt is!
  ; Ez az eredeti leírás:
  ; Ha az [:item-handler/request-item! ...] esemény megtörténésekor az item-handler
  ; engine már használatban van, akkor az adatok letöltése előtt szükséges visszaléptetni
  ; az engine-t {:data-received? false} állapotba, hogy a letöltés idejére újra
  ; megjelenjen a letöltésjelző!
  ; Pl.: Ha a felhasználó egy elem megtekintése közben duplikálja az elemet, majd
  ;      a megjelenő értesítésen a "Másolat megtekintése" gombra kattint, akkor
  ;      az item-handler engine letölti a másolat-elemet és a letöltés idejére
  ;      szükséges újra megjeleníteni a letöltésjelzőt!
  ; (r core.events/reset-downloads! db editor-id))
  db)

(defn store-received-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  ; XXX#3907 (source-code/cljs/engines/engine_handler/README.md)
  (let [received-content (r download.subs/get-resolver-answer db editor-id :get-content server-response)
        content-path     (r body.subs/get-body-prop           db editor-id :content-path)]
       (assoc-in db content-path (map/remove-namespace received-content))))

(defn receive-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  (as-> db % (r data-received                         % editor-id)
             (r store-received-content!               % editor-id server-response)
             (r core.events/use-default-content!      % editor-id)
             (r backup.events/backup-current-content! % editor-id)))
