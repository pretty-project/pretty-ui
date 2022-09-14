
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.config-editor.download.events
    (:require [mid-fruits.map                         :as map]
              [plugins.config-editor.backup.events    :as backup.events]
              [plugins.config-editor.body.subs        :as body.subs]
              [plugins.config-editor.core.events      :as core.events]
              [plugins.config-editor.download.subs    :as download.subs]
              [plugins.plugin-handler.download.events :as download.events]
              [x.app-core.api                         :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.download.events
(def data-received download.events/data-received)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; XXX#3005
  (r core.events/reset-downloads! db editor-id))

(defn store-downloaded-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  ; - XXX#3907
  ;   Az item-lister pluginnal megegyezően az config-editor plugin is névtér nélkül tárolja a letöltött dokumentumot.
  ;
  ; - XXX#3400
  ;   Az elemről letöltéskor másolat készül, hogy a "Visszaállítás (revert)" gomb használatával
  ;   az elem letöltéskori állapota visszaállítható legyen.
  (let [resolver-id  (r download.subs/get-resolver-id db editor-id :get-config)
        config-path  (r body.subs/get-body-prop       db editor-id :config-path)
        document     (-> server-response resolver-id map/remove-namespace)]
       (as-> db % (assoc-in % config-path document)
                  (r backup.events/backup-current-config! % editor-id))))

(defn receive-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ editor-id server-response]]
  (as-> db % (r data-received            % editor-id)
             (r store-downloaded-config! % editor-id server-response)))
