
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
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  [item-browser/go-home-button :my-browser]
  [browser-id]
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? browser-id])
        at-home?          @(a/subscribe [:item-browser/at-home?          browser-id])
        error-mode?       @(a/subscribe [:item-browser/get-meta-item     browser-id :error-mode?])]
       [elements/button ::go-home-button
                        ; A go-home-button gomb a plugin {:error-mode? true} állapotában is használható!
                        {:disabled? (or browser-disabled? (and at-home? (not error-mode?)))
                         :on-click  [:item-browser/go-home! browser-id]
                         :preset    :home-icon-button}]))

(defn go-up-button
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  [item-browser/go-up-button :my-browser]
  [browser-id]
  (let [browser-disabled? @(a/subscribe [:item-browser/browser-disabled? browser-id])
        at-home?          @(a/subscribe [:item-browser/at-home?          browser-id])]
       [elements/button ::go-up-button
                        {:disabled? (or browser-disabled? at-home?)
                         :on-click  [:item-browser/go-up! browser-id]
                         :preset    :back-icon-button}]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  [browser-id]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group [go-up-button                           browser-id]
                                               [go-home-button                         browser-id]
                                               [item-lister/new-item-button            browser-id]
                                               [item-lister/sort-items-button          browser-id]
                                               [item-lister/toggle-select-mode-button  browser-id]
                                               [item-lister/toggle-reorder-mode-button browser-id]]
    [:div.item-lister--header--menu-item-group [item-lister/search-block browser-id]]])

(defn header
  ; @param (keyword) browser-id
  ; @param (map) header-props
  ;  {:new-item-event (metamorphic-event)(opt)
  ;   :new-item-options (vector)(opt)}
  ;
  ; @usage
  ;  [item-browser/header :my-browser {...}]
  [browser-id header-props]
  (let [header-props (assoc header-props :menu-element #'menu-mode-header)]
       [item-lister/header browser-id header-props]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  [browser-id body-props]
  [item-lister/body browser-id body-props])

(defn body
  ; @param (keyword) browser-id
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
  ;   :root-item-id (string)
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
  ; Az item-browser body komponensének Reagent azonosítója nem egyezhet meg az item-lister
  ; body komponensének Reagent azonosítójával!
  (let [body-props (core.prototypes/body-props-prototype browser-id body-props)]
       (reagent/lifecycles (core.helpers/component-id browser-id :body-wrapper)
                           {:reagent-render      (fn []             [body-structure               browser-id body-props])
                            :component-did-mount (fn [] (a/dispatch [:item-browser/body-did-mount browser-id body-props]))})))
