
(ns tools.file-saver.helpers
    (:require [dom.api :as dom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-file-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (-> "file-saver" dom/get-element-by-id .click))
