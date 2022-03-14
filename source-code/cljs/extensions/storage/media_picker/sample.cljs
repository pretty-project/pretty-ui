
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-picker.sample
    (:require [extensions.storage.api :as storage]
              [x.app-core.api         :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-media-picker
  []
  [storage/media-picker :my-picker {:value-path [:my-item]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :load-my-media-picker!
  [:storage.media-picker/load-picker! :my-picker {:value-path [:my-item]}])
