
(ns app-extensions.storage.file-uploader.engine
    (:require [app-fruits.dom :as dom]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn any-file-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [file-selector (dom/get-element-by-id "storage--file-selector")]
       (dom/file-selector->any-file-selected? file-selector)))
