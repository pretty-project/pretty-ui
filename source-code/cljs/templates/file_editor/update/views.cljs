
(ns templates.file-editor.update.views
    (:require [components.api :as components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-saved-dialog
  ; @param (keyword) editor-id
  [_]
  [components/notification-bubble :file-editor/content-saved-dialog
                                  {:content :changes-saved}])

(defn save-content-failed-dialog
  ; @param (keyword) editor-id
  [_]
  [components/notification-bubble :file-editor/save-content-failed-notification
                                  {:content :failed-to-save}])
