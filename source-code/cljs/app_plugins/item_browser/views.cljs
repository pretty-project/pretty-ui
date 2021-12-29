
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.3.2
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.views
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-db.api         :as db]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-layouts.api    :as layouts]
              [app-plugins.item-browser.engine :as engine]
              [app-plugins.item-lister.views   :as views]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-home-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:at-home? (boolean)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [at-home?]}]
  [elements/button ::go-home-button
                   {:disabled? at-home?
                    :on-click  (engine/go-home-event extension-id)
                    :preset    :home-icon-button}])
                    ;:icon-family :material-icons-outlined}])
                    ;:color :secondary}])

(defn go-up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:at-home? (boolean)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [at-home?]}]
  [elements/button ::go-up-button
                   {:disabled? at-home?
                    :on-click  (engine/go-up-event extension-id)
                    :preset    :up-icon-button}])
                    ;:color :secondary}])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn actions-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:no-items-to-show? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [no-items-to-show?] :as header-props}]
  [:div.item-lister--header--menu-bar
    [:div.item-lister--header--menu-item-group
      [views/new-item-select            extension-id item-namespace {:options [:upload-files! :create-directory!]}]
      [go-home-button          extension-id item-namespace header-props]
      [go-up-button            extension-id item-namespace header-props]
      [views/sort-items-button          extension-id item-namespace header-props]
      [views/toggle-select-mode-button  extension-id item-namespace header-props]]
      ;[views/toggle-reorder-mode-button extension-id item-namespace header-props]]
    [:div.item-lister--header--menu-item-group
      [views/search-block extension-id item-namespace header-props]]])

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
                                                 [views/search-mode-header  extension-id item-namespace]]
    [app-fruits.react-transition/mount-animation {:animation-timeout 500 :mounted? select-mode?}
                                                 [views/select-mode-header  extension-id item-namespace header-props]]
    [app-fruits.react-transition/mount-animation {:animation-timeout 500 :mounted? reorder-mode?}
                                                 [views/reorder-mode-header extension-id item-namespace header-props]]]
  [:div {:style {:display "none"}}
   [go-home-button          extension-id item-namespace header-props]
   [go-up-button            extension-id item-namespace header-props]]])

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) header-props
  ;
  ; @return (component)
  ([extension-id item-namespace]
   [header extension-id item-namespace {}])

  ([extension-id item-namespace header-props]
   (let [subscribed-props (a/subscribe [:item-lister/get-header-props extension-id item-namespace])]
        (fn [] [header-structure extension-id item-namespace (merge header-props @subscribed-props)]))))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  [extension-id item-namespace body-props]
  [views/body extension-id item-namespace body-props])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [extension-id item-namespace {:keys [description] :as view-props}]
  [layouts/layout-a extension-id {:body   {:content [body   extension-id item-namespace view-props]}
                                  :header {:content [header extension-id item-namespace]}
                                  :description description}])

(defn view
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) view-props
  ;  {:list-element (component)}
  ;
  ; @usage
  ;  [item-browser/view :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-list-element [item-dex item] [:div ...])
  ;  [item-browser/view :my-extension :my-type {:element #'my-list-element}]
  ;
  ; @return (component)
  [extension-id item-namespace view-props]
  (let [subscribed-props (a/subscribe [:item-browser/get-view-props extension-id item-namespace])]
       (fn [] [layout extension-id item-namespace (merge view-props @subscribed-props)])))
