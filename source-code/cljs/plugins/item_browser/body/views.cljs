

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.body.views
    (:require [plugins.item-browser.body.prototypes :as body.prototypes]
              [plugins.item-browser.core.helpers    :as core.helpers]
              [plugins.item-lister.body.views       :as body.views]
              [reagent.api                          :as reagent]
              [x.app-core.api                       :as a]
              [x.app-tools.api                      :as tools]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-lister.body.views
(def error-body        body.views/error-body)
(def item-list         body.views/item-list)
(def no-items-to-show  body.views/no-items-to-show)
(def downloading-items body.views/downloading-items)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  [browser-id]
  ; XXX#6177
  (cond @(a/subscribe [:item-browser/get-meta-item browser-id :error-mode?])
         [error-body browser-id {:error-description :the-content-you-opened-may-be-broken}]
        ;@(a/subscribe [:environment/browser-offline?])
        ; [offline-body browser-id]
        @(a/subscribe [:item-browser/data-received? browser-id])
         [:<> [item-list             browser-id]
              [tools/infinite-loader browser-id {:on-viewport [:item-browser/request-items! browser-id]}]
              [no-items-to-show      browser-id]
              [downloading-items     browser-id]]
        :data-not-received
         [downloading-items browser-id]))

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
  ;   :ghost-element (metamorphic-content)(opt)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :items-key (keyword)
  ;   :items-path (vector)(opt)
  ;    Default: core.helpers/default-items-path
  ;   :label-key (keyword)
  ;    Only w/ {:auto-title? true}
  ;   :list-element (metamorphic-content)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :order-by-options (namespaced keywords in vector)(opt)
  ;    Default: plugins.item-lister.core.config/DEFAULT-ORDER-BY-OPTIONS
  ;   :path-key (keyword)
  ;   :prefilter (map)(opt)
  ;   :search-keys (keywords in vector)(opt)
  ;    Default: plugins.item-lister.core.config/DEFAULT-SEARCH-KEYS}
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
                            :component-did-mount    (fn []  (a/dispatch [:item-browser/body-did-mount    browser-id body-props]))
                            :component-will-unmount (fn []  (a/dispatch [:item-browser/body-will-unmount browser-id]))
                            :component-did-update   (fn [%] (a/dispatch [:item-browser/body-did-update   browser-id %]))})))
