
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
              [app-plugins.item-editor.engine :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (strings in vector)
(def COLORS ["var( --soft-blue )"
             "var( --soft-purple )"
             "var( --soft-green )"
             "var( --soft-red )"])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- color-picker-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/color-picker ::color-picker
                         {:initial-options COLORS
                          :value-path [extension-id :editor-data :colors]}])

(defn- undo-delete-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @return (component)
  [extension-id item-namespace item-id]
  (let [dialog-id (engine/dialog-id extension-id item-namespace :delete item-id)]
       [elements/button {:label :undo-delete! :preset :primary-button
                         :on-click {:dispatch-n [[:item-editor/undo-delete! extension-id item-namespace item-id]
                                                 [:ui/pop-bubble! dialog-id]]}}]))



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
                      {:body   {:content [color-picker-body extension-id]}
                       :header {:content #'ui/close-popup-header}
                       :min-width :none}]))

(a/reg-event-fx
  :item-editor/render-undo-delete-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  (fn [_ [_ extension-id item-namespace item-id]]
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :delete item-id)
                        {:body {:content [undo-delete-body extension-id item-namespace item-id]}
                         :autopop? false}]))
