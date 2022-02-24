
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.file-uploader.sample
    (:require [x.app-core.api :as a]
              [app-extensions.storage.api]))



;; -- Example A. --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :load-my-file-uploader!
  [:storage.file-uploader/load-uploader! :my-uploader {:destination-id "my-directory"}])
