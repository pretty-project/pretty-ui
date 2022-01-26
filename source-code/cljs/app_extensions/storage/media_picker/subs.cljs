
(ns app-extensions.storage.media-picker.subs
    (:require [x.app-core.api :as a]
              [app-plugins.item-browser.api :as item-browser]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ picker-id]])

(a/reg-sub :storage.media-picker/get-header-props get-header-props)

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ picker-id]])

(a/reg-sub :storage.media-picker/get-body-props get-body-props)
