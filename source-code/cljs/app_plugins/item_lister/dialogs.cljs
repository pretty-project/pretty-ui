
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



;; -- Common components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- close-dialog-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::close-dialog-button
                   {:label :cancel! :preset :default-button :on-click [:ui/close-popup! extension-id]}])



;; -- Duplicate dialog components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn- duplicate-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::duplicate-selected-items-button
                   {:label :copy! :preset :primary-button
                    :on-click {:dispatch-n [[:ui/close-popup!                       extension-id]
                                            [:item-lister/duplicate-selected-items! extension-id]]}}])

(defn- duplicate-selected-items-dialog-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/polarity ::duplicate-selected-items-dialog-header
                     {:start-content [close-dialog-button             extension-id]
                      :end-content   [duplicate-selected-items-button extension-id]}])

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

(defn- delete-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::delete-selected-items-button
                   {:label :copy! :preset :primary-button
                    :on-click {:dispatch-n [[:ui/close-popup!                    extension-id]
                                            [:item-lister/delete-selected-items! extension-id]]}}])

(defn- delete-selected-items-dialog-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/polarity ::delete-selected-items-dialog-header
                     {:start-content [close-dialog-button          extension-id]
                      :end-content   [delete-selected-items-button extension-id]}])

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
  (fn [_ [_ extension-id]]
      [:ui/add-popup! extension-id
                      {:body   {:content #'duplicate-selected-items-dialog-body}
                       :header {:content #'duplicate-selected-items-dialog-header}}]))

(a/reg-event-fx
  :item-lister/render-delete-selected-items-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  (fn [_ [_ extension-id]]
      [:ui/add-popup! extension-id
                      {:body   {:content #'delete-selected-items-dialog-body}
                       :header {:content #'delete-selected-items-dialog-header}}]))
