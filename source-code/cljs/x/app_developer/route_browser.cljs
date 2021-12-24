
(ns x.app-developer.route-browser
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.api   :as elements]
              [x.app-router.api     :as router]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-VIEW :routes)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:client-routes (r router/get-client-routes db)})

(a/reg-sub ::get-body-props get-body-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- route-list-item
  [body-id body-props route-id route-props]
  [:div {:style {:padding "12px 12px" :width "100%"}}
        [:div {:style {:font-size "14px" :font-weight "500"}}

              (str route-id)]
        [:div {:style {:font-size "14px"}}
              (str route-props)]])

(defn- route-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [client-routes] :as body-props}]
  (vec (reduce-kv (fn [route-list route-id route-props]
                      (conj route-list [route-list-item body-id body-props route-id route-props]))
                  [:<>] client-routes)))

(defn- route-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [route-list body-id body-props])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [components/subscriber ::body
                         {:component  #'route-browser
                          :subscriber [::get-body-props]}])
