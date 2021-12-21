
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.07
; Description:
; Version: v0.5.6
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.dialogs
    (:require [mid-fruits.candy   :refer [param return]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [app-plugins.item-lister.engine :as engine]))



;; -- Archive items components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- close-archive-selected-items-dialog-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [dialog-id (engine/dialog-id extension-id item-namespace :archive-items)]
       [elements/button ::close-archive-selected-items-dialog-button
                        {:label :cancel! :preset :default-button
                         :keypress {:key-code 27}
                         :on-click [:ui/close-popup! dialog-id]}]))

(defn- archive-selected-items-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [dialog-id (engine/dialog-id extension-id item-namespace :archive-items)]
       [elements/button ::archive-selected-items-button
                        {:label :archive! :preset :primary-button
                         :keypress {:key-code 13}
                         :on-click {:dispatch-n [[:ui/close-popup! dialog-id]
                                                 [:item-lister/archive-selected-items! extension-id]]}}]))

(defn- archive-selected-items-dialog-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [elements/polarity ::archive-selected-items-dialog-header
                     {:start-content [close-archive-selected-items-dialog-button extension-id item-namespace]
                      :end-content   [archive-selected-items-button              extension-id item-namespace]}])

(defn- archive-selected-items-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [_]
  [elements/text {:content :archive-selected-items? :selectable? true :font-weight :bold}])



;; -- Items deleted components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- selected-items-archived-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (component)
  [extension-id item-namespace item-ids]
  (let [dialog-id (engine/dialog-id extension-id item-namespace :items-archived)]
       [:<> [elements/horizontal-separator {:size :s}]
            [elements/label {:content :n-items-archived :layout :fit :replacements [(count item-ids)]}]
            [elements/horizontal-separator {:size :s}]
            [elements/button {:label :recover! :preset :primary-button :layout :fit
                              :on-click {:dispatch-n [[:item-lister/undo-archive-items! extension-id item-namespace item-ids]
                                                      [:ui/pop-bubble! dialog-id]]}}]
            [elements/horizontal-separator {:size :s}]]))



;; -- Delete items components -------------------------------------------------
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
       [elements/button ::close-delete-selected-items-dialog-button
                        {:label :cancel! :preset :default-button
                         :keypress {:key-code 27}
                         :on-click [:ui/close-popup! dialog-id]}]))

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
                         :keypress {:key-code 13}
                         :on-click {:dispatch-n [[:ui/close-popup! dialog-id]
                                                 [:item-lister/delete-selected-items! extension-id item-namespace]]}}]))

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



;; -- Items deleted components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- selected-items-deleted-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (component)
  [extension-id item-namespace item-ids]
  (let [dialog-id (engine/dialog-id extension-id item-namespace :items-deleted)]
       [:<> [elements/horizontal-separator {:size :s}]
            [elements/label {:content :n-items-deleted :layout :fit :replacements [(count item-ids)]}]
            [elements/horizontal-separator {:size :s}]
            [elements/button {:label :recover! :preset :primary-button :layout :fit
                              :on-click {:dispatch-n [[:item-lister/undo-delete-items! extension-id item-namespace item-ids]
                                                      [:ui/pop-bubble! dialog-id]]}}]
            [elements/horizontal-separator {:size :s}]]))



;; -- Archive items lifecycle events ------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/render-archive-selected-items-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [_ [_ extension-id item-namespace]]
      [:ui/add-popup! (engine/dialog-id extension-id item-namespace :archive-items)
                      {:body   {:content [archive-selected-items-dialog-body   extension-id]}
                       :header {:content [archive-selected-items-dialog-header extension-id item-namespace]}}]))

(a/reg-event-fx
  :item-lister/render-selected-items-archived-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  (fn [_ [_ extension-id item-namespace item-ids]]
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :items-archived)
                        {:body {:content [selected-items-archived-dialog-body extension-id item-namespace item-ids]}}]))



;; -- Delete items lifecycle events -------------------------------------------
;; ----------------------------------------------------------------------------

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

(a/reg-event-fx
  :item-lister/render-selected-items-deleted-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  (fn [_ [_ extension-id item-namespace item-ids]]
      [:ui/blow-bubble! (engine/dialog-id extension-id item-namespace :items-deleted)
                        {:body {:content [selected-items-deleted-dialog-body extension-id item-namespace item-ids]}
                         :destructor [:item-lister/clean-backup-items! extension-id item-namespace item-ids]}]))
