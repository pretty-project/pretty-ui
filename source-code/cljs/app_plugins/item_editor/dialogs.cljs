
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.13
; Description:
; Version: v0.4.8
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.dialogs
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [app-plugins.item-editor.engine :as engine]
              [app-plugins.item-editor.subs   :as subs]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (strings in vector)
(def COLORS ["var( --soft-blue )"
             "var( --soft-purple )"
             "var( --soft-green )"
             "var( --soft-red )"])



;; -- Popup components --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-picker-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/color-picker ::color-picker
                         {:initial-options COLORS
                          :value-path [extension-id :item-editor/data-items :colors]}])



;; -- Bubble components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn notification-bubble-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (component)
  [bubble-id {:keys [label button-event button-label]}]
  [:<> [elements/horizontal-separator {:size :s}]
       [elements/label                {:content label :layout :fit}]
       [elements/horizontal-separator {:size :s}]
       [elements/button               {:label button-label :preset :primary-button :layout :fit
                                       :on-click {:dispatch-n [button-event [:ui/pop-bubble! bubble-id]]}}]
       [elements/horizontal-separator {:size :s}]])

(defn undo-delete-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (component)
  [extension-id item-namespace item-id]
  (let [dialog-id  (engine/dialog-id extension-id item-namespace :item-deleted)
        undo-event [:item-editor/undo-delete! extension-id item-namespace item-id]]
       [notification-bubble-body dialog-id
                                 {:label :item-deleted :button-event undo-event :button-label :recover!}]))

(defn changes-discarded-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (component)
  [extension-id item-namespace item-id]
  (let [dialog-id  (engine/dialog-id extension-id item-namespace :changes-discarded)
        undo-event [:item-editor/undo-discard! extension-id item-namespace item-id]]
       [notification-bubble-body dialog-id
                                 {:label :unsaved-changes-discarded :button-event undo-event :button-label :restore!}]))

(defn item-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  ;
  ; @return (component)
  [extension-id item-namespace copy-id]
  (let [dialog-id  (engine/dialog-id extension-id item-namespace :item-duplicated)
        edit-event [:item-editor/edit-item! extension-id item-namespace copy-id]]
       [notification-bubble-body dialog-id
                                 {:label :item-duplicated :button-event edit-event :button-label :edit-copy!}]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/render-color-picker-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [_ [_ extension-id item-namespace]]
      [:ui/add-popup! (engine/dialog-id extension-id item-namespace :color-picker)
                      {:body   [color-picker-dialog-body extension-id]
                      ;:header #'ui/close-popup-header
                       :min-width :none}]))

(a/reg-event-fx
  :item-editor/render-undo-delete-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [current-item-id (r subs/get-current-item-id db extension-id)]
           [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :item-deleted)
                             {:body       [undo-delete-dialog-body           extension-id item-namespace current-item-id]
                              :destructor [:item-editor/clean-recovery-data! extension-id item-namespace current-item-id]}])))

(a/reg-event-fx
  :item-editor/render-changes-discarded-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [current-item-id (r subs/get-current-item-id db extension-id)]
           [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :changes-discarded)
                             {:body       [changes-discarded-dialog-body     extension-id item-namespace current-item-id]
                              :destructor [:item-editor/clean-recovery-data! extension-id item-namespace current-item-id]}])))

(a/reg-event-fx
  :item-editor/render-edit-copy-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  (fn [_ [_ extension-id item-namespace copy-id]]
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :item-duplicated)
                        {:body [item-duplicated-dialog-body extension-id item-namespace copy-id]}]))
