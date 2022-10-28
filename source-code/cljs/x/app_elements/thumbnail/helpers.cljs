
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.thumbnail.helpers
    (:require [re-frame.api                   :as r]
              [x.app-elements.element.helpers :as element.helpers]
              [x.app-environment.api          :as x.environment]))



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
  [_ {:keys [disabled? on-click style]}]
  (if disabled? {:disabled        true
                 :style           style}
                {:data-clickable  true
                 :on-click       #(r/dispatch on-click)
                 :on-mouse-up    #(x.environment/blur-element!)
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
