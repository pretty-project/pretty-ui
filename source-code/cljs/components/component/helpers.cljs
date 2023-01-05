
(ns components.component.helpers
    (:require [elements.element.helpers]
              [hiccup.api :as hiccup]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; elements.element.helpers
(def apply-preset                 elements.element.helpers/apply-preset)
(def apply-color                  elements.element.helpers/apply-color)
(def apply-dimension              elements.element.helpers/apply-dimension)
(def component-indent-attributes  elements.element.helpers/element-indent-attributes)
(def component-outdent-attributes elements.element.helpers/element-outdent-attributes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component-default-attributes
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)}
  ;
  ; @return
  ; {:class (keywords in vector)
  ;  :data-disabled (boolean)}
  [_ {:keys [class disabled?]}]
  {:class         (hiccup/join-class :c-component class)
   :data-disabled (boolean disabled?)})
