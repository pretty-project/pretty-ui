
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.text.helpers
    (:require [css.api                  :as css]
              [elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [font-size max-lines selectable? style]}]
  (if max-lines (let [line-height-var (css/var  (str "line-height-" (name font-size)))
                      height-calc     (css/calc (str max-lines" * "line-height-var))]
                     {:data-cropped    true
                      :data-selectable selectable?
                      :style           (assoc style :max-height height-calc)})
                {:data-selectable selectable?
                 :style           style}))

(defn text-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [text-id {:keys [color font-size font-weight horizontal-align line-height min-width] :as text-props}]
  (merge (element.helpers/element-default-attributes text-id text-props)
         (element.helpers/element-indent-attributes  text-id text-props)
         (element.helpers/apply-color {} :color :data-color color)
         {:data-font-size        font-size
          :data-font-weight      font-weight
          :data-horizontal-align horizontal-align
          :data-line-height      line-height
          :data-min-width        min-width}))