
(ns app-extensions.storage.media-picker.subs
    (:require [x.app-core.api :as a]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-media-picker-props
  [db [_ picker-id]])

(a/reg-sub :storage/get-media-picker-props get-media-picker-props)
