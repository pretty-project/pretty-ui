
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
              [x.app-core.api                         :refer [r]]))



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
  ; XXX#3005
  (r core.events/reset-downloads! db editor-id))

(defn store-downloaded-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  ; - XXX#3907
  ;   Az item-lister pluginnal megegyezően az file-editor plugin is névtér nélkül
  ;   tárolja a letöltött tartalmat.
  ;
  ; - XXX#3400
  ;   A tartalomról letöltéskor másolat készül, hogy a "Visszaállítás (revert)" gomb
  ;   használatával a tartalom letöltéskori állapota visszaállítható legyen.
  (let [resolver-id  (r download.subs/get-resolver-id db editor-id :get-content)
        content-path (r body.subs/get-body-prop       db editor-id :content-path)
        content      (-> server-response resolver-id map/remove-namespace)]
       (as-> db % (assoc-in % content-path content)
                  (r backup.events/backup-current-content! % editor-id))))

(defn receive-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  (as-> db % (r data-received             % editor-id)
             (r store-downloaded-content! % editor-id server-response)))
