
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.core.events
    (:require [mid-fruits.map                     :refer [dissoc-in]]
              [plugins.item-editor.body.subs      :as body.subs]
              [plugins.plugin-handler.core.events :as core.events]
              [re-frame.api                       :as r :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.events
(def set-meta-item!     core.events/set-meta-item!)
(def remove-meta-items! core.events/remove-meta-items!)
(def set-mode!          core.events/set-mode!)
(def set-item-id!       core.events/set-item-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ;
  ; @return (map)
  [db [_ picker-id]]
  (r set-mode! db picker-id :error-mode?))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-downloads!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ;
  ; @return (map)
  [db [_ picker-id]]
  (let [item-path (r body.subs/get-body-prop db picker-id :item-path)]
       (-> db (dissoc-in [:plugins :plugin-handler/meta-items picker-id :data-received?])
              (dissoc-in item-path))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :item-picker/set-error-mode! set-error-mode!)
