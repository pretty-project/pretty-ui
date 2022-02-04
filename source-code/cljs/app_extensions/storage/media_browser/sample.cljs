
(ns app-extensions.storage.media-browser.sample
    (:require [x.app-core.api :as a]
              [app-extensions.storage.api]))



;; -- Example A. --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :load-my-media-browser!
  [:router/go-to! "/@app-home/storage"])



;; -- Example B. --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :load-your-media-browser!
  [:router/go-to! "/@app-home/storage/my-directory"])
