
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-selector.sample
    (:require [extensions.storage.api]
              [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :load-my-media-selector!
  [:storage.media-selector/load-selector! :my-selector {:value-path [:my-item]}])
