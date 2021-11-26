
(ns extensions.services.engine
    (:require [x.app-core.api :as a]
              [x.app-db.api   :as db]))



;; -- TEMP helpers ------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-data-prototype
  ; @param (map) service-data
  ;
  ; @return (map)
  ;   {}
  [service-data]
  (merge {:quantity-unit :hour}
         service-data))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :extensions.services/download-services!
  [])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:router/add-route!
   {:route-event    [:extensions.services/render!]
    :route-template "/services"}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [::initialize!]})
