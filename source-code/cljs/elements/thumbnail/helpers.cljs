
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.thumbnail.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [re-frame.api                  :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-thumbnail-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [thumbnail-id {:keys [disabled? on-click style]}]
  (if disabled? {:disabled        true
                 :style           style}
                {:data-clickable  true
                 :on-click       #(r/dispatch on-click)
                 :on-mouse-up    #(element.side-effects/blur-element! thumbnail-id)
                 :style           style}))

(defn static-thumbnail-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [style]}]
  {:style style})

(defn thumbnail-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:border-radius (keyword)
  ;   :height (keyword)
  ;   :width (keyword)}
  ;
  ; @return (map)
  ;  {:data-border-radius (keyword)
  ;   :data-height (keyword)
  ;   :data-width (keyword)}
  [thumbnail-id {:keys [background-size border-radius height width] :as thumbnail-props}]
  (merge (element.helpers/element-default-attributes thumbnail-id thumbnail-props)
         (element.helpers/element-indent-attributes  thumbnail-id thumbnail-props)
         {:data-border-radius border-radius
          :data-height        height
          :data-selectable    false
          :data-width         width}))
