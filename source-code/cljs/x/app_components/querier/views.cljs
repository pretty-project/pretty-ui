
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.querier.views
    (:require [mid-fruits.random :as random]
              [reagent.api       :as reagent]
              [x.app-core.api    :as a]))



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
  (if-let [server-response @(a/subscribe [:sync/get-request-response querier-id])]
          (cond render-f  [render-f]
                component component)))

(defn- querier
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) querier-id
  ; @param (map) querier-props
  ;  {:query (vector)}
  [querier-id {:keys [query] :as querier-props}]
  (reagent/lifecycles {:component-did-mount (fn [] (a/dispatch [:pathom/send-query! querier-id {:query query}]))
                       :reagent-render      (fn [] [querier-content querier-id querier-props])}))

(defn component
  ; @param (keyword)(opt) querier-id
  ; @param (map) querier-props
  ;  {:component (component)(opt)
  ;    Only w/o {:render-f ...}
  ;   :query (vector)
  ;   :render-f (function)(opt)
  ;    Only w/o {:component ...}}
  ;
  ; @usage
  ;  [components/querier {...}]
  ;
  ; @usage
  ;  [components/querier :my-querier {...}]
  ;
  ; @usage
  ;  (defn my-component [] ...)
  ;  [components/querier :my-querier {:component [my-component]
  ;                                   :query     [:my-query]}]
  ;
  ; @usage
  ;  (defn my-component [] ...)
  ;  [components/querier :my-querier {:query    [:my-query]
  ;                                   :render-f #'my-component}]
  ([querier-props]
   [component (random/generate-keyword) querier-props])

  ([querier-id querier-props]
   [querier querier-id querier-props]))
