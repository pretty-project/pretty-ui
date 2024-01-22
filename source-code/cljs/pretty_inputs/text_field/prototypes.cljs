
(ns pretty-inputs.text-field.prototypes
    (:require [fruits.loop.api              :refer [<-walk]]
              [fruits.vector.api            :as vector]
              [pretty-build-kit.api         :as pretty-build-kit]
              [pretty-inputs.input.utils    :as input.utils]
              [pretty-inputs.text-field.adornments :as text-field.adornments]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn end-adornments-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:emptiable? (boolean)(opt)
  ;  :end-adornments (maps in vector)(opt)}
  ;
  ; @return (maps in vector)
  [field-id {:keys [emptiable? end-adornments] :as field-props}]
  (if emptiable? (let [empty-field-adornment (text-field.adornments/empty-field-adornment field-id field-props)]
                      (vector/conj-item end-adornments empty-field-adornment))
                 (-> end-adornments)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:border-color (keyword or string)(opt)
  ;  :marker-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :font-size (keyword, px or string)
  ;  :font-weight (keyword or integer)
  ;  :form-id (keyword)
  ;  :line-height (keyword, px or string)
  ;  :marker-position (keyword)
  ;  :type (keyword)
  ;  :value-path (Re-Frame path vector)}
  [field-id {:keys [border-color marker-color] :as field-props}]
  ; XXX#5068
  ; By using the '<-walk' function the ':on-blur', ':on-type-ended' and ':on-focus'
  ; events take the 'field-props' map AFTER it gets merged with the default values!
  (<-walk {:font-size       :s
           :focus-id        field-id
           :form-id         field-id
           :font-weight     :normal
           :line-height     :text-block
           :type            :text
           :value-path      (input.utils/default-value-path field-id)}
          (fn [%] (merge % (if border-color {:border-position :all
                                             :border-width    :xxs})
                           (if marker-color {:marker-position :tr})))
          (fn [%] (merge % field-props))
          (fn [%] (merge % {:on-blur       [:pretty-inputs.text-field/field-blurred field-id %]
                            :on-focus      [:pretty-inputs.text-field/field-focused field-id %]
                            :on-type-ended [:pretty-inputs.text-field/type-ended    field-id %]}))))
