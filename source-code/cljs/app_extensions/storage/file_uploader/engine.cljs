
(ns app-extensions.storage.file-uploader.engine
    (:require [app-fruits.dom :as dom]
              [mid-fruits.keyword :as keyword]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn any-file-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [file-selector (dom/get-element-by-id "storage--file-selector")]
       (dom/file-selector->any-file-selected? file-selector)))

(defn request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (keyword/add-namespace :storage uploader-id))
