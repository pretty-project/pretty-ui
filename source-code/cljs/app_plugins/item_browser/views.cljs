
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.views
    (:require [app-fruits.react-transition     :as react-transition]
              [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-lister.api     :as item-lister]
              [mid-fruits.candy                :refer [param]]
              [x.app-components.api            :as components]
              [x.app-core.api                  :as a]
              [x.app-elements.api              :as elements]
              [x.app-layouts.api               :as layouts]
              [x.app-tools.api                 :as tools]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:collection-name (string)
  ;   :items-key (keyword)
  ;   :label-key (keyword)
  ;   :path-key (keyword)}
  [extension-id _ body-props]
  (merge {:collection-name (name extension-id)
          :items-key :items
          :label-key :name
          :path-key  :path}
         (param body-props)))



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
        error-mode?       @(a/subscribe [:item-lister/error-mode?        extension-id item-namespace])
        at-home?          @(a/subscribe [:item-browser/at-home?          extension-id item-namespace])]
       [elements/button ::go-home-button
                        ; A go-home-button gomb az item-lister plugin {:error-mode? true} állapotában is használható!
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

(defn menu-mode-header-structure
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
      [item-lister/new-item-select            extension-id item-namespace]
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
  [extension-id item-namespace]
  (let [menu-mode? @(a/subscribe [:item-lister/menu-mode? extension-id item-namespace])]
       [react-transition/mount-animation {:animation-timeout 500 :mounted? menu-mode?}
                                         [menu-mode-header-structure extension-id item-namespace]]))

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

(defn body
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: false
  ;   :download-limit (integer)(opt)
  ;    Default: 20
  ;   :handler-key (keyword)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :items-key (keyword)(opt)
  ;    Default: :items
  ;   :label-key (keyword)(opt)
  ;    Default: :name
  ;   :list-element (metamorphic-content)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;   :order-by-options (namespaced keywords in vector)(opt)
  ;    Default: [:modified-at/descending :modified-at/ascending :name/ascending :name/descending]
  ;   :path-key (keyword)(opt)
  ;    Default: :path
  ;   :prefilter (map)(opt)
  ;   :search-keys (keywords in vector)(opt)
  ;    Default: [:name]}
  ;
  ; @example
  ;  [item-browser/body :my-extension :my-type {...}]
  ;
  ; @example
  ;  (defn my-list-element [extension-id item-namespace item-dex item] [:div ...])
  ;  [item-browser/body :my-extension :my-type {:list-element #'my-list-element
  ;                                             :prefilter    {:my-type/color "red"}}]
  [extension-id item-namespace body-props]
  (let [body-props (body-props-prototype extension-id item-namespace body-props)]
       [item-lister/body extension-id item-namespace body-props]))
