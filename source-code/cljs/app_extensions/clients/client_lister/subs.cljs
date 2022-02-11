
(ns app-extensions.clients.client-lister.subs
    (:require [x.app-activities.api :as activities]
              [x.app-core.api       :as a :refer [r]]
              [x.app-locales.api    :as locales]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-item-modified-at
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ {:keys [modified-at]}]]
  (r activities/get-actual-timestamp db modified-at))

(a/reg-sub :clients.client-lister/get-item-modified-at get-item-modified-at)
