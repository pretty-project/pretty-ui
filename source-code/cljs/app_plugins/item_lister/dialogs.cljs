
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.07
; Description:
; Version: v0.5.6
; Compatibility: x4.4.9



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

(defn- items-marked-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ;  {:marked-message (metamorphic-content)}
  ; @param (strings in vector) item-ids
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [marked-message] :as mark-props} item-ids]
  (let [dialog-id  (engine/dialog-id extension-id item-namespace :items-marked)
        undo-event [:item-lister/undo-mark-items! extension-id item-namespace mark-props item-ids]]
       [:<> [elements/horizontal-separator {:size :s}]
            [elements/label {:content marked-message :layout :fit :replacements [(count item-ids)]}]
            [elements/horizontal-separator {:size :s}]
            [elements/button {:label :recover! :preset :primary-button :layout :fit
                              :on-click {:dispatch-n [undo-event [:ui/pop-bubble! dialog-id]]}}]
            [elements/horizontal-separator {:size :s}]]))

(defn- items-deleted-dialog-body
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



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/render-items-marked-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) mark-props
  ; @param (strings in vector) item-ids
  (fn [_ [_ extension-id item-namespace mark-props item-ids]]
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :items-marked)
                        {:body {:content [items-marked-dialog-body extension-id item-namespace mark-props item-ids]}}]))

(a/reg-event-fx
  :item-lister/render-items-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  (fn [_ [_ extension-id item-namespace item-ids]]
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :items-deleted)
                        {:body {:content [items-deleted-dialog-body extension-id item-namespace item-ids]}
                         :destructor [:item-lister/clean-backup-items! extension-id item-namespace item-ids]}]))
