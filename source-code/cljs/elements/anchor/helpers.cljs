
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
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

(defn anchor-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ; {:color (keyword or string)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [color style]}]
  (-> {:style style}
      (element.helpers/apply-color :color :data-color color)))

(defn anchor-font-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ; {:font-size (keyword)
  ;  :line-height (keyword)}
  ;
  ; @return (map)
  ; {:data-font-size (keyword)
  ;  :data-line-height (keyword)}
  [_ {:keys [font-size line-height]}]
  {:data-font-size   font-size
   :data-line-height line-height})

(defn anchor-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [anchor-id {:keys [disabled? href on-click] :as anchor-props}]
  (merge {}
         (anchor-style-attributes anchor-id anchor-props)
         (anchor-font-attributes  anchor-id anchor-props)
         (if disabled? {:disabled       true}
                       {:data-clickable true
                        :href           href
                        :on-click       #(r/dispatch on-click)
                        :on-mouse-up    #(x.environment/blur-element!)})))

(defn anchor-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ;
  ; @return (map)
  [anchor-id anchor-props]
  (merge (element.helpers/element-default-attributes anchor-id anchor-props)
         (element.helpers/element-indent-attributes  anchor-id anchor-props)))
