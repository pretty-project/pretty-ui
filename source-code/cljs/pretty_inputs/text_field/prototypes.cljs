
(ns pretty-inputs.text-field.prototypes
    (:require [fruits.loop.api                     :refer [<-walk]]
              [fruits.vector.api                   :as vector]
              [pretty-css.api :as pretty-css]
              [pretty-engine.api                   :as pretty-engine]
              [pretty-inputs.text-field.adornments :as text-field.adornments]))

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
  (letfn [(f0 [] (-> field-id (pretty-engine/get-input-displayed-value field-props) on-click-f))]
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
  (<-walk {:font-size   :s
           :focus-id    field-id
           :form-id     field-id
           :font-weight :normal
           :line-height :text-block
           :type        :text}
          (fn [%] (merge % (if border-color {:border-position :all
                                             :border-width    :xxs})
                           (if marker-color {:marker-position :tr})))
          (fn [%] (merge % field-props))
          (fn [%] (merge % {:on-blur       [:pretty-inputs.text-field/field-blurred field-id %]
                            :on-focus      [:pretty-inputs.text-field/field-focused field-id %]
                            :on-type-ended [:pretty-inputs.text-field/type-ended    field-id %]}))))
