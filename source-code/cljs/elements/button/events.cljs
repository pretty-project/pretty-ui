
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.button.events
    (:require [mid-fruits.map :refer [dissoc-in]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)
  ;   :on-click (metamorphic-event)}
  [db [_ button-id {:keys [keypress on-click]}]]
  (-> db (assoc-in [:elements :element-handler/meta-items button-id :keypress] keypress)
         (assoc-in [:elements :element-handler/meta-items button-id :on-click] on-click)))

(defn button-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)
  ;   :on-click (metamorphic-event)}
  [db [_ button-id {:keys [keypress on-click]}]]
  (-> db (assoc-in [:elements :element-handler/meta-items button-id :keypress] keypress)
         (assoc-in [:elements :element-handler/meta-items button-id :on-click] on-click)))

(defn button-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [db [_ button-id _]]
  (-> db (dissoc-in [:elements :element-handler/meta-items button-id :keypress])
         (dissoc-in [:elements :element-handler/meta-items button-id :on-click])))
