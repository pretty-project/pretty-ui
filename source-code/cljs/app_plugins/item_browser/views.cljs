
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.8
; Compatibility: x4.5.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.views
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-db.api         :as db]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.api   :as elements]
              [x.app-layouts.api    :as layouts]
              [x.app-tools.api      :as tools]
              [app-fruits.react-transition     :as react-transition]
              [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-lister.api     :as item-lister]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-home-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-browser/go-home-button :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [error-mode? @(a/subscribe [:item-lister/error-mode?       extension-id item-namespace])
        %           @(a/subscribe [:item-browser/get-header-props extension-id item-namespace])]
       [elements/button ::go-home-button
                        ; A go-home-button gomb az item-lister plugin {:error-mode? true} állapotában is használható!
                        {:disabled? (and (:at-home? %) (not error-mode?))
                         :on-click  [:item-browser/go-home! extension-id item-namespace]
                         :preset    :home-icon-button}]))

(defn go-up-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-browser/go-up-button :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [% @(a/subscribe [:item-browser/get-header-props extension-id item-namespace])]
       [elements/button ::go-up-button
                        {:disabled? (:at-home? %)
                         :on-click  [:item-browser/go-up! extension-id item-namespace]
                         :preset    :back-icon-button}]))



;; -- Error components --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [_ _]
  [:<> [elements/horizontal-separator {:size :xxl}]
       [elements/label {:min-height :m :content :an-error-occured :font-size :m}]
       [elements/label {:min-height :m :content :the-item-you-opened-may-be-broken :color :muted}]])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-mode-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [go-up-button                    extension-id item-namespace]
      [go-home-button                  extension-id item-namespace]
      [item-lister/new-item-block             extension-id item-namespace]
      [item-lister/sort-items-button          extension-id item-namespace]
      [item-lister/toggle-select-mode-button  extension-id item-namespace]
      [item-lister/toggle-reorder-mode-button extension-id item-namespace]]
    [:div.item-lister--header--menu-item-group
      [item-lister/search-block extension-id item-namespace]]])

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [% @(a/subscribe [:item-lister/get-menu-mode-props extension-id item-namespace])]
       [react-transition/mount-animation {:animation-timeout 500 :mounted? (:menu-mode? %)}
                                         [menu-mode-header-structure extension-id item-namespace]]))

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:new-item-options (vector)(opt)}
  ;
  ; @example
  ;  [item-browser/header :my-extension :my-type]
  ;
  ; @example
  ;  [item-browser/header :my-extension :my-type {:new-item-options [:add-my-type! :add-your-type!]}]
  ;
  ; @return (component)
  [extension-id item-namespace header-props]
  (let [header-props (assoc header-props :menu #'menu-mode-header)]
       [item-lister/header extension-id item-namespace header-props]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) body-props
  ;  {:list-element (metamorphic-content)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]}
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

(defn- view-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [extension-id item-namespace {:keys [description] :as view-props}]
  (if-let [error-mode? @(a/subscribe [:item-lister/error-mode? extension-id item-namespace])]
          ; If error-mode is enabled ...
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
  ;   :list-element (component)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :sortable? (boolean)(opt)
  ;    Default: false}
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
  [components/subscriber {:base-props view-props
                          :component  [view-structure               extension-id item-namespace]
                          :subscriber [:item-browser/get-view-props extension-id item-namespace]}])
