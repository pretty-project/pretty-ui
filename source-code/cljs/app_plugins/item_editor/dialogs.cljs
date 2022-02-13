
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.13
; Description:
; Version: v0.5.8
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.dialogs
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [x.app-ui.api       :as ui]
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
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id _]
  [elements/color-picker ::color-picker
                         {:initial-options COLORS
                          :value-path [extension-id :item-editor/data-items :colors]}])



;; -- Bubble components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (component)
  [extension-id item-namespace item-id]
  (let [undo-event [:item-editor/undo-delete! extension-id item-namespace item-id]]
       [ui/state-changed-bubble-body (engine/dialog-id extension-id item-namespace :item-deleted)
                                     {:label :item-deleted
                                      :primary-button {:on-click undo-event :label :recover!}}]))

(defn changes-discarded-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (component)
  [extension-id item-namespace item-id]
  (let [undo-event [:item-editor/undo-discard! extension-id item-namespace item-id]]
       [ui/state-changed-bubble-body (engine/dialog-id extension-id item-namespace :changes-discarded)
                                     {:label :unsaved-changes-discarded
                                      :primary-button {:on-click undo-event :label :restore!}}]))

(defn item-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  ;
  ; @return (component)
  [extension-id item-namespace copy-id]
  (let [edit-event [:item-editor/edit-item! extension-id item-namespace copy-id]]
       [ui/state-changed-bubble-body (engine/dialog-id extension-id item-namespace :item-duplicated)
                                     {:label :item-duplicated
                                      :primary-button {:on-click edit-event :label :edit-copy!}}]))



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
                      {:body   [color-picker-dialog-body extension-id item-namespace]
                      ;:header #'ui/close-popup-header
                       :min-width :none}]))

(a/reg-event-fx
  :item-editor/render-undo-delete-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [current-item-id (r subs/get-current-item-id db extension-id item-namespace)]
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
      (let [current-item-id (r subs/get-current-item-id db extension-id item-namespace)]
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
