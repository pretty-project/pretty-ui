
(ns elements.digit-field.helpers
    (:require [elements.digit-field.config :as digit-field.config]
              [pretty-css.api              :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props->digits-width
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) field-props
  ;
  ; @return (integer)
  [field-props]
  (+ (* digit-field.config/DIGIT-WIDTH 4)
     (* digit-field.config/DIGIT-GAP   3)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [style] :as field-props}]
  (merge (pretty-css/indent-attributes field-props)
         {:style style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [_ field-props]
  (merge (pretty-css/default-attributes field-props)
         (pretty-css/outdent-attributes field-props)))
