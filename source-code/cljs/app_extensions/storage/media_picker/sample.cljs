
(ns app-extensions.storage.media-picker.sample
    (:require [x.app-core.api :as a]
              [app-extensions.storage.api :as storage]))



;; -- Example A. --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-media-picker
  []
  [storage/media-picker :my-picker {:value-path [:my-item]}])



;; -- Example B. --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :load-my-media-picker!
  [:storage.media-picker/load-picker! :my-picker {:value-path [:my-item]}])
