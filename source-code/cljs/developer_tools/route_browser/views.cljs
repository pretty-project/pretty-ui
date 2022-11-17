
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.route-browser.views
    (:require [candy.api    :refer [return]]
              [elements.api :as elements]
              [pretty.print :as pretty]
              [re-frame.api :as r]
              [string.api   :as string]
              [vector.api   :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- route-filter-field
  []
  [elements/text-field {:indent      {:horizontal :s :vertical :xs}
                        :label       "Filter"
                        :placeholder "/my-route"
                        :value-path  [:developer-tools :route-browser/meta-items :filter-term]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- route-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [route-id route-props]
  [:div {:style {:padding "12px 12px" :width "100%"}}
        [:div {:style {:font-size "14px" :font-weight "500"}}
              (str route-id)]
        [:pre {:style {:font-size "12px"}}
              (pretty/mixed->string route-props)]])

(defn- route-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [client-routes @(r/subscribe [:x.router/get-client-routes])
        filter-term   @(r/subscribe [:x.db/get-item [:developer-tools :route-browser/meta-items :filter-term]])]
       (letfn [(filter [routes route-id {:keys [route-template] :as route-props}]
                       (if (string/starts-with? route-template filter-term)
                           (assoc  routes route-id route-props)
                           (return routes)))
               (f [route-list route-id]
                  (let [route-props (get client-routes route-id)]
                       (conj route-list [route-list-item route-id route-props])))]
              (let [filtered-routes (reduce-kv filter {} client-routes)]
                   (reduce f [:<>] (-> filtered-routes keys vector/abc-items))))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [route-filter-field]
       [route-list]])
