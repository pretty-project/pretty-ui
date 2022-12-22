
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.body.views
    (:require [engines.item-browser.body.prototypes :as body.prototypes]
              [engines.item-browser.core.helpers    :as core.helpers]
              [engines.item-lister.body.views       :as body.views]
              [re-frame.api                         :as r]
              [reagent.api                          :as reagent]
              [tools.infinite-loader.api            :as infinite-loader]
              [x.components.api                     :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-lister.body.views
(def list-element  body.views/list-element)
(def placeholder   body.views/placeholder)
(def ghost-element body.views/ghost-element)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  [browser-id]
  (if-let [error-element @(r/subscribe [:item-browser/get-body-prop browser-id :error-element])]
          [x.components/content browser-id error-element]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  [browser-id]
  ; XXX#6177 (source-code/cljs/engines/item_browser/body/effects.cljs)
  ; XXX#1249 (source-code/cljs/engines/item_lister/body/effects.cljs)
  (cond @(r/subscribe [:item-browser/get-meta-item browser-id :engine-error])
         [error-element browser-id]
       ;@(r/subscribe [:x.environment/browser-offline?])
       ; [offline-body browser-id]
        @(r/subscribe [:item-browser/data-received? browser-id])
         [:<> [list-element              browser-id]
              [infinite-loader/component browser-id {:on-viewport [:item-browser/request-items! browser-id]}]
              [placeholder               browser-id]
              [ghost-element             browser-id]]
         :data-not-received
         [ghost-element browser-id]))

(defn body
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ; {:auto-title? (boolean)(opt)
  ;   Default: false
  ;   W/ {:label-key ...}
  ;  :clear-behaviour (keyword)(opt)
  ;   :none, :on-leave, :on-item-change
  ;   Default: :none
  ;  :default-item-id (string)
  ;  :default-order-by (namespaced keyword)
  ;  :display-progress? (boolean)(opt)
  ;   Default: true
  ;  :download-limit (integer)(opt)
  ;   Default: engines.item-lister.core.config/DEFAULT-DOWNLOAD-LIMIT
  ;  :error-element (metamorphic-content)(opt)
  ;  :ghost-element (metamorphic-content)(opt)
  ;  :item-id (string)(opt)
  ;  :items-key (keyword)
  ;  :items-path (vector)(opt)
  ;   Default: core.helpers/default-items-path
  ;  :label-key (keyword)
  ;   W/ {:auto-title? true}
  ;  :list-element (metamorphic-content)
  ;  :path-key (keyword)
  ;  :placeholder (metamorphic-content)(opt)
  ;   Default: :no-items-to-show
  ;  :prefilter (map)(opt)
  ;  :query (vector)(opt)
  ;  :search-keys (keywords in vector)(opt)
  ;   Default: engines.item-lister.core.config/DEFAULT-SEARCH-KEYS
  ;  :transfer-id (keyword)(opt)}
  ;
  ; @usage
  ; [body :my-browser {...}]
  ;
  ; @usage
  ; (defn my-list-element [browser-id] [:div ...])
  ; [body :my-browser {:list-element #'my-list-element
  ;                    :prefilter    {:my-type/color "red"}}]
  [browser-id body-props]
  (let [body-props (body.prototypes/body-props-prototype browser-id body-props)]
       (reagent/lifecycles (core.helpers/component-id browser-id :body)
                           {:component-did-mount    (fn []  (r/dispatch [:item-browser/body-did-mount    browser-id body-props]))
                            :component-will-unmount (fn []  (r/dispatch [:item-browser/body-will-unmount browser-id]))
                            :component-did-update   (fn [%] (r/dispatch [:item-browser/body-did-update   browser-id %]))
                            :reagent-render         (fn []              [body-structure                  browser-id body-props])})))
