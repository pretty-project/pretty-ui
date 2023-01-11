
(ns elements.button.events
    (:require [candy.api    :refer [return]]
              [map.api      :refer [dissoc-in]]
              [re-frame.api :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-keypress-properties!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:keypress (map)(opt)
  ;  :on-click (metamorphic-event)}
  [db [_ button-id {:keys [keypress on-click]}]]
  (-> db (assoc-in [:elements :element-handler/meta-items button-id :keypress] keypress)
         (assoc-in [:elements :element-handler/meta-items button-id :on-click] on-click)))

(defn remove-keypress-properties!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [db [_ button-id _]]
  (-> db (dissoc-in [:elements :element-handler/meta-items button-id :keypress])
         (dissoc-in [:elements :element-handler/meta-items button-id :on-click])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:keypress (map)(opt)}
  [db [_ button-id {:keys [keypress] :as button-props}]]
  (if keypress (r store-keypress-properties! db button-id button-props)
               (return db)))

(defn button-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:keypress (map)(opt)}
  [db [_ button-id {:keys [keypress] :as button-props}]]
  (if keypress (r store-keypress-properties! db button-id button-props)
               (return db)))

(defn button-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; {:keypress (map)(opt)}
  [db [_ button-id {:keys [keypress] :as button-props}]]
  (if keypress (r remove-keypress-properties! db button-id button-props)
               (return db)))
