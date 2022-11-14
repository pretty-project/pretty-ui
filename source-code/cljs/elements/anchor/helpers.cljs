
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.anchor.helpers
    (:require [elements.element.helpers :as element.helpers]
              [re-frame.api             :as r]
              [x.environment.api        :as x.environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn anchor-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [disabled? href on-click style]}]
  (if disabled? {:disabled       true
                 :style          style}
                {:data-clickable true
                 :href           href
                 :on-click       #(r/dispatch on-click)
                 :on-mouse-up    #(x.environment/blur-element!)
                 :style          style}))

(defn anchor-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ;  {:color (keyword or string)
  ;   :font-size (keyword)
  ;   :line-height (keyword)}
  ;
  ; @return (map)
  ;  {:data-font-size (keyword)
  ;   :data-line-height (keyword)}
  [anchor-id {:keys [color font-size line-height] :as anchor-props}]
  (merge (element.helpers/element-default-attributes anchor-id anchor-props)
         (element.helpers/element-indent-attributes  anchor-id anchor-props)
         (element.helpers/apply-color {} :color :data-color color)
         {:data-font-size   font-size
          :data-line-height line-height}))
