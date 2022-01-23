
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.8
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.views
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-db.api         :as db]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-layouts.api    :as layouts]
              [x.app-tools.api      :as tools]
              [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-lister.api     :as item-lister]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-home-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:at-home? (boolean)(opt)}
  ;
  ; @usage
  ;  [item-browser/go-home-button :my-extension :my-type {...}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [at-home? error-mode?]}]
  [elements/button ::go-home-button
                   ; A go-home-button gomb a böngésző {:error-mode? true} állapotában használható!
                   {:disabled? (and at-home? (not error-mode?))
                    :on-click  [:item-browser/go-home! extension-id item-namespace]
                    :preset    :home-icon-button}])

(defn go-up-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:at-home? (boolean)(opt)
  ;   :disabled? (boolean)(opt)}
  ;
  ; @usage
  ;  [item-browser/go-up-button :my-extension :my-type {...}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [at-home? disabled?]}]
  [elements/button ::go-up-button
                   {:disabled? (or at-home? disabled?)
                    :on-click  [:item-browser/go-up! extension-id item-namespace]
                    :preset    :back-icon-button}])



;; -- Error components --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [:<> [elements/horizontal-separator {:size :xxl}]
       [elements/label {:content :an-error-occured :font-size :m :min-height :m}]
       [elements/label {:content :the-item-you-opened-may-be-broken :color :muted :min-height :m}]])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:new-item-options (vector)(opt)
  ;   :no-items-to-show? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [new-item-options no-items-to-show?] :as header-props}]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [go-up-button                    extension-id item-namespace header-props]
      [go-home-button                  extension-id item-namespace header-props]
      (if new-item-options [item-lister/new-item-select extension-id item-namespace header-props]
                           [item-lister/new-item-button extension-id item-namespace])
      [item-lister/sort-items-button         extension-id item-namespace header-props]
      [item-lister/toggle-select-mode-button extension-id item-namespace header-props]]
     ;[item-lister/toggle-reorder-mode-button extension-id item-namespace header-props]
    [:div.item-lister--header--menu-item-group
      [item-lister/search-block extension-id item-namespace header-props]]])

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) header-props
  ;  {:new-item-options (vector)(opt)}
  ;
  ; @example
  ;  [item-browser/header :my-extension :my-type]
  ;
  ; @example
  ;  [item-browser/header :my-extension :my-type {:new-item-options [:add-my-type! :add-your-type!]}]
  ;
  ; @return (component)
  ([extension-id item-namespace]
   [header extension-id item-namespace {}])

  ([extension-id item-namespace header-props]
   (let [subscribed-props (a/subscribe [:item-browser/get-header-props extension-id item-namespace])]
        (fn [_ _ header-props] (let [header-props (merge header-props {:menu #'menu-mode-header} @subscribed-props)]
                                    [item-lister/header extension-id item-namespace header-props])))))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) body-props
  ;  {:list-element (metamorphic-content)}
  ;
  ; @example
  ;  [item-browser/body :my-extension :my-type {...}]
  ;
  ; @example
  ;  (defn my-list-element [item-dex item] [:div ...])
  ;  [item-browser/body :my-extension :my-type {:list-element #'my-list-element}]
  ;
  ; @return (component)
  [extension-id item-namespace body-props]
  [item-lister/body extension-id item-namespace body-props])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [extension-id item-namespace {:keys [description error-mode?] :as view-props}]
  (if error-mode? ; If error-mode is enabled ...
                  [layouts/layout-a extension-id {:body   [error-body extension-id item-namespace]
                                                  :header [header     extension-id item-namespace view-props]}]
                  ; If error-mode is NOT enabled ...
                  [layouts/layout-a extension-id {:body   [body   extension-id item-namespace view-props]
                                                  :header [header extension-id item-namespace view-props]
                                                  :description description}]))

(defn view
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) view-props
  ;  {:new-item-options (vector)(opt)
  ;   :list-element (component)}
  ;
  ; @usage
  ;  [item-browser/view :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item] [:div ...])
  ;  [item-browser/view :my-extension :my-type {:element #'my-list-element
  ;                                             :new-item-options [:add-my-type! :add-your-type!]}]
  ;
  ; @return (component)
  [extension-id item-namespace view-props]
  (let [subscribed-props (a/subscribe [:item-browser/get-view-props extension-id item-namespace])]
       (fn [] [layout extension-id item-namespace (merge view-props @subscribed-props)])))
