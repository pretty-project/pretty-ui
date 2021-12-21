
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.13
; Description:
; Version: v0.3.2
; Compatibility: x4.4.9



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



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- color-picker-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/color-picker ::color-picker
                         {:initial-options COLORS
                          :value-path [extension-id :item-editor/data-items :colors]}])

(defn- undo-delete-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (component)
  [extension-id item-namespace item-id]
  (let [dialog-id (engine/dialog-id extension-id item-namespace item-id :deleted)]
       [:<> [elements/horizontal-separator {:size :s}]
            [elements/label {:content :item-deleted :layout :fit}]
            [elements/horizontal-separator {:size :s}]
            [elements/button {:label :recover! :preset :primary-button :layout :fit
                              :on-click {:dispatch-n [[:item-editor/undo-delete! extension-id item-namespace item-id]
                                                      [:ui/pop-bubble! dialog-id]]}}]
            [elements/horizontal-separator {:size :s}]]))

(defn- changes-discarded-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (component)
  [extension-id item-namespace item-id]
  (let [dialog-id (engine/dialog-id extension-id item-namespace item-id :discarded)]
       [:<> [elements/horizontal-separator {:size :s}]
            [elements/label {:content :unsaved-changes-discarded :layout :fit}]
            [elements/horizontal-separator {:size :s}]
            [elements/button {:label :restore! :preset :primary-button :layout :fit
                              :on-click {:dispatch-n [[:item-editor/undo-discard! extension-id item-namespace item-id]
                                                      [:ui/pop-bubble! dialog-id]]}}]
            [elements/horizontal-separator {:size :s}]]))

(defn- item-duplicated-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  ;
  ; @return (component)
  [extension-id item-namespace copy-id]
  (let [dialog-id (engine/dialog-id extension-id item-namespace copy-id :duplicated)]
       [:<> [elements/horizontal-separator {:size :s}]
            [elements/label {:content :item-duplicated :layout :fit}]
            [elements/horizontal-separator {:size :s}]
            [elements/button {:label :edit-copy! :preset :primary-button :layout :fit
                              :on-click {:dispatch-n [[:item-editor/edit-copy! extension-id item-namespace copy-id]
                                                      [:ui/pop-bubble! dialog-id]]}}]
            [elements/horizontal-separator {:size :s}]]))



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
                      {:body   {:content [color-picker-dialog-body extension-id]}
                       :header {:content #'ui/close-popup-header}
                       :min-width :none}]))

(a/reg-event-fx
  :item-editor/render-undo-delete-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [current-item-id (r subs/get-current-item-id db extension-id)]
           [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace current-item-id :deleted)
                             {:body       {:content [undo-delete-dialog-body extension-id item-namespace current-item-id]}
                              :destructor [:item-editor/clean-recovery-data! extension-id item-namespace current-item-id]}])))

(a/reg-event-fx
  :item-editor/render-changes-discarded-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      (let [current-item-id (r subs/get-current-item-id db extension-id)]
           [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace current-item-id :discarded)
                             {:body {:content [changes-discarded-dialog-body extension-id item-namespace current-item-id]}
                              :destructor [:item-editor/clean-recovery-data! extension-id item-namespace current-item-id]}])))

(a/reg-event-fx
  :item-editor/render-edit-copy-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) copy-id
  (fn [_ [_ extension-id item-namespace copy-id]]
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace copy-id :duplicated)
                        {:body {:content [item-duplicated-dialog-body extension-id item-namespace copy-id]}}]))
