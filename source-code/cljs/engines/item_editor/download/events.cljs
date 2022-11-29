
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.download.events
    (:require [engines.engine-handler.download.events :as download.events]
              [engines.item-editor.backup.events      :as backup.events]
              [engines.item-editor.body.subs          :as body.subs]
              [engines.item-editor.core.events        :as core.events]
              [engines.item-editor.core.subs          :as core.subs]
              [engines.item-editor.download.subs      :as download.subs]
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
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; XXX#3005 (source-code/cljs/engines/item_handler/download/events.cljs)
  (r core.events/reset-downloads! db editor-id))

(defn store-received-suggestions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  ; XXX#3907
  (let [suggestions-path     (r body.subs/get-body-prop db editor-id :suggestions-path)
        received-suggestions (-> server-response :item-editor/get-item-suggestions map/remove-namespace)]
       (assoc-in db suggestions-path received-suggestions)))

(defn store-received-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  ; XXX#3907
  ; A többi engine-nel megegyezően az item-editor engine is névtér nélkül
  ; tárolja a letöltött dokumentumot.
  ;
  ; XXX#3400
  ; Az elemről letöltéskor másolat készül, hogy a "Visszaállítás (revert)" gomb használatával
  ; az elem letöltéskori állapota visszaállítható legyen.
  (let [received-item (r download.subs/get-resolver-answer db editor-id :get-item server-response)
        item-path     (r body.subs/get-body-prop           db editor-id :item-path)]
       (assoc-in db item-path (map/remove-namespace received-item))))

(defn receive-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  (cond-> ; Set {:data-received? true} state ...
          (r data-received db editor-id)
          ; If editor downloading item ...
          (r core.subs/download-item? db editor-id)
          (as-> % (r store-received-item! % editor-id server-response))
          ; If editor downloading suggestions ...
          (r core.subs/download-suggestions? db editor-id)
          (as-> % (r store-received-suggestions! % editor-id server-response))
          ; If editor in recovery-mode ...
          (r core.subs/get-meta-item db editor-id :recovery-mode?)
          (as-> % (r backup.events/recover-item! % editor-id))
          :use-initial-item!    (as-> % (r core.events/use-initial-item!      % editor-id))
          :use-default-item!    (as-> % (r core.events/use-default-item!      % editor-id))
          :backup-current-item! (as-> % (r backup.events/backup-current-item! % editor-id))))
