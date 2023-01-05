
(ns templates.file-editor.update.effects
    (:require [engines.file-editor.api :as file-editor]
              [re-frame.api            :as r :refer [r]]))

;; -- Save content effects ----------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :file-editor/content-saved
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id server-response]]
      [:x.ui/render-bubble! :file-editor/content-saved-dialog {:body :changes-saved}]))

(r/reg-event-fx :file-editor/save-content-failed
  ; @param (keyword) editor-id
  ; @param (map) server-response
  (fn [{:keys [db]} [_ editor-id _]]
      [:x.ui/render-bubble! :file-editor/save-content-failed-notification {:body :failed-to-save}]))
