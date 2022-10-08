
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.core.events
    (:require [mid-fruits.map                     :as map :refer [dissoc-in]]
              [plugins.file-editor.body.subs      :as body.subs]
              [plugins.plugin-handler.core.events :as core.events]
              [re-frame.api                       :as r :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-mode!          core.events/set-mode!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (r set-mode! db editor-id :error-mode?))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [content-path (r body.subs/get-body-prop db editor-id :content-path)]
       (-> db (dissoc-in [:plugins :plugin-handler/meta-items   editor-id :data-received?])
              (dissoc-in [:plugins :plugin-handler/backup-items editor-id])
              (dissoc-in content-path))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-default-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  ; XXX#5067 (plugins.item-editor.core.events)
  (let [default-content (r body.subs/get-body-prop db editor-id :default-content)
        content-path    (r body.subs/get-body-prop db editor-id :content-path)]
       (update-in db content-path map/reversed-merge default-content)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :file-editor/set-error-mode! set-error-mode!)
