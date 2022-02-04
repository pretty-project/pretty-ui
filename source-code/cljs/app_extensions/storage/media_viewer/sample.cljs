
(ns app-extensions.storage.media-viewer.sample
    (:require [x.app-core.api :as a]
              [app-extensions.storage.api :as storage]))



;; -- Example A. --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-media-viewer
  []
  [storage/media-viewer :my-viewer {:items-path [:my-item]}])



;; -- Example B. --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :load-my-media-viewer!
  [:storage.media-viewer/load-viewer! :my-viewer {:items-path [:my-item]}])
