
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.core.events
    (:require [engines.engine-handler.core.events :as core.events]
              [engines.file-editor.body.subs      :as body.subs]
              [mid-fruits.map                     :as map :refer [dissoc-in]]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-mode!          core.events/set-mode!)
(def set-engine-error!  core.events/set-engine-error!)



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
       (-> db (dissoc-in [:engines :engine-handler/meta-items   editor-id :data-received?])
              (dissoc-in [:engines :engine-handler/backup-items editor-id])
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
  ; XXX#5067 (engines.item-editor.core.events)
  (let [default-content (r body.subs/get-body-prop db editor-id :default-content)
        content-path    (r body.subs/get-body-prop db editor-id :content-path)]
       (update-in db content-path map/reversed-merge default-content)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :file-editor/set-engine-error! set-engine-error!)
