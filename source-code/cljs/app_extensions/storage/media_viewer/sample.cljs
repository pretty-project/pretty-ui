
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.media-viewer.sample
    (:require [x.app-core.api :as a]
              [app-extensions.storage.api :as storage]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-media-viewer
  []
  [storage/media-viewer :my-viewer {:directory-id "my-directory"}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :load-my-media-viewer!
  [:storage.media-viewer/load-viewer! :my-viewer {:directory-id "my-directory"}])
