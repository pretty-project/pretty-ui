
(ns pretty-inputs.text-field.prototypes
    (:require [fruits.loop.api                     :refer [<-walk]]
              [fruits.vector.api                   :as vector]
              [pretty-inputs.engine.api            :as pretty-inputs.engine]
              [pretty-inputs.text-field.adornments :as text-field.adornments]
              [pretty-properties.api               :as pretty-properties]
              [pretty-rules.api                    :as pretty-rules]
              [pretty-standards.api                :as pretty-standards]
              [pretty-subitems.api                :as pretty-subitems]
              [react-references.api                :as react-references]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-props-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:on-click-f (function)}
  ;
  ; @return (maps)
  [field-id field-props {:keys [on-click-f] :as adornment-props}]
  ; Provides the actual field content to each adornment's 'on-click-f' function as a parameter.
  (letfn [(f0 [] (-> field-id (pretty-inputs.engine/get-input-displayed-value field-props) on-click-f))]
         (if on-click-f (-> adornment-props (assoc :on-click-f f0))
                        (-> adornment-props))))

(defn start-adornments-prototype
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:start-adornments (maps in vector)(opt)}
  ;
  ; @return (maps in vector)
  [field-id {:keys [start-adornments] :as field-props}]
  (letfn [(f0 [adornment-props] (adornment-props-prototype field-id field-props adornment-props))]
         (vector/->items start-adornments f0)))

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
  (letfn [(f0 [adornment-props] (adornment-props-prototype field-id field-props adornment-props))]
         (let [empty-field-adornment (text-field.adornments/empty-field-adornment field-id field-props)]
              (if emptiable? (-> end-adornments (vector/conj-item empty-field-adornment)
                                                (vector/->items f0))
                             (-> end-adornments (vector/->items f0))))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [id props]
  (-> props (pretty-properties/default-flex-props {:gap :xs :horizontal-align :left :orientation :vertical})
            (pretty-rules/auto-set-mounted)
            (pretty-subitems/ensure-subitem           :field)
            (pretty-subitems/subitems<-disabled-state :header :field)))

  ; XXX#5068
  ; By using the '<-walk' function the ':on-blur', ':on-type-ended' and ':on-focus'
  ; events take the 'field-props' map AFTER it gets merged with the default values!

  ;(<-walk {}));:font-size   :s
           ;:focus-id    field-id
           ;:form-id     field-id
           ;:font-weight :normal
           ;:line-height :text-block

;          (fn [%] (merge % (if border-color {:border-position :all
;                                             :border-width    :xxs
;                           (if marker-color {:marker-position :tr})}
          ;(fn [%] (merge % field-props))))
;          (fn [%] (merge % {:on-blur       [:pretty-inputs.text-field/field-blurred field-id %]
;                            :on-focus      [:pretty-inputs.text-field/field-focused field-id %]
;                            :on-type-ended [:pretty-inputs.text-field/type-ended    field-id %]))))
        ; input/autofill-rules


        ;field-props (pretty-elements.engine/element-subitem-field<-subitem-default field-id field-props :start-adornments :start-adornment-default)
        ;field-props (pretty-elements.engine/element-subitem-field<-disabled-state  field-id field-props :end-adornments   :end-adornment-default)
        ;field-props (pretty-elements.engine/element-subitem-field<-subitem-default field-id field-props :start-adornments :start-adornment-default)
        ;field-props (pretty-elements.engine/element-subitem-field<-disabled-state  field-id field-props :end-adornments   :end-adornment-default)
        ; auto-disable-input-autofill
        ; generate-input-autofill
          ; (pretty-properties/default-input-validation-props {:validate-when-leave? true})
