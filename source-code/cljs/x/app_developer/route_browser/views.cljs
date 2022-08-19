
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.route-browser.views
    (:require [mid-fruits.pretty :as pretty]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [route-id route-props]
  [:div {:style {:padding "12px 12px" :width "100%"}}
        [:div {:style {:font-size "14px" :font-weight "500"}}
              (str route-id)]
        [:pre {:style {:font-size "12px"}}
              (pretty/mixed->string route-props)]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [client-routes @(a/subscribe [:router/get-client-routes])]
       (letfn [(f [route-list route-id]
                  (let [route-props (get client-routes route-id)]
                       (conj route-list [route-list-item route-id route-props])))]
              (reduce f [:<>] (-> client-routes keys vector/abc-items)))))
