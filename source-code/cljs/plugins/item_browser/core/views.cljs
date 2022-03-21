
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.views
    (:require [plugins.item-browser.core.helpers    :as core.helpers]
              [plugins.item-browser.core.prototypes :as core.prototypes]
              [plugins.item-lister.api              :as item-lister]
              [reagent.api                          :as reagent]
              [x.app-core.api                       :as a]
              [x.app-elements.api                   :as elements]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-home-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-browser/go-home-button :my-extension :my-type]
  [extension-id item-namespace]
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? extension-id item-namespace])
        error-mode?       @(a/subscribe [:item-browser/get-meta-item     extension-id item-namespace :error-mode?])
        at-home?          @(a/subscribe [:item-browser/at-home?          extension-id item-namespace])]
       [elements/button ::go-home-button
                        ; A go-home-button gomb a plugin {:error-mode? true} állapotában is használható!
                        {:disabled? (or browser-disabled? (and at-home? (not error-mode?)))
                         :on-click  [:item-browser/go-home! extension-id item-namespace]
                         :preset    :home-icon-button}]))

(defn go-up-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-browser/go-up-button :my-extension :my-type]
  [extension-id item-namespace]
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? extension-id item-namespace])
        at-home?          @(a/subscribe [:item-browser/at-home? extension-id item-namespace])]
       [elements/button ::go-up-button
                        {:disabled? (or browser-disabled? at-home?)
                         :on-click  [:item-browser/go-up! extension-id item-namespace]
                         :preset    :back-icon-button}]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [go-up-button                           extension-id item-namespace]
      [go-home-button                         extension-id item-namespace]
      [item-lister/new-item-button            extension-id item-namespace]
      [item-lister/sort-items-button          extension-id item-namespace]
      [item-lister/toggle-select-mode-button  extension-id item-namespace]
      [item-lister/toggle-reorder-mode-button extension-id item-namespace]]
    [:div.item-lister--header--menu-item-group
      [item-lister/search-block extension-id item-namespace]]])

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:new-item-route (string)(opt)
  ;   :new-item-options (vector)(opt)}
  ;
  ; @usage
  ;  [item-browser/header :my-extension :my-type {...}]
  [extension-id item-namespace header-props]
  (let [header-props (assoc header-props :menu-element #'menu-mode-header)]
       [item-lister/header extension-id item-namespace header-props]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  [extension-id item-namespace body-props]
  [item-lister/body extension-id item-namespace body-props])

(defn body
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: false
  ;   :download-limit (integer)(opt)
  ;    Default: plugins.item-lister.core.config/DEFAULT-DOWNLOAD-LIMIT
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :items-key (keyword)(opt)
  ;    Default: config/DEFAULT-ITEMS-KEY
  ;   :items-path (vector)(opt)
  ;    Default: core.helpers/default-items-path
  ;   :label-key (keyword)(opt)
  ;    Default: config/DEFAULT-LABEL-KEY
  ;   :list-element (metamorphic-content)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :order-by-options (namespaced keywords in vector)(opt)
  ;    Default: plugins.item-lister.core.config/DEFAULT-ORDER-BY-OPTIONS
  ;   :path-key (keyword)(opt)
  ;    Default: config/DEFAULT-PATH-KEY
  ;   :prefilter (map)(opt)
  ;   :root-item-id (string)(opt)
  ;   :search-keys (keywords in vector)(opt)
  ;    Default: plugins.item-lister.core.config/DEFAULT-SEARCH-KEYS}
  ;
  ; @example
  ;  [item-browser/body :my-extension :my-type {...}]
  ;
  ; @example
  ;  (defn my-list-element [extension-id item-namespace item-dex item] [:div ...])
  ;  [item-browser/body :my-extension :my-type {:list-element #'my-list-element
  ;                                             :prefilter    {:my-type/color "red"}}]
  [extension-id item-namespace body-props]
  (let [body-props (core.prototypes/body-props-prototype extension-id item-namespace body-props)]
       (reagent/lifecycles (core.helpers/component-id extension-id item-namespace :body)
                           {:reagent-render      (fn []             [body-structure               extension-id item-namespace body-props])
                            :component-did-mount (fn [] (a/dispatch [:item-browser/body-did-mount extension-id item-namespace body-props]))})))
