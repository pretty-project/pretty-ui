
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.components.querier.views
    (:require [random.api                 :as random]
              [reagent.api                :as reagent]
              [re-frame.api               :as r]
              [x.components.content.views :as content.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- querier-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) querier-id
  ; @param (map) querier-props
  ;  {:placeholder (metamorphic-content)(opt)}
  [querier-id {:keys [placeholder]}]
  (if placeholder [content.views/component querier-id {:content placeholder}]))

(defn- querier-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) querier-id
  ; @param (map) querier-props
  ;  {:content (metamorphic-content)(opt)}
  [querier-id {:keys [content]}]
  (if content (let [server-response @(r/subscribe [:pathom/get-query-response querier-id])]
                   [content.views/component querier-id {:content content
                                                        :params  [server-response]}])))

(defn- querier-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) querier-id
  ; @param (map) querier-props
  [querier-id querier-props]
  (if (or @(r/subscribe [:x.sync/request-stalled? querier-id])
          @(r/subscribe [:x.sync/request-resent?  querier-id]))
      [querier-content     querier-id querier-props]
      [querier-placeholder querier-id querier-props]))

(defn- querier
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) querier-id
  ; @param (map) querier-props
  ;  {:query (vector)
  ;   :refresh-interval (ms)(opt)}
  [querier-id {:keys [query refresh-interval] :as querier-props}]
  (let [query-props {:query query :refresh-interval refresh-interval}]
       (reagent/lifecycles {:component-did-mount    (fn [] (r/dispatch [:pathom/send-query!           querier-id query-props]))
                            :component-will-unmount (fn [] (r/dispatch [:pathom/clear-query-response! querier-id]))
                            :reagent-render         (fn [] [querier-body querier-id querier-props])})))

(defn component
  ; @param (keyword)(opt) querier-id
  ; @param (map) querier-props
  ;  {:content (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :query (vector)
  ;   :refresh-interval (ms)(opt)}
  ;
  ; @usage
  ;  [querier {...}]
  ;
  ; @usage
  ;  [querier :my-querier {...}]
  ;
  ; @usage
  ;  [querier :my-querier {:query [:my-query]}]
  ;
  ; @usage
  ;  (defn my-component [querier-id server-response] ...)
  ;  [querier :my-querier {:content [my-component]
  ;                        :query   [:my-query]}]
  ;
  ; @usage
  ;  (defn my-component [querier-id server-response] ...)
  ;  [querier :my-querier {:content  #'my-component
  ;                        :query    [:my-query]}]
  ([querier-props]
   [component (random/generate-keyword) querier-props])

  ([querier-id querier-props]
   [querier querier-id querier-props]))
