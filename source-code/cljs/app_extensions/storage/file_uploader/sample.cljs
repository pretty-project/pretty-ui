
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.file-uploader.sample
    (:require [app-extensions.storage.api]
              [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :load-my-file-uploader!
  [:storage.file-uploader/load-uploader! :my-uploader {:destination-id "my-directory"}])
