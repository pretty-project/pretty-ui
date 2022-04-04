
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.colors.effects
    (:require [plugins.item-editor.colors.views :as colors.views]
              [x.app-core.api                   :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/render-color-picker-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [_ [_ editor-id]]
      [:ui/add-popup! :plugins.item-editor/color-picker-dialog
                      {:body [colors.views/color-picker-dialog-body editor-id]
                      ;:header #'ui/close-popup-header
                       :min-width :none}]))
