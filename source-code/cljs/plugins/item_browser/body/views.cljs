
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.body.views
    (:require [plugins.item-browser.body.prototypes :as body.prototypes]
              [plugins.item-browser.core.helpers    :as core.helpers]
              [plugins.item-lister.body.views       :as body.views]
              [reagent.api                          :as reagent]
              [tools.infinite-loader.api            :as infinite-loader]
              [re-frame.api                         :as r]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.body.views
(def item-list         body.views/item-list)
(def no-items-to-show  body.views/no-items-to-show)
(def ghost-element     body.views/ghost-element)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  [browser-id]
  (if-let [error-element @(r/subscribe [:item-browser/get-body-prop browser-id :error-element])]
          [error-element browser-id]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  [browser-id]
  ; XXX#6177
  (cond @(r/subscribe [:item-browser/get-meta-item browser-id :error-mode?])
         [error-element browser-id]
        ;@(r/subscribe [:environment/browser-offline?])
        ; [offline-body browser-id]
        @(r/subscribe [:item-browser/data-received? browser-id])
         [:<> [item-list                 browser-id]
              [infinite-loader/component browser-id {:on-viewport [:item-browser/request-items! browser-id]}]
              [no-items-to-show          browser-id]
              [ghost-element             browser-id]]
        :data-not-received
         [ghost-element browser-id]))

(defn body
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:label-key ...}
  ;   :default-item-id (string)
  ;   :default-order-by (namespaced keyword)
  ;   :download-limit (integer)(opt)
  ;    Default: plugins.item-lister.core.config/DEFAULT-DOWNLOAD-LIMIT
  ;   :error-element (metamorphic-content)(opt)
  ;   :ghost-element (metamorphic-content)(opt)
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :items-key (keyword)
  ;   :items-path (vector)(opt)
  ;    Default: core.helpers/default-items-path
  ;   :label-key (keyword)
  ;    Only w/ {:auto-title? true}
  ;   :list-element (metamorphic-content)
  ;   :order-by-options (namespaced keywords in vector)(opt)
  ;    Default: plugins.item-lister.core.config/DEFAULT-ORDER-BY-OPTIONS
  ;   :path-key (keyword)
  ;   :prefilter (map)(opt)
  ;   :query (vector)(opt)
  ;   :search-keys (keywords in vector)(opt)
  ;    Default: plugins.item-lister.core.config/DEFAULT-SEARCH-KEYS
  ;   :transfer-id (keyword)(opt)}
  ;
  ; @example
  ;  [item-browser/body :my-browser {...}]
  ;
  ; @example
  ;  (defn my-list-element [browser-id item-dex item] [:div ...])
  ;  [item-browser/body :my-browser {:list-element #'my-list-element
  ;                                  :prefilter    {:my-type/color "red"}}]
  [browser-id body-props]
  (let [body-props (body.prototypes/body-props-prototype browser-id body-props)]
       (reagent/lifecycles (core.helpers/component-id browser-id :body)
                           {:reagent-render         (fn []              [body-structure                  browser-id body-props])
                            :component-did-mount    (fn []  (r/dispatch [:item-browser/body-did-mount    browser-id body-props]))
                            :component-will-unmount (fn []  (r/dispatch [:item-browser/body-will-unmount browser-id]))
                            :component-did-update   (fn [%] (r/dispatch [:item-browser/body-did-update   browser-id %]))})))
