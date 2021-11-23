
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.2.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.views
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-db.api         :as db]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-plugins.item-browser.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn home-button
  ; @param (keyword) extension-id
  ; @param (map) element-props
  ;  {:at-home? (boolean)}
  ;
  ; @usage
  ;  [item-browser/home-button :my-extension {:at-home? false}]
  ;
  ; @return (component)
  [extension-id {:keys [at-home?]}]
  [elements/button ::home-button
                   {:disabled? (param at-home?)

                  ;:on-click  [:media/go-home!]
                    :on-click  (engine/go-home-event extension-id item-namespace)
                    :preset    :home-icon-button}])

(defn up-button
  ; @param (keyword) extension-id
  ; @param (map) element-props
  ;  {:at-home? (boolean)}
  ;
  ; @usage
  ;  [item-browser/up-button :my-extension {:at-home? false}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [at-home?]}]
  [elements/button ::up-button
                   {:disabled? (param at-home?)
                    :on-click  (engine/go-up-event extension-id item-namespace)
                    :preset    :up-icon-button}])
