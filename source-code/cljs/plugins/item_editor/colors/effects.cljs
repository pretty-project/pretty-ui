
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
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [_ [_ extension-id item-namespace]]
      [:ui/add-popup! :plugins.item-editor/color-picker-dialog
                      {:body [colors.views/color-picker-dialog-body extension-id item-namespace]
                      ;:header #'ui/close-popup-header
                       :min-width :none}]))
