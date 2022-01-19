
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.07
; Description:
; Version: v0.6.4
; Compatibility: x4.5.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.dialogs
    (:require [mid-fruits.candy   :refer [param return]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
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
  (let [dialog-id  (engine/dialog-id extension-id item-namespace :items-deleted)
        undo-event [:item-lister/undo-delete-items! extension-id item-namespace item-ids]]
       [:<> [elements/horizontal-separator {:size :s}]
            [elements/label {:content :n-items-deleted :layout :fit :replacements [(count item-ids)]}]
            [elements/horizontal-separator {:size :s}]
            [elements/button {:label :recover! :preset :primary-button :layout :fit
                              :on-click {:dispatch-n [undo-event [:ui/pop-bubble! dialog-id]]}}]
            [elements/horizontal-separator {:size :s}]]))

(defn items-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (component)
  [extension-id item-namespace item-ids]
  (let [dialog-id  (engine/dialog-id extension-id item-namespace :items-duplicated)
        undo-event [:item-lister/undo-duplicate-items! extension-id item-namespace item-ids]]
       [:<> [elements/horizontal-separator {:size :s}]
            [elements/label {:content :n-items-duplicated :layout :fit :replacements [(count item-ids)]}]
            [elements/horizontal-separator {:size :s}]
            [elements/button {:label :recover! :preset :primary-button :layout :fit
                              :on-click {:dispatch-n [undo-event [:ui/pop-bubble! dialog-id]]}}]
            [elements/horizontal-separator {:size :s}]]))



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
  ; @param (strings in vector) item-ids
  (fn [_ [_ extension-id item-namespace item-ids]]
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :items-duplicated)
                        {:body [items-duplicated-dialog-body extension-id item-namespace item-ids]}]))
