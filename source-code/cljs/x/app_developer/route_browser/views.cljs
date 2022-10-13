
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.route-browser.views
    (:require [mid-fruits.pretty :as pretty]
              [mid-fruits.vector :as vector]
              [re-frame.api      :as r]))



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
  (let [client-routes @(r/subscribe [:router/get-client-routes])]
       (letfn [(f [route-list route-id]
                  (let [route-props (get client-routes route-id)]
                       (conj route-list [route-list-item route-id route-props])))]
              (reduce f [:<>] (-> client-routes keys vector/abc-items)))))
