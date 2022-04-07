
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.header.views
    (:require [plugins.item-lister.api :as item-lister]
              [x.app-core.api          :as a]
              [x.app-elements.api      :as elements]))



;; ----------------------------------------------------------------------------
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



;; ----------------------------------------------------------------------------
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
