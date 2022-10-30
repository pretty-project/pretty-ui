
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.querier.views
    (:require [mid-fruits.random              :as random]
              [reagent.api                    :as reagent]
              [re-frame.api                   :as r]
              [x.app-components.content.views :as content.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- querier-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) querier-id
  ; @param (map) querier-props
  ;  {:component (component)(opt)
  ;   :render-f (function)(opt)}
  [querier-id {:keys [component render-f]}]
  (if-let [server-response @(r/subscribe [:pathom/get-query-response querier-id])]
          (cond render-f  [render-f       server-response]
                component (conj component server-response))))

(defn- querier
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) querier-id
  ; @param (map) querier-props
  ;  {:query (vector)}
  [querier-id {:keys [query] :as querier-props}]
  (reagent/lifecycles {:component-did-mount    (fn [] (r/dispatch [:pathom/send-query!           querier-id {:query query}]))
                       :component-will-unmount (fn [] (r/dispatch [:pathom/clear-query-response! querier-id]))
                       :reagent-render         (fn [] [querier-content querier-id querier-props])}))

(defn component
  ; @param (keyword)(opt) querier-id
  ; @param (map) querier-props
  ;  {:component (component)(opt)
  ;   :query (vector)
  ;   :render-f (function)(opt)}
  ;
  ; @usage
  ;  [querier {...}]
  ;
  ; @usage
  ;  [querier :my-querier {...}]
  ;
  ; @usage
  ;  (defn my-component [server-response] ...)
  ;  [querier :my-querier {:component [my-component]
  ;                        :query     [:my-query]}]
  ;
  ; @usage
  ;  (defn my-component [server-response] ...)
  ;  [querier :my-querier {:query    [:my-query]
  ;                        :render-f #'my-component}]
  ([querier-props]
   [component (random/generate-keyword) querier-props])

  ([querier-id querier-props]
   [querier querier-id querier-props]))
