
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.07
; Description:
; Version: v0.4.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.dialogs
    (:require [mid-fruits.candy   :refer [param return]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [mid-plugins.item-lister.engine :as engine]))



;; -- Duplicate dialog components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn- close-duplicate-selected-items-dialog-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [dialog-id (engine/dialog-id extension-id item-namespace :delete-items)]
       [elements/button ::close-dialog-button
                        {:label :cancel! :preset :default-button :on-click [:ui/close-popup! dialog-id]}]))

(defn- duplicate-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [dialog-id (engine/dialog-id extension-id item-namespace :delete-items)]
       [elements/button ::duplicate-selected-items-button
                        {:label :copy! :preset :primary-button
                         :on-click {:dispatch-n [[:ui/close-popup!                       dialog-id]
                                                 [:item-lister/duplicate-selected-items! extension-id]]}}]))

(defn- duplicate-selected-items-dialog-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [elements/polarity ::duplicate-selected-items-dialog-header
                     {:start-content [close-duplicate-selected-items-dialog-button extension-id item-namespace]
                      :end-content   [duplicate-selected-items-button              extension-id item-namespace]}])

(defn- duplicate-selected-items-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [_]
  [elements/text {:content :duplicate-selected-items? :selectable? true :font-weight :bold}])



;; -- Delete dialog components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- close-delete-selected-items-dialog-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [dialog-id (engine/dialog-id extension-id item-namespace :delete-items)]
       [elements/button ::close-dialog-button
                        {:label :cancel! :preset :default-button :on-click [:ui/close-popup! dialog-id]}]))

(defn- delete-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [dialog-id (engine/dialog-id extension-id item-namespace :delete-items)]
       [elements/button ::delete-selected-items-button
                        {:label :delete! :preset :warning-button
                         :on-click {:dispatch-n [[:ui/close-popup!                    dialog-id]
                                                 [:item-lister/delete-selected-items! extension-id]]}}]))

(defn- delete-selected-items-dialog-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [elements/polarity ::delete-selected-items-dialog-header
                     {:start-content [close-delete-selected-items-dialog-button extension-id item-namespace]
                      :end-content   [delete-selected-items-button              extension-id item-namespace]}])

(defn- delete-selected-items-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [_]
  [elements/text {:content :delete-selected-items? :selectable? true :font-weight :bold}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/render-duplicate-selected-items-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [_ [_ extension-id item-namespace]]
      [:ui/add-popup! (engine/dialog-id extension-id item-namespace :duplicate-items)
                      {:body   {:content [duplicate-selected-items-dialog-body   extension-id]}
                       :header {:content [duplicate-selected-items-dialog-header extension-id item-namespace]}}]))

(a/reg-event-fx
  :item-lister/render-delete-selected-items-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [_ [_ extension-id item-namespace]]
      [:ui/add-popup! (engine/dialog-id extension-id item-namespace :delete-items)
                      {:body   {:content [delete-selected-items-dialog-body   extension-id]}
                       :header {:content [delete-selected-items-dialog-header extension-id item-namespace]}}]))
