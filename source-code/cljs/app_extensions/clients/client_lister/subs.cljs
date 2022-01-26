
(ns app-extensions.clients.client-lister.subs
    (:require [x.app-activities.api :as activities]
              [x.app-core.api       :as a :refer [r]]
              [x.app-locales.api    :as locales]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-item-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ _ {:keys [modified-at]}]]
  {:modified-at       (r activities/get-actual-timestamp db modified-at)
   :selected-language (r locales/get-selected-language   db)})

(a/reg-sub :clients.client-lister/get-client-item-props get-item-props)
