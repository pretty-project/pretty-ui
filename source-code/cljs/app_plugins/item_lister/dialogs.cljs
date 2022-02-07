
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.07
; Description:
; Version: v0.7.4
; Compatibility: x4.5.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.dialogs
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-ui.api     :as ui]
              [app-plugins.item-lister.engine :as engine]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-deleted-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (component)
  [extension-id item-namespace item-ids]
  (let [undo-event [:item-lister/undo-delete-items! extension-id item-namespace item-ids]]
       [ui/state-changed-bubble-body (engine/dialog-id extension-id item-namespace :items-deleted)
                                     {:label {:content :n-items-deleted :replacements [(count item-ids)]}
                                      :primary-button {:on-click undo-event :label :recover!}}]))

(defn items-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) copy-ids
  ;
  ; @return (component)
  [extension-id item-namespace copy-ids]
  (let [undo-event [:item-lister/undo-duplicate-items! extension-id item-namespace copy-ids]]
       [ui/state-changed-bubble-body (engine/dialog-id extension-id item-namespace :items-duplicated)
                                     {:label {:content :n-items-duplicated :replacements [(count copy-ids)]}
                                      :primary-button {:on-click undo-event :label :undo!}}]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/render-items-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  (fn [_ [_ extension-id item-namespace item-ids]]
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :items-deleted)
                        {:body       [items-deleted-dialog-body        extension-id item-namespace item-ids]
                         :destructor [:item-lister/clean-backup-items! extension-id item-namespace item-ids]}]))

(a/reg-event-fx
  :item-lister/render-items-duplicated-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) copy-ids
  (fn [_ [_ extension-id item-namespace copy-ids]]
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :items-duplicated)
                        {:body [items-duplicated-dialog-body extension-id item-namespace copy-ids]}]))
