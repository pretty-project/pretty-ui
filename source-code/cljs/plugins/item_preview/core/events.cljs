
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-preview.core.events
    (:require [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.item-preview.body.subs     :as body.subs]
              [plugins.plugin-handler.core.events :as core.events]
              [re-frame.api                       :as r :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-mode!          core.events/set-mode!)
(def set-item-id!       core.events/set-item-id!)
(def clear-item-id!     core.events/clear-item-id!)
(def update-item-id!    core.events/update-item-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (map)
  [db [_ preview-id]]
  (r set-mode! db preview-id :error-mode?))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (map)
  [db [_ preview-id]]
  (let [item-path (r body.subs/get-body-prop db preview-id :item-path)]
       (-> db (dissoc-in [:plugins :plugin-handler/meta-items preview-id :data-received?])
              (dissoc-in item-path))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-preview!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (map)
  [db [_ preview-id]]
  (r update-item-id! db preview-id))

(defn reload-preview!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (map)
  [db [_ preview-id]]
  (as-> db % (r clear-item-id!   % preview-id)
             (r update-item-id!  % preview-id)
             (r reset-downloads! % preview-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-preview/set-error-mode! set-error-mode!)
