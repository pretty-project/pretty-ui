
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.4.6
; Compatibility: x4.5.3



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
              [app-plugins.item-browser.engine :as engine]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-home-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:at-home? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [at-home? error-mode?]}]
  [elements/button ::go-home-button
                   ; A go-home-button gomb a böngésző {:error-mode? true} állapotában használható!
                   {:disabled? (and at-home? (not error-mode?))
                    :on-click  [:item-browser/go-home! extension-id item-namespace]
                    :preset    :home-icon-button}])

(defn go-up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:at-home? (boolean)(opt)
  ;   :disabled? (boolean)(opt)}
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
       [elements/label {:content :an-error-occured :font-size :m :layout :fit}]
       [elements/horizontal-separator {:size :xs}]
       [elements/label {:content :the-item-you-opened-may-be-broken :color :muted :layout :fit}]
       [elements/horizontal-separator {:size :xs}]])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn actions-mode-header
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
      (if new-item-options [app-plugins.item-lister.views/new-item-select extension-id item-namespace header-props]
                           [app-plugins.item-lister.views/new-item-button extension-id item-namespace])
      [go-home-button                  extension-id item-namespace header-props]
      [go-up-button                    extension-id item-namespace header-props]
      [app-plugins.item-lister.views/sort-items-button         extension-id item-namespace header-props]
      [app-plugins.item-lister.views/toggle-select-mode-button extension-id item-namespace header-props]]
      ;[app-plugins.item-lister.views/toggle-reorder-mode-button extension-id item-namespace header-props]]
    [:div.item-lister--header--menu-item-group
      [app-plugins.item-lister.views/search-block extension-id item-namespace header-props]]])

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:actions-mode? (boolean)(opt)
  ;   :reorder-mode? (boolean)(opt)
  ;   :search-mode? (boolean)(opt)
  ;   :select-mode? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [actions-mode? reorder-mode? search-mode? select-mode?] :as header-props}]
 [:div {:style {:width "100%"}}
  [:div#item-lister--header--structure
    [app-fruits.react-transition/mount-animation {:animation-timeout 500 :mounted? actions-mode?}
                                                 [actions-mode-header extension-id item-namespace header-props]]
    [app-fruits.react-transition/mount-animation {:animation-timeout 500 :mounted? search-mode?}
                                                 [app-plugins.item-lister.views/search-mode-header  extension-id item-namespace]]
    [app-fruits.react-transition/mount-animation {:animation-timeout 500 :mounted? select-mode?}
                                                 [app-plugins.item-lister.views/select-mode-header  extension-id item-namespace header-props]]
    [app-fruits.react-transition/mount-animation {:animation-timeout 500 :mounted? reorder-mode?}
                                                 [app-plugins.item-lister.views/reorder-mode-header extension-id item-namespace header-props]]]
  [:div {:style {:display "none"}}
   [go-home-button          extension-id item-namespace header-props]
   [go-up-button            extension-id item-namespace header-props]]])

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
        (fn [] [header-structure extension-id item-namespace (merge header-props @subscribed-props)]))))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  [extension-id item-namespace body-props]
  [app-plugins.item-lister.views/body extension-id item-namespace body-props])



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
