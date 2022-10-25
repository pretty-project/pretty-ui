
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.core.events
    (:require [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.item-viewer.body.subs      :as body.subs]
              [plugins.item-viewer.core.subs      :as core.subs]
              [plugins.engine-handler.core.events :as core.events]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.engine-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-item-id!       core.events/set-item-id!)
(def update-item-id!    core.events/update-item-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  [db [_ viewer-id]]
  (assoc-in db [:engines :engine-handler/meta-items viewer-id :error-mode?] true))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  [db [_ viewer-id]]
  (let [item-path (r body.subs/get-body-prop db viewer-id :item-path)]
       (-> db (dissoc-in [:engines :engine-handler/meta-items viewer-id :data-received?])
              (dissoc-in item-path))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-viewer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  [db [_ viewer-id]]
  (r update-item-id! db viewer-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-viewer/set-error-mode! set-error-mode!)
