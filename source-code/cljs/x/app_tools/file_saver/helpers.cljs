
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.file-saver.helpers
    (:require [app-fruits.dom :as dom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-file-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (-> "x-file-saver" dom/get-element-by-id .click))