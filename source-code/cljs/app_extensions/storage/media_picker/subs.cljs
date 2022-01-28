
(ns app-extensions.storage.media-picker.subs
    (:require [x.app-core.api :as a :refer [r]]
              [app-plugins.item-browser.api :as item-browser]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-header-mode
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ picker-id]]
  {:search-mode? (get-in db [:storage :item-lister/meta-items :search-mode?])})

(a/reg-sub :storage.media-picker/get-header-mode get-header-mode)

(defn get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ picker-id]]
  (r item-browser/get-header-props db :storage :media))

(a/reg-sub :storage.media-picker/get-header-props get-header-props)

(defn get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ picker-id]])

(a/reg-sub :storage.media-picker/get-body-props get-body-props)
