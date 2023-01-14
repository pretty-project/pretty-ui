
(ns templates.file-editor.update.effects
    (:require [re-frame.api                       :as r]
              [templates.file-editor.update.views :as update.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :file-editor/content-saved
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [_ [_ editor-id]]
      [:file-editor/render-content-saved-dialog! editor-id]))

(r/reg-event-fx :file-editor/save-content-failed
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [_ [_ editor-id]]
      [:file-editor/render-save-content-failed-dialog! editor-id]))

(r/reg-event-fx :file-editor/render-content-saved-dialog!
  ; @param (keyword) editor-id
  (fn [_ [_ editor-id]]
      [:x.ui/render-bubble! :file-editor/content-saved-dialog
                            {:content [update.views/content-saved-dialog editor-id]}]))

(r/reg-event-fx :file-editor/render-save-content-failed-dialog
  ; @param (keyword) editor-id
  (fn [_ [_ editor-id]]
      [:x.ui/render-bubble! :file-editor/save-content-failed-dialog
                            {:content [update.views/save-content-failed-dialog editor-id]}]))
