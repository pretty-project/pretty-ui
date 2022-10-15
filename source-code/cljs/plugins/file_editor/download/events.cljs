
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.download.events
    (:require [mid-fruits.map                         :as map]
              [plugins.file-editor.backup.events      :as backup.events]
              [plugins.file-editor.body.subs          :as body.subs]
              [plugins.file-editor.core.events        :as core.events]
              [plugins.file-editor.download.subs      :as download.subs]
              [plugins.plugin-handler.download.events :as download.events]
              [re-frame.api                           :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.download.events
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
  ; XXX#3005 (plugins.item-viewer.download.events)
  (r core.events/reset-downloads! db editor-id))

(defn store-received-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  ; XXX#3907
  ; A többi pluginnal megegyezően az file-editor plugin is névtér nélkül
  ; tárolja a letöltött tartalmat.
  ;
  ; XXX#3400
  ; A tartalomról letöltéskor másolat készül, hogy a "Visszaállítás (revert)" gomb
  ; használatával a tartalom letöltéskori állapota visszaállítható legyen.
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
