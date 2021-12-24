
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
              [app-plugins.item-lister.api     :as item-lister]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- home-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:at-home? (boolean)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [at-home?]}]
  [elements/button ::home-button
                   {:disabled? at-home?
                    :on-click  (engine/go-home-event extension-id)
                    :preset    :home-icon-button}])

(defn- up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:at-home? (boolean)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [at-home?]}]
  [elements/button ::up-button
                   {:disabled? at-home?
                    :on-click  (engine/go-up-event extension-id)
                    :preset    :up-icon-button}])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  [extension-id item-namespace]
  [:div "header"])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  [extension-id item-namespace body-props]
  [item-lister/body extension-id item-namespace body-props])



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
