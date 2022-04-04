
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.form.effects
    (:require [plugins.item-editor.form.views :as form.views]
              [x.app-core.api                 :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/render-color-picker-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [_ [_ editor-id]]
      [:ui/add-popup! :plugins.item-editor/color-picker-dialog
                      {:body [form.views/color-picker-dialog-body editor-id]
                      ;:header #'ui/close-popup-header
                       :min-width :none}]))
